package com.hajela.authservice.services.impl;


import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.dto.ResetPasswordDto;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.entities.Role;
import com.hajela.authservice.entities.RoleEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.entities.UserStatus;
import com.hajela.authservice.exceptions.*;
import com.hajela.authservice.repo.RoleRepository;
import com.hajela.authservice.repo.UserRepository;
import com.hajela.authservice.services.UserActivationService;
import com.hajela.authservice.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailServiceImpl;
    private final UserActivationService activationService;

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createNewUser(RegistrationRequest registrationRequest) {
        Optional<UserEntity> entityByEmail = userRepository.findByEmail(registrationRequest.getEmail());
        if (entityByEmail.isPresent()) {
            throw new UserAlreadyExists(registrationRequest.getEmail());
        }
        UserEntity userEntity = UserEntity.from(registrationRequest);
        userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntity.setStatus(UserStatus.CREATED.name());
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByName(registrationRequest.getRole());
        if (roleEntityOptional.isEmpty()) {
            throw new InvalidRoleException(registrationRequest.getRole().getRoleName());
        }
        userEntity.setRole(roleEntityOptional.get());

        userEntity = userRepository.save(userEntity);
        log.info("Created New User={}", userEntity);
        activationService.createNewActivationCode(userEntity);
        emailServiceImpl.sendActivationEmail(userEntity);
    }

    public Page<UserDto> findAllUsers(Pageable pageable, String role) {
        RoleEntity roleEntity = roleRepository.findByName(Role.fromRole(role))
                .orElseThrow(() -> new InvalidRoleException(role));

        Page<UserEntity> userPage = userRepository.findAllByRoleOrderByUserIdDesc(pageable, roleEntity);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(UserDto::from)
                .toList();
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }

    public UserDto findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDto::from)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    public RoleEntity saveRole(RoleEntity role) {
        log.info("Saving role={}", role);
        return roleRepository.save(role);
    }


    public UserEntity login(AuthRequest authRequest) {

        UserEntity user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new InvalidEmailException(authRequest.getEmail()));

        if (!UserStatus.ACTIVATED.name().equals(user.getStatus())) {
            throw new UserStatusBlockedException(user.getStatus());
        }

        boolean passwordMatches = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        log.info("Password matches {}", passwordMatches);

        if (passwordMatches) {
            return user;
        }

        throw new IncorrectPasswordException(authRequest.getEmail());
    }

    public UserEntity updateUserStatusToActivated(UserEntity user) {
        user.setStatus(UserStatus.ACTIVATED.name());
        return userRepository.save(user);
    }

    public void updateUserStatusToForgotPassword(UserEntity user) {
        user.setStatus(UserStatus.FORGOT_PASSWORD.name());
        userRepository.save(user);
    }

    public void resetUserPassword(UserEntity user, ResetPasswordDto resetPasswordDto) {
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}

package com.hajela.authservice.services;


import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.entities.Role;
import com.hajela.authservice.entities.RoleEntity;
import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.*;
import com.hajela.authservice.repo.RoleRepository;
import com.hajela.authservice.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }

    public void createNewUser(RegistrationRequest registrationRequest) {
        Optional<UserEntity> entityByEmail = userRepository.findByEmail(registrationRequest.getEmail());
        if (entityByEmail.isPresent()) {
            throw new UserAlreadyExists(registrationRequest.getEmail());
        }
        UserEntity userEntity = UserEntity.from(registrationRequest);
        userEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntity.setStatus("CREATED");
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByName(registrationRequest.getRole());
        if (roleEntityOptional.isEmpty()) {
            throw new InvalidRoleException(registrationRequest.getRole().getRoleName());
        }
        userEntity.setRole(roleEntityOptional.get());

        userRepository.save(userEntity);
        log.info("Created New User={}", userEntity);
    }

    public Page<UserDto> findAllUsers(Pageable pageable, String role) {
        RoleEntity roleEntity = roleRepository.findByName(Role.fromRole(role)).get();
        Page<UserEntity> userPage = userRepository.findAllByRoleOrderByUserIdDesc(pageable, roleEntity);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(UserDto::from)
                .toList();
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }

    public UserDto findUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(UserDto::from)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    public RoleEntity saveRole(RoleEntity role) {
        log.info("Saving role={}", role);
        return roleRepository.save(role);
    }


    public UserEntity login(AuthRequest authRequest) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(authRequest.getEmail());
        if (userEntityOptional.isEmpty()) {
            throw new InvalidEmailException(authRequest.getEmail());
        }

        var passwordMatches = passwordEncoder.matches(authRequest.getPassword(), userEntityOptional.get().getPassword());
        log.info("Password matches {}", passwordMatches);

        if (passwordMatches) {
            return userEntityOptional.get();
        }

        throw new IncorrectPasswordException(authRequest.getEmail());
    }

}

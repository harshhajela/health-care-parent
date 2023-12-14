package com.hajela.authservice.services;

import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.dto.ResetPasswordDto;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.entities.RoleEntity;
import com.hajela.authservice.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    public Optional<UserEntity> findUserByEmail(String email);

    public void createNewUser(RegistrationRequest registrationRequest);

    public Page<UserDto> findAllUsers(Pageable pageable, String role);

    public UserDto findUserById(Long userId);

    public RoleEntity saveRole(RoleEntity role);

    public UserEntity login(AuthRequest authRequest);

    public UserEntity updateUserStatusToActivated(UserEntity user);

    public void updateUserStatusToForgotPassword(UserEntity user);

    public void resetUserPassword(UserEntity user, ResetPasswordDto resetPasswordDto);
}

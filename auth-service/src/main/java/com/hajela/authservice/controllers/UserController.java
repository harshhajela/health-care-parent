package com.hajela.authservice.controllers;


import com.hajela.authservice.dto.UpdateUserPasswordDto;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.exceptions.InvalidEmailException;
import com.hajela.authservice.exceptions.UserIdNotFoundException;
import com.hajela.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/role/{role}")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                     @PathVariable(value = "role") String role) {
        return ResponseEntity.ok(userService.findAllUsers(pageable, role));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "userId") Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId).map(UserDto::from)
                .orElseThrow(() -> new UserIdNotFoundException(userId)));

    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(value = "email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email).map(UserDto::from)
                .orElseThrow(() -> new InvalidEmailException(email)));
    }

    @PutMapping(value = "/reset-password/{userId}")
    public ResponseEntity<Void> updateUserPassword(@PathVariable(value = "userId") Long userId,
                                                   @Validated @RequestBody UpdateUserPasswordDto userPasswordDto) {
        userService.updatePassword(userId, userPasswordDto);
        return ResponseEntity.ok().build();
    }
}

package com.hajela.authservice.controllers;


import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.services.RefreshTokenService;
import com.hajela.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping(value = "/{role}")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                     @PathVariable(value = "role") String role) {
        return ResponseEntity.ok(userService.findAllUsers(pageable, role));
    }

    /*@GetMapping(value = "/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(value = "email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveNewUser(@RequestBody UserRegistrationDto userDto) {
        UserEntity newUser = userService.createNewUser(userDto);
        return ResponseEntity.ok(UserDto.from(newUser));
    }



    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "userId") Integer userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping(value = "/createRefreshToken")
    public ResponseEntity<String> refreshToken(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(refreshTokenService.createRefreshToken(loginDto.getEmail()));
    }*/
}

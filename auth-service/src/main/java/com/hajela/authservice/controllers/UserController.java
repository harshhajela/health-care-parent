package com.hajela.authservice.controllers;


import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.exceptions.InvalidEmailException;
import com.hajela.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(value = "email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email).map(UserDto::from)
                .orElseThrow(() -> new InvalidEmailException(email)));
    }

}

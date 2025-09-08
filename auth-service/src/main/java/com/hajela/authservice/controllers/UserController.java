package com.hajela.authservice.controllers;


import com.hajela.authservice.dto.UpdateUserPasswordDto;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.exceptions.InvalidEmailException;
import com.hajela.authservice.exceptions.UserIdNotFoundException;
import com.hajela.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "User management operations for administrators")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get users by role", description = "Retrieve paginated list of users filtered by role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping(value = "/role/{role}")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @Parameter(description = "Pagination parameters") @PageableDefault(page = 0, size = 5) Pageable pageable,
            @Parameter(description = "User role (CUSTOMER, PROVIDER, ADMIN)", required = true) @PathVariable(value = "role") String role) {
        return ResponseEntity.ok(userService.findAllUsers(pageable, role));
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable(value = "userId") Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId).map(UserDto::from)
                .orElseThrow(() -> new UserIdNotFoundException(userId)));

    }

    @Operation(summary = "Get user by email", description = "Retrieve user details by email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @Parameter(description = "User email address", required = true) @PathVariable(value = "email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email).map(UserDto::from)
                .orElseThrow(() -> new InvalidEmailException(email)));
    }

    @Operation(summary = "Reset user password", description = "Reset password for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping(value = "/reset-password/{userId}")
    public ResponseEntity<Void> updateUserPassword(
            @Parameter(description = "User ID", required = true) @PathVariable(value = "userId") Long userId,
            @Parameter(description = "New password details", required = true) @Validated @RequestBody UpdateUserPasswordDto userPasswordDto) {
        userService.updatePassword(userId, userPasswordDto);
        return ResponseEntity.ok().build();
    }
}

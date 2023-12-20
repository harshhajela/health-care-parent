package com.example.bookingservice.client;


import com.example.bookingservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth-service.url}")
    private String authServiceUrl;

    public Optional<UserDto> getUserByEmail(String email) {
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(
                authServiceUrl +"/v1/users/" + email,
                UserDto.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(responseEntity.getBody());
        } else {
            return Optional.empty();
        }
    }

}

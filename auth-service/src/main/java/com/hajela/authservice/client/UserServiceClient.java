package com.hajela.authservice.client;

import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.dto.UserDto;
import com.hajela.authservice.exceptions.InvalidAuthCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public void registerUser(RegistrationRequest registrationRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegistrationRequest> requestEntity = new HttpEntity<>(registrationRequest, headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(
                "http://USER-SERVICE/v1/users",
                requestEntity,
                UserDto.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            var response = responseEntity.getBody();
            assert response != null;

        } else {
            // Handle error here or throw an exception

        }
    }

    public UserDto login(AuthRequest authRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(authRequest, headers);

        try {
            ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(
                    "http://USER-SERVICE/v1/users/login",
                    requestEntity,
                    UserDto.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // Handle error here or throw an exception
                return null;
            }
        } catch (HttpClientErrorException e) {
            // Handle HTTP 4xx errors (client errors) here
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidAuthCredentialsException();
            } else {
                // Handle other client errors
                return null;
            }
        }
    }

    public String createRefreshToken(AuthRequest authRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(authRequest, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    "http://USER-SERVICE/v1/users/createRefreshToken",
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // Handle error here or throw an exception
                return null;
            }
        } catch (HttpClientErrorException e) {
            // Handle HTTP 4xx errors (client errors) here
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidAuthCredentialsException();
            } else {
                // Handle other client errors
                return null;
            }
        }
    }
}

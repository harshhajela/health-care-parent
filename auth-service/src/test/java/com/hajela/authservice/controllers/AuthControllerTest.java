package com.hajela.authservice.controllers;

import com.hajela.authservice.BaseIntegrationTest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.entities.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.basePath = "/v1/auth";
    }

    @Test
    @Order(1)
    void registerNewUserSuccessfully() {
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(createRegistrationDto("test1@test.com", Role.CUSTOMER))
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(201);
        // @formatter:on
    }

    @Test
    void registerUserWithSameEmailThrowsException() {
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(createRegistrationDto("test1@test.com", Role.CUSTOMER))
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("user.exists"))
                .body("message", equalTo("User exists with email: test1@test.com"));
        // @formatter:on
    }

    private RegistrationRequest createRegistrationDto(String email, Role role) {
        return RegistrationRequest.builder()
                .email(email)
                .password("123")
                .confirmPassword("123")
                .role(role)
                .build();
    }
}
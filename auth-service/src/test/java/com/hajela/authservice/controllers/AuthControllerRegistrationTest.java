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
class AuthControllerRegistrationTest extends BaseIntegrationTest {

    public static final String VALID_EMAIL = "registrationTest@test.com";
    public static final String VALID_PASSWORD = "Password@123";

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
                .body(createRegistrationDto(VALID_EMAIL))
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
                .body(createRegistrationDto(VALID_EMAIL))
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("user.exists"))
                .body("message", equalTo("User exists with email: registrationTest@test.com"));
        // @formatter:on
    }

    @Test
    void failedRegistrationWithoutEmail() {
        var request = RegistrationRequest.builder().role(Role.CUSTOMER)
                .password(VALID_PASSWORD).confirmPassword(VALID_PASSWORD).build();
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("invalid.request"))
                .body("message", equalTo("Please check errors for more details!"))
                .body("errors[0]", equalTo("email: Email cannot be blank"));
        // @formatter:on
    }

    @Test
    void failedRegistrationWithoutEmailPattern() {
        var request = createRegistrationDto("wrongemail");
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("invalid.request"))
                .body("message", equalTo("Please check errors for more details!"))
                .body("errors[0]", equalTo("email: Invalid email format"));
        // @formatter:on
    }

    @Test
    void failedRegistrationWithoutPasswordPattern() {
        var request = createRegistrationDto(VALID_EMAIL);
        request.setPassword("password123");
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("invalid.request"))
                .body("message", equalTo("Please check errors for more details!"))
                .body("errors[0]", equalTo("password: Invalid password format"));
        // @formatter:on
    }

    @Test
    void failedRegistrationWhenPasswordDoNotMatch() {
        var request = createRegistrationDto("test@test.com");
        request.setPassword("Pass@1234");
        // @formatter:off
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo("invalid.request"))
                .body("message", equalTo("Please check errors for more details!"))
                .body("errors[0]", equalTo("passwordsMatch: Password and Confirm Password must match"));
        // @formatter:on
    }

    private RegistrationRequest createRegistrationDto(String email) {
        return RegistrationRequest.builder()
                .email(email)
                .password(VALID_PASSWORD)
                .confirmPassword(VALID_PASSWORD)
                .role(Role.CUSTOMER)
                .build();
    }
}
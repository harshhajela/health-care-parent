package com.hajela.authservice.controllers;

import com.hajela.authservice.BaseIntegrationTest;
import com.hajela.authservice.entities.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class UserControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.basePath = "/v1/users";
    }

    @Test
    void registerNewUserSuccessfully() {
        // @formatter:off
        given().log().all()
                .pathParam("role", Role.CUSTOMER)
                .contentType(ContentType.JSON)
                //.body(createRegistrationDto("test1@test.com", Role.CUSTOMER))
                .when()
                .get("/{role}")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("numberOfElements", equalTo(0));
        // @formatter:on
    }

}
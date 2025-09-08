//package com.hajela.profileservice.controller;
//
//import com.hajela.profileservice.BaseIntegrationTest;
//import com.hajela.profileservice.JwtTestUtils;
//import com.hajela.profileservice.entity.SaveCustomerProfileDto;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.*;
//
//@Ignore
//class CustomerProfileControllerTest extends BaseIntegrationTest {
//
//    public static final String CUSTOMER_ROLE = "CUSTOMER";
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    @LocalServerPort
//    private int serverPort;
//
//    @Autowired
//    private JwtTestUtils jwtUtils;
//
//    @BeforeEach
//    void setUpRestAssured() {
//        RestAssured.port = serverPort;
//        RestAssured.basePath = "/v1/customer/profile";
//    }
//
//    @Ignore
//    @Test
//    void postCustomerProfileWorks() {
//        var testEmail = "customer1@profile.com";
//        var token = jwtUtils.generateToken(testEmail, CUSTOMER_ROLE);
//        // @formatter:off
//        given().log().all()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(createProfileDto())
//                .when()
//                .post()
//                .then().log().all()
//                .assertThat()
//                .statusCode(201)
//                .body("id", notNullValue())
//                .body("email", is(testEmail))
//                .body("firstName", is("firstName"))
//                .body("lastName", is("lastName"))
//                .body("phoneNumber", is("phoneNumber"))
//                .body("gender", is("gender"))
//                .body("dateOfBirth", equalTo(LocalDate.now().format(formatter)))
//                .body("address1", is("address1"))
//                .body("city", is("city"))
//                .body("state", is("state"))
//        ;
//        // @formatter:on
//    }
//
//    @Ignore
//    @Test
//    void getCustomerProfileWorks() {
//        var testEmail = "customer2@profile.com";
//        var token = jwtUtils.generateToken(testEmail, CUSTOMER_ROLE);
//        // @formatter:off
//        given().log().all()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(createProfileDto())
//                .when()
//                .post()
//                .then().log().all()
//                .assertThat()
//                .statusCode(201);
//        // @formatter:on
//
//        // @formatter:off
//        given().log().all()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .when()
//                .get()
//                .then().log().all()
//                .assertThat()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("email", is(testEmail));
//
//    }
//
//    private SaveCustomerProfileDto createProfileDto() {
//        return SaveCustomerProfileDto.builder()
//                .firstName("firstName")
//                .lastName("lastName")
//                .phoneNumber("phoneNumber")
//                .gender("gender")
//                .dateOfBirth(LocalDate.now())
//                .address1("address1")
//                .city("city")
//                .state("state")
//                .build();
//    }
//
//
//    @Test
//    void postCustomerProfileThrowsExceptionWhenIncorrectDtoIsUsed() {
//        var testEmail = "customer3@profile.com";
//        var token = jwtUtils.generateToken(testEmail, CUSTOMER_ROLE);
//        var dto = new SaveCustomerProfileDto();
//        // @formatter:off
//        given().log().all()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(dto)
//                .when()
//                .post()
//                .then().log().all()
//                .assertThat()
//                .statusCode(400)
//                .body("code", equalTo("invalid.request"))
//                .body("message", equalTo("Please check errors for more details!"))
//                .body("errors", hasItems(
//                        "city: City cannot be blank",
//                        "dateOfBirth: Date of birth cannot be null",
//                        "firstName: First name cannot be blank",
//                        "lastName: Last name cannot be blank",
//                        "phoneNumber: Phone number cannot be blank",
//                        "gender: Gender cannot be blank",
//                        "address1: Address 1 cannot be blank",
//                        "state: State cannot be blank"
//                ));
//        // @formatter:on
//    }
//}
package com.example.bookingservice;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import com.example.bookingservice.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

class BookingServiceApplicationTests extends BaseIntegrationTest {

    public static final String CUSTOMER_EMAIL = "customer@test.com";
    public static final String CUSTOMER_ROLE = "CUSTOMER";
    public static final String PROVIDER_EMAIL = "provider@test.com";
    public static final String PROVIDER_ROLE = "PROVIDER";
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @LocalServerPort
    private int serverPort;

    @Autowired
    private JwtTestUtils jwtUtils;

    private final RestAssuredConfig config = new RestAssuredConfig().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.basePath = "/v1/bookings";
    }

    @Test
    void contextLoads() {
        // Do nothing
    }

    @Test
    void testPostBookingsWorks() {
        var createBookingDto = getBookingDto();
        String jwtToken = jwtUtils.generateToken(CUSTOMER_EMAIL, "CUSTOMER");

        doStubbings();

        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(createBookingDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("bookingId", notNullValue());
        // @formatter:on
    }

    private void doStubbings() {
        stubFor(get(urlEqualTo("/v1/users/" + CUSTOMER_EMAIL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(toJson(getUserDto(CUSTOMER_EMAIL, CUSTOMER_ROLE)))
                ));

        stubFor(get(urlEqualTo("/v1/users/" + PROVIDER_EMAIL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(toJson(getUserDto(PROVIDER_EMAIL, PROVIDER_ROLE)))
                ));
    }

    private UserDto getUserDto(String email, String role) {
        return UserDto.builder()
                .id(100L)
                .email(email)
                .role(role)
                .created(LocalDateTime.now())
                .status("ACTIVATED")
                .build();
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    @Test
    void testBookingHistoryWorks() {
        var createBookingDto = getBookingDto();
        var token = jwtUtils.generateToken(CUSTOMER_EMAIL, "CUSTOMER");
        doStubbings();

        // @formatter:off
        BookingDto response = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(createBookingDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract().response().as(BookingDto.class);
        // @formatter:on

        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .pathParam("bookingId", response.getBookingId())
                .when()
                .get("/{bookingId}")
                .then().log().all()
                .assertThat()
                .statusCode(200);
        // @formatter:on

        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/history")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("[0].bookingId", notNullValue())
                .body("[0].customerEmail", equalTo(CUSTOMER_EMAIL))
                .body("[0].providerEmail", equalTo(PROVIDER_EMAIL))
                .body("[0].createdAt", notNullValue())
                .body("[0].bookingDate", notNullValue())
                .body("[0].bookingStatus", equalTo("BOOKING_CREATED"))
                .body("[0].servicesBooked", hasItems("Service1", "Service2"));
        // @formatter:on
    }

    private CreateBookingDto getBookingDto() {
        return CreateBookingDto.builder()
                .providerEmail("provider@test.com")
                .bookingDate(LocalDateTime.now())
                .servicesBooked(getServicesOffered())
                .build();
    }

    private List<String> getServicesOffered() {
        return List.of("Service1", "Service2");
    }

}

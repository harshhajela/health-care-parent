package com.example.bookingservice;

import com.example.bookingservice.dto.BookingDto;
import com.example.bookingservice.dto.CreateBookingDto;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.notNullValue;

@Rollback
@Transactional
class BookingServiceApplicationTests extends BaseIntegrationTest {

    @LocalServerPort
    private int serverPort;

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
    void testPostWorksOk() {
        var createBookingDto = getBookingDto();
        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .body(createBookingDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("bookingId", notNullValue());
        // @formatter:on
    }

    @Test
    void testBookingHistoryWorks() {
        var createBookingDto = getBookingDto();

        // @formatter:off
        BookingDto response = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
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
                .get("/history/{bookingId}")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("bookingId", notNullValue());
        // @formatter:on
    }

    private CreateBookingDto getBookingDto() {
        return CreateBookingDto.builder()
                .careProviderId(1)
                .customerId(1)
                .build();
    }

}

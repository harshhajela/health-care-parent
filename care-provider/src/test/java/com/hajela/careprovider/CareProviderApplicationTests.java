package com.hajela.careprovider;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@Rollback
@Transactional
class CareProviderApplicationTests extends BaseIntegrationTest {

	@LocalServerPort
	private int serverPort;

	private final RestAssuredConfig config = new RestAssuredConfig().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

	@BeforeEach
	void setUpRestAssured() {
		RestAssured.port = serverPort;
		RestAssured.basePath = "/v1/care-providers";
	}

//	@Test
//	void contextLoads() {
//	}

	/*@Test
	void createCareProvider() {
		// @formatter:off
		given().log().all()
				.config(config)
				.contentType(ContentType.JSON)
				.body(createDto())
				.when()
				.post()
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.body("careProviderId", notNullValue());
		// @formatter:on
	}*/

//	private CreateCareProvider createDto() {
//		return CreateCareProvider.builder()
//				.email("harsh.hajela@gmail.com")
//				.firstName("Harsh")
//				.lastName("Hajela")
//				.userName("harsh.hajela")
//				.build();
//	}
}

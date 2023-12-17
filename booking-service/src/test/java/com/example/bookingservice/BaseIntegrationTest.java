package com.example.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@Slf4j
@Testcontainers
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BookingServiceApplication.class, JwtTestUtils.class})
public abstract class BaseIntegrationTest {

    private static WireMockServer wireMockServer;
    @Container
    private static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);
//            .withDatabaseName("booking_service");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        log.info("Setting postgres properties");
        startContainerIfNotRunning(); // Ensure container is started
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("auth-service.url", () -> "http://localhost:8000");
    }

    private static void startContainerIfNotRunning() {
        if (!mongoContainer.isRunning()) {
            log.info("Starting MONGODB container");
            mongoContainer.start();
        }
    }

    @BeforeAll
    public static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        WireMockConfiguration wireMockConfiguration = WireMockConfiguration
                .wireMockConfig()
                .port(8000);
//                .extensions(new JacksonMappingExtension(objectMapper));
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8000));
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        log.info("Wiremock server started");
    }

    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

    @AfterAll
    public static void tearDown() {
        log.info("Stopping MongoDB container and Wiremock server");
        mongoContainer.stop();
        wireMockServer.stop();
    }

}

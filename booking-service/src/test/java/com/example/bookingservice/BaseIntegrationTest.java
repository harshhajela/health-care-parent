package com.example.bookingservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@Rollback
@Transactional
@ActiveProfiles({"test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BookingServiceApplication.class})
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("booking_service")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        log.info("Setting postgres properties");
        startContainerIfNotRunning(); // Ensure container is started
        dymDynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dymDynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        dymDynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    private static void startContainerIfNotRunning() {
        if (!postgresContainer.isRunning()) {
            log.info("Starting postgres container");
            postgresContainer.start();
        }
    }

    protected int getMappedPort() {
        startContainerIfNotRunning(); // Ensure container is started
        return postgresContainer.getFirstMappedPort();
    }


    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

    @AfterAll
    public static void tearDown() {
        log.info("Stopping postgres container");
        postgresContainer.stop();
    }

}

package com.hajela.authservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {AuthServiceApplication.class})
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        log.info("Setting postgres properties");
        startContainerIfNotRunning(); // Ensure container is started
        dymDynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dymDynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        dymDynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
        // Disable properties for integration tests
        dymDynamicPropertyRegistry.add("eureka.client.enabled", () -> "false");
        dymDynamicPropertyRegistry.add("email.enabled", () -> "false");
    }

    private static void startContainerIfNotRunning() {
        if (!postgresContainer.isRunning()) {
            log.info("Starting postgres container");
            postgresContainer.start();
        }
    }

    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

}

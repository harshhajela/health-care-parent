package com.hajela.authservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {AuthServiceApplication.class})
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("user_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static final KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        startContainerIfNotRunning();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        // Disable properties for integration tests
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("email.enabled", () -> "false");
        //kafka
        registry.add("kafka.host", kafkaContainer::getBootstrapServers);
    }

    private static void startContainerIfNotRunning() {
        if (!postgresContainer.isRunning()) {
            log.info("Starting postgres container");
            postgresContainer.start();
        }
        if (!kafkaContainer.isRunning()) {
            log.info("Starting kafka container");
            kafkaContainer.start();
        }
    }

    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

}

package com.hajela.profileservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
@Rollback
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProfileServiceApplication.class})
public abstract class BaseIntegrationTest {

    @Container
    private static final MongoDBContainer mongoContainer =
            new MongoDBContainer("mongo:latest")
                    .withExposedPorts(27017);


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        log.info("Setting postgres properties");
        startContainerIfNotRunning(); // Ensure container is started
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
        registry.add("eureka.client.enabled", () -> "false");
    }

    private static void startContainerIfNotRunning() {
        if (!mongoContainer.isRunning()) {
            log.info("Starting MONGODB container");
            mongoContainer.start();
        }
    }

    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

    @AfterAll
    public static void tearDown() {
        log.info("Stopping MongoDB container and Wiremock server");
        mongoContainer.stop();
    }

}

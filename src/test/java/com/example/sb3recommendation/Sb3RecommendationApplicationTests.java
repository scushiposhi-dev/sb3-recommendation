package com.example.sb3recommendation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT,properties = {"eureka.client.enabled=false"})
class Sb3RecommendationApplicationTests extends MonogoDbTestBase {

    @Test
    void contextLoads() {
        assertNotNull(MonogoDbTestBase.class);
    }

}

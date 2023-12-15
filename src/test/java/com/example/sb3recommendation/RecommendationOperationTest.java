package com.example.sb3recommendation;

import com.example.sb3recommendation.exceptions.InvalidInputException;
import com.example.sb3recommendation.message.Event;
import com.example.sb3recommendation.model.Recommendation;
import com.example.sb3recommendation.repository.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT,properties = {"spring.main.allow-bean-definition-overriding=true",
        "eureka.client.enabled=false"})
class RecommendationOperationTest extends MonogoDbTestBase {
    @Autowired
    WebTestClient client;

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    @Qualifier("messageProcessor")
    Consumer<Event<Integer,Recommendation>> messageProcessor;


    @BeforeEach
    void setUp(){
        recommendationRepository.deleteAll().block();
    }

    @Test
    void getRecommendationsByProductId(){

        int productId=1;
        int recommendationId1=1;
        int recommendationId2=2;
        int recommendationId3=3;

        sendCreateRecommendationEvent(productId,recommendationId1);
        sendCreateRecommendationEvent(productId,recommendationId2);
        sendCreateRecommendationEvent(productId,recommendationId3);

        WebTestClient.BodyContentSpec recommendationsByProductId = getAndVerifyRecommendationsByProductId("?productId=+"+productId, OK);

        recommendationsByProductId.jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[2].productId").isEqualTo(productId)
                .jsonPath("$[2].recommendationId").isEqualTo(3);

    }

    @Test
    void deleteRecommendations(){
        int prodId=1;
        int recommId=1;

        sendCreateRecommendationEvent(prodId,recommId);
        assertNotNull(recommendationRepository.findByProductId(prodId).blockFirst());

        sendDeleteRecommendationEvent(prodId);
        assertNull(recommendationRepository.findByProductId(prodId).blockFirst());
    }

    @Test
    void duplicateError() {

        int productId = 1;
        int recommendationId = 1;

        sendCreateRecommendationEvent(productId, recommendationId);
        assertEquals(1, (long)recommendationRepository.count().block());

        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> sendCreateRecommendationEvent(productId, recommendationId),
                "Expected a InvalidInputException here!");

        assertEquals("Duplicate prod id:1,rec id:1", thrown.getMessage());

        assertEquals(1, (long)recommendationRepository.count().block());
    }

    @Test
    void getRecommendationsNotFound() {
        getAndVerifyRecommendationsByProductId("?productId=3333", OK)
                .jsonPath("$.length()").isEqualTo(0);
    }

    private void sendCreateRecommendationEvent(int productId,int recommendationId){
        Recommendation recommendation = Recommendation.builder()
                .productId(productId)
                .recommendationId(recommendationId)
                .author("me")
                .content("content for recommendation Id" + recommendationId)
                .build();

        Event<Integer,Recommendation> event = Event.<Integer, Recommendation>builder()
                .eventType(Event.Type.CREATE)
                .key(productId)
                .data(recommendation)
                .build();

        messageProcessor.accept(event);
    }
    private void sendDeleteRecommendationEvent(int prodId){
        Event<Integer,Recommendation> event = Event.<Integer, Recommendation>builder().eventType(Event.Type.DELETE).key(prodId).build();
        messageProcessor.accept(event);
    }

    private WebTestClient.BodyContentSpec getAndVerifyRecommendationsByProductId(String productIdQuery, HttpStatus expectedStatus) {
        return client.get()
                .uri("/v1/recommendation" + productIdQuery)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

}

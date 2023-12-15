package com.example.sb3recommendation.config;

import com.example.sb3recommendation.exceptions.EventProcessingException;
import com.example.sb3recommendation.message.Event;
import com.example.sb3recommendation.model.Recommendation;
import com.example.sb3recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MessageProcessorConfig {

    private final RecommendationService recommendationService;

    @Bean
    public Consumer<Event<Integer, Recommendation>> messageProcessor() {

        return event -> {

            log.info("Process message created at:{}", event.getEventCreatedAt());

            switch (event.getEventType()) {
                case CREATE:
                    Recommendation recommendation = event.getData();
                    log.info("Create recommendation with id:{}/{}", recommendation.getProductId(), recommendation.getRecommendationId());
                    recommendationService.createRecommendation(recommendation).block();
                    break;
                case DELETE:
                    log.info("Delete recommendations by productId:{}", event.getKey());
                    recommendationService.deleteRecommendationsByProductId(event.getKey()).block();
                    break;
                default:
                    log.warn("Event not handled:{}", event.getEventType());
                    throw new EventProcessingException("Event requested"+event.getEventType()+",event handled: CREATE/DELETE");
            }
            log.info("message processed");
        };
    }
}

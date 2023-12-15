package com.example.sb3recommendation.restcontroller;

import com.example.sb3recommendation.model.Recommendation;
import com.example.sb3recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class RecommendationRestController {
    private final RecommendationService recommendationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,path = "/v1/recommendation")
    public Flux<Recommendation> getRecommendationsByProductId(@RequestParam(name = "productId") int productId){
        return recommendationService.getRecommendationsByProductId(productId);
    }
}


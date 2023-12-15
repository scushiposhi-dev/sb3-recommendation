package com.example.sb3recommendation.repository;

import com.example.sb3recommendation.entity.RecommendationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity,String> {
    Flux<RecommendationEntity> findByProductId(int productId);
    Mono<Void> deleteAllByProductId(int productId);
}

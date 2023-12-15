package com.example.sb3recommendation;

import com.example.sb3recommendation.entity.RecommendationEntity;
import com.example.sb3recommendation.repository.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class RecommendationRepositoryTest extends MonogoDbTestBase {

    @Autowired
    RecommendationRepository recommendationRepository;
    private RecommendationEntity toSave;
    private RecommendationEntity saved;

    @BeforeEach
    void setUp(){
        StepVerifier.create(recommendationRepository.deleteAll()).verifyComplete();

        toSave = getEntity();

        StepVerifier.create(recommendationRepository.save(toSave))
                .consumeNextWith(p->saved=p)
                .verifyComplete();
    }

    @Test
    void createOk(){
        StepVerifier.create(recommendationRepository.save(RecommendationEntity.builder().build()))
                .assertNext(e->assertNotNull(e))
                .verifyComplete();
    }

    @Test
    void duplicateError(){
        StepVerifier.create(recommendationRepository.save(toSave))
                .expectError(DuplicateKeyException.class);
    }

    @Test
    void deleteOk(){
        StepVerifier.create(recommendationRepository.delete(saved)).verifyComplete();

        StepVerifier.create(recommendationRepository.existsById(saved.getId()))
                .expectNext(false).verifyComplete();
    }

    @Test
    void getRecommendationsByProdId(){
        StepVerifier.create(recommendationRepository.findByProductId(saved.getProductId()).collectList())
                .assertNext(list->{
                    assertNotNull(list);
                    assertEquals(1,list.size());
                }).verifyComplete();
    }


    @Test
    void optimisticLockError(){
        RecommendationEntity saved1 = recommendationRepository.findById(saved.getId()).block();
        RecommendationEntity saved1copy = recommendationRepository.findById(saved.getId()).block();

        saved1.setAuthor("change it");
        recommendationRepository.save(saved1).block();

        StepVerifier.create(recommendationRepository.save(saved1copy))
                .expectError(OptimisticLockingFailureException.class)
                .verify();

        StepVerifier.create(recommendationRepository.findById(saved1.getId()))
                .expectNextMatches(found->found.getVersion()==1)
                .verifyComplete();
    }

    boolean assertEqualsRecommendation(RecommendationEntity toSave,RecommendationEntity saved){
        return toSave.getRecommendationId()== saved.getRecommendationId()
                &&
                toSave.getProductId()==saved.getProductId()
                &&
                toSave.getAuthor().equals(saved.getAuthor())
                &&
                toSave.getContent().equals(saved.getContent());
    }
    RecommendationEntity getEntity(){
        return RecommendationEntity.builder()
                .author("you")
                .content("I don't know")
                .rate(5)
                .productId(1)
                .recommendationId(1)
                .build();
    }
}

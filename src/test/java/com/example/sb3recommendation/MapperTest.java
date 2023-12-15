package com.example.sb3recommendation;

import com.example.sb3recommendation.entity.RecommendationEntity;
import com.example.sb3recommendation.mapper.RecommendationMapper;
import com.example.sb3recommendation.model.Recommendation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    RecommendationMapper recommendationMapper=Mappers.getMapper(RecommendationMapper.class);

    @Test
    void mapperTests(){
        assertNotNull(recommendationMapper);

        RecommendationEntity recommendationEntity = RecommendationEntity.builder()
                .author("me")
                .content("go somewhere else")
                .productId(1)
                .rate(3)
                .recommendationId(1).build();

        Recommendation recommendation = recommendationMapper.toDto(recommendationEntity);

        assertEquals(recommendation.getAuthor(),recommendationEntity.getAuthor());
        assertEquals(recommendation.getContent(),recommendationEntity.getContent());
        assertEquals(recommendation.getRate(),recommendationEntity.getRate());
        assertEquals(recommendation.getRecommendationId(),recommendationEntity.getRecommendationId());

        assertNull(recommendation.getServiceAddress());
    }
}

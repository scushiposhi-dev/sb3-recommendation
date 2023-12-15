package com.example.sb3recommendation.mapper;

import com.example.sb3recommendation.entity.RecommendationEntity;
import com.example.sb3recommendation.model.Recommendation;
import com.example.sb3recommendation.util.ServiceUtil;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class,uses = ServiceUtil.class)
public interface RecommendationMapper {
    RecommendationEntity toEntity(Recommendation recommendation);
    Recommendation toDto(RecommendationEntity recommendationEntity);
}

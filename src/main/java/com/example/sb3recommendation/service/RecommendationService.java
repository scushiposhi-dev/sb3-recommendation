package com.example.sb3recommendation.service;

import com.example.sb3recommendation.exceptions.InvalidInputException;
import com.example.sb3recommendation.mapper.RecommendationMapper;
import com.example.sb3recommendation.model.Recommendation;
import com.example.sb3recommendation.repository.RecommendationRepository;
import com.example.sb3recommendation.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final ServiceUtil serviceUtil;

    @Transactional(readOnly = true)
    public Flux<Recommendation> getRecommendationsByProductId(int productId){
        return recommendationRepository.findByProductId(productId)
                .log(log.getName(),Level.FINE)
                .map(p->recommendationMapper.toDto(p))
                .map(this::setServiceAddress);
    }
//    @Transactional
    public Mono<Recommendation> createRecommendation(Recommendation recommendation){
        return recommendationRepository.save(recommendationMapper.toEntity(recommendation))
                .log("we are creating", Level.FINE)
                .onErrorMap(DuplicateKeyException.class,dup->new InvalidInputException("Duplicate prod id:"+recommendation.getProductId()+",rec id:"+recommendation.getRecommendationId()))
                .map(recommendationMapper::toDto)
                .map(this::setServiceAddress);
    }
    public Mono<Void> deleteRecommendationsByProductId(int productId){
        return recommendationRepository.deleteAllByProductId(productId);
    }

    private Recommendation setServiceAddress(Recommendation recommendation){
        recommendation.setServiceAddress(serviceUtil.getServiceAddress());
        return recommendation;
    }
}

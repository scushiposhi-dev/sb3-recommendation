package com.example.sb3recommendation.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recommendation {

  private Integer productId;

  private Integer recommendationId;

  private Integer rate;

  private String content;

  private String author;

  private String serviceAddress;
}
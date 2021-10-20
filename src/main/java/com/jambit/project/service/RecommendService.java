package com.jambit.project.service;

import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.RecommendDto;

public interface RecommendService {
    Long create(RecommendDto recommendDto);
    Boolean delete(Long recommendId);
    Integer countRecommendByType(Long refId, TargetType targetType);
    Boolean getRecommendByTypeAndUser(Long refId, TargetType targetType, String nickname);
}

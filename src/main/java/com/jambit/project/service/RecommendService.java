package com.jambit.project.service;

import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.RecommendDto;

import java.util.List;

public interface RecommendService {
    Long create(RecommendDto recommendDto);
    Boolean delete(Long recommendId);
    Integer countRecommendByType(Long refId, TargetType targetType);
    Boolean getRecommendByTypeAndUser(Long refId, TargetType targetType, String nickname);
    //List<RecommendDto> findRecommendListByUserId(TargetType targetType, String nickname);
    Long findIdByTypeAndUserAndRef(TargetType targetType, String nickname, Long refId);
}

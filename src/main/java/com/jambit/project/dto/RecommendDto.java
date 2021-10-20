package com.jambit.project.dto;

import com.jambit.project.domain.entity.Recommend;
import com.jambit.project.domain.entity.TargetType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendDto {
    private Long id;
    private Long refId;
    private TargetType targetType;
    private String nickname;
    private Boolean isDeleted;

    public static Recommend toEntity(RecommendDto recommendDto) {
        return Recommend.builder()
                .id(recommendDto.getId())
                .refId(recommendDto.getRefId())
                .targetType(recommendDto.getTargetType())
                .nickname(recommendDto.getNickname())
                .isDeleted(recommendDto.getIsDeleted())
                .build();
    }
}

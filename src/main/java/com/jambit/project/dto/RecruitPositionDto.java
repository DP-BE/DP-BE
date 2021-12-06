package com.jambit.project.dto;

import com.jambit.project.domain.entity.RecruitPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitPositionDto {
    private Long id;
    private String position;
    private Long count;
    private Long postId;

    public static RecruitPosition toEntity(RecruitPositionDto recruitPositionDto) {
        return RecruitPosition.builder()
                .id(recruitPositionDto.getId())
                .position(recruitPositionDto.getPosition())
                .count(recruitPositionDto.getCount())
                .postId(recruitPositionDto.getPostId())
                .build();
    }
}

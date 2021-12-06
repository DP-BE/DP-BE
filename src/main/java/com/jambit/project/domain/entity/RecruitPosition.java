package com.jambit.project.domain.entity;

import com.jambit.project.dto.RecruitPositionDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecruitPosition extends  BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column
    private String position;

    @Column
    private Long count;

    @Column(nullable = false)
    private Long postId;

    public static RecruitPositionDto toDto(RecruitPosition recruitPosition) {
        return RecruitPositionDto.builder()
                .id(recruitPosition.getId())
                .position(recruitPosition.getPosition())
                .count(recruitPosition.getCount())
                .postId(recruitPosition.getPostId())
                .build();
    }
}

package com.jambit.project.domain.entity;

import com.jambit.project.dto.RecommendDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommend extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    @Column(nullable = false)
    private Long refId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Column
    private String nickname;

    @Column
    private Boolean isDeleted;

    public static RecommendDto toDto(Recommend recommend) {
        return RecommendDto.builder()
                .id(recommend.getId())
                .refId(recommend.getRefId())
                .targetType(recommend.getTargetType())
                .nickname(recommend.getNickname())
                .isDeleted(recommend.getIsDeleted())
                .build();
    }
}

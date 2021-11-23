package com.jambit.project.domain.entity;

import com.jambit.project.dto.FollowDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Follow extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @Column(nullable = false)
    private String followee;

    @Column(nullable = false)
    private String nickname;

    public static FollowDto toDto(Follow follow) {
        return FollowDto.builder()
                .id(follow.getId())
                .followee(follow.getFollowee())
                .nickname(follow.getNickname())
                .build();
    }
}

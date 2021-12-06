package com.jambit.project.dto;

import com.jambit.project.domain.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
    private Long id;
    private String followee;
    private String nickname;

    private String profileImage;

    public static Follow toEntity(FollowDto followDto){
        return Follow.builder()
                .id(followDto.getId())
                .nickname(followDto.getNickname())
                .followee(followDto.getFollowee())
                .build();
    }
}

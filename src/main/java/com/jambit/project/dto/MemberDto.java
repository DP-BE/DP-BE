package com.jambit.project.dto;

import com.jambit.project.domain.entity.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberDto {
    private Long id;
    private String userId;
    private String nickname;
    private String description;
    private String skillSet;
    private Long projectCnt;
    private String profileImage;

    private List<String> skillList;

    public static Member toEntity(MemberDto memberDto){
        return Member.builder()
                .id(memberDto.getId())
                .userId(memberDto.getUserId())
                .nickname(memberDto.getNickname())
                .description(memberDto.getDescription())
                .skillSet(memberDto.getSkillSet())
                .projectCnt(memberDto.getProjectCnt())
                .profileImage(memberDto.getProfileImage())
                .build();
    }

}

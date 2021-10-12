package com.jambit.project.dto;

import com.jambit.project.domain.entity.Project;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ProjectDto {
    private Long id;
    private String participatedNickname;
    private String projectName;
    private String content;
    private String link;
    private Long viewCount;
    private Long replyCount;
    private Long likes;
    private String techStack;
    public static Project toEntity(ProjectDto projectDto){
        return Project.builder()
                .id(projectDto.getId())
                .participatedNickname(projectDto.getParticipatedNickname())
                .projectName(projectDto.getProjectName())
                .content(projectDto.getContent())
                .link(projectDto.getLink())
                .viewCount(projectDto.getViewCount())
                .replyCount(projectDto.getReplyCount())
                .likes(projectDto.getLikes())
                .techStack(projectDto.getTechStack())
                .build();
    }

}

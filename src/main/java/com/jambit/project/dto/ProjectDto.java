package com.jambit.project.dto;

import com.jambit.project.domain.entity.ProgressType;
import com.jambit.project.domain.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String participatedNickname;
    private String projectManager;
    private String projectName;
    private String content;
    private String githubLink;
    private String projectLink;
    private Long viewCount;
    private Long replyCount;
    private String techStack;
    private Long likesCount;
    private ProgressType progress;

    private List<String> imgList;

    public static Project toEntity(ProjectDto projectDto){
        return Project.builder()
                .id(projectDto.getId())
                .participatedNickname(projectDto.getParticipatedNickname())
                .projectName(projectDto.getProjectName())
                .content(projectDto.getContent())
                .githubLink(projectDto.getGithubLink())
                .projectLink(projectDto.getProjectLink())
                .viewCount(projectDto.getViewCount())
                .replyCount(projectDto.getReplyCount())
                .techStack(projectDto.getTechStack())
                .likesCount(projectDto.getLikesCount())
                .progress(projectDto.getProgress())
                .projectManager(projectDto.getProjectManager())
                .build();
    }

}

package com.jambit.project.domain.entity;


import com.jambit.project.dto.ProjectDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Project extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String participatedNickname;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String content;

    @Column
    private String link;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long replyCount;

    @Column
    private String techStack;

    public static ProjectDto toDto(Project project){
        return ProjectDto.builder()
                .id(project.getId())
                .participatedNickname(project.getParticipatedNickname())
                .projectName(project.getProjectName())
                .content(project.getContent())
                .link(project.getLink())
                .viewCount(project.getViewCount())
                .replyCount(project.getReplyCount())
                .techStack(project.getTechStack())
                .build();
    }

}

package com.jambit.project.service;

import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.SkillSetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    ProjectDto getProject(Long project_id);

    Long createProject(String projectDto, MultipartFile[] files) throws Exception;

    Long modifyProject(ProjectDto projectDto);

    Long deleteProject(Long project_id);

    List<MemberDto> findParticipatedMember(Long projectId);

    List<ProjectDto> findLikedProjectList(String nickname);

    // 좋아요 순으로 프로젝트 리스트 5개
    List<ProjectDto> findTopProjects();

    List<SkillSetDto> findProjectSkillSet(Long projectId);

    void updateViewCount(Long projectId);
}

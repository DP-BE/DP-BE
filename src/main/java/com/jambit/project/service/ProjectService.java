package com.jambit.project.service;

import com.jambit.project.dto.ProjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    ProjectDto getProject(Long project_id);

    List<ProjectDto> findAllProjects();

    Long createProject(String projectDto, MultipartFile[] files) throws Exception;

    Long modifyProject(ProjectDto projectDto);

    Long deleteProject(Long project_id);

    // 좋아요 순으로 프로젝트 리스트 5개
    List<ProjectDto> findTopProjects();

    // 프로젝트 링크 출력
    List<String> findLinkListByProjectId(Long projectId);

    // 사람 출력
    List<String> findNicknameListByProjectId(Long projectId);

}

package com.jambit.project.service;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.Project;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.ImageRepository;
import com.jambit.project.domain.repository.ProjectRepository;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final FileHandler fileHandler;
    private final ImageRepository imageRepository;
    @Transactional
    public ProjectDto getProject(Long project_id){
        Optional<Project> findProjectWrapper = projectRepository.findById(project_id);
        if(findProjectWrapper.isPresent()){
            Project findProject = findProjectWrapper.get();
            return Project.toDto(findProject);
        }
        return null;
    }

    @Transactional
    public Long createProject(ProjectDto projectDto, List<MultipartFile> files) throws Exception {
        if(projectDto != null) {
            projectDto.setLikesCount(0L);
            projectDto.setReplyCount(0L);
            projectDto.setViewCount(0L);
            Project project = ProjectDto.toEntity(projectDto);
            projectRepository.save(project);

            List<Image> imageList = fileHandler.parseFileInfo(project.getId(), TargetType.PROJECT, files);

            if(!imageList.isEmpty()){
                for(Image image : imageList){
                    imageRepository.save(image);
                }
            }
            return project.getId();
        }
        return null;
    }

    @Transactional
    public Long modifyProject(ProjectDto projectDto){
        if(projectRepository.findById(projectDto.getId()).isPresent()){
            Project project = updateProject(projectDto);
            projectRepository.save(project);
            return project.getId();
        }
        return null;
    }

    private Project updateProject(ProjectDto projectDto){
        Project project = new Project();
        project.setId(projectDto.getId());
        project.setProjectName(projectDto.getProjectName());
        project.setContent(projectDto.getContent());
        project.setLink(projectDto.getLink());
        project.setParticipatedNickname(projectDto.getParticipatedNickname());
        project.setTechStack(projectDto.getTechStack());
        project.setLikesCount(projectDto.getLikesCount());
        project.setReplyCount(projectDto.getReplyCount());
        project.setViewCount(projectDto.getViewCount());
        return project;
    }

    @Transactional
    public Long deleteProject(Long project_id){
        Optional<Project> targetProject = projectRepository.findById(project_id);
        if(targetProject.isPresent()){
            projectRepository.deleteById(project_id);
            return project_id;
        }
        return null;
    }
/*
    @Transactional // paging
    public List<ProjectDto> findProjectListByUserNickname(String nickname){
        List<Project> findProjectList = projectRepository.findAllProjectListByNicknameLike(nickname);

        if(findProjectList != null) {
            return findProjectList.stream()
                    .map(Project::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }
*/
    @Transactional
    public List<String> findLinkListByProjectId(Long projectId){
        Optional<Project> targetProject = projectRepository.findById(projectId);
        if(targetProject.isPresent()) {
            String links = targetProject.get().getLink();
            String[] linkList = links.split(",");
            return Arrays.asList(linkList);
        }
        return null;
    }

    @Transactional
    public List<String> findNicknameListByProjectId(Long projectId){
        Optional<Project> targetProject = projectRepository.findById(projectId);
        if(targetProject.isPresent()) {
            String users = targetProject.get().getParticipatedNickname();
            String[] userList = users.split(",");
            return Arrays.asList(userList);
        }
        return null;
    }

    @Transactional
    public List<ProjectDto> findTopProjects(){
        List<Project> findProjectList = projectRepository.findTop5ByOrderByLikesCountDesc();
        if(findProjectList != null){
            return findProjectList.stream()
                    .map(Project::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }
}

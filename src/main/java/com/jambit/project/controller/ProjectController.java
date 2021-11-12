package com.jambit.project.controller;


import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.ProjectRepository;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.service.ImageService;
import com.jambit.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Slf4j

public class ProjectController {
    private final ProjectService projectService;
    private final ImageService imageService;

    @GetMapping("/{project_id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("project_id")Long project_id){
        ProjectDto findProject = projectService.getProject(project_id);
        if(findProject != null){
            return new ResponseEntity<>(findProject,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Long> registerProject( @RequestPart(value = "image", required = false) MultipartFile[] files,
                                                 @RequestPart(value = "projectDto") String projectDto) throws Exception{
        Long projectId = projectService.createProject(projectDto, files);
        if(projectId != null){
            return new ResponseEntity<>(projectId, HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PutMapping("")
    public ResponseEntity<Long> reviseProject(@RequestBody ProjectDto projectDto){
        if(projectService.modifyProject(projectDto) != null){
            return new ResponseEntity<>(projectDto.getId(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable("project_id")Long projectId){
        if(projectService.deleteProject(projectId) != null){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }
/*
    @GetMapping("/{user_nickname}")
    public ResponseEntity<List<ProjectDto>> getUserProject(@PathVariable("user_nickname")String userNickname){
        List<ProjectDto> userProjects = projectService.findProjectListByUserNickname(userNickname);
        return new ResponseEntity<>(userProjects, HttpStatus.OK);
    }*/

    @GetMapping("/top") // parameter 뭘로 할지?
    public ResponseEntity<List<ProjectDto>> getTopProject(){
        List<ProjectDto> topProjects = projectService.findTopProjects();
        return new ResponseEntity<>(topProjects,HttpStatus.OK);
    }

    @GetMapping("/link/{project_id}")
    public ResponseEntity<List<String>> getLink(@PathVariable("project_id") Long projectId){
        List<String> links = projectService.findLinkListByProjectId(projectId);
        return new ResponseEntity<>(links,HttpStatus.OK);
    }

    @GetMapping("/nickname/{project_id}")
    public ResponseEntity<List<String>> getNickname(@PathVariable("project_id") Long projectId){
        List<String> nicknames = projectService.findNicknameListByProjectId(projectId);
        return new ResponseEntity<>(nicknames,HttpStatus.OK);
    }

}

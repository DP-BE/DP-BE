package com.jambit.project.controller;


import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.ProjectRepository;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.SkillSetDto;
import com.jambit.project.service.ImageService;
import com.jambit.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    // 프로젝트 등록
    @PostMapping("")
    public ResponseEntity<Long> registerProject( @RequestPart(value = "image", required = false) MultipartFile[] files,
                                                 @RequestPart(value = "projectDto") String projectDto) throws Exception{
        Long projectId = projectService.createProject(projectDto, files);
        if(projectId != null){
            return new ResponseEntity<>(projectId, HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    // 프로젝트 수정
    @PutMapping("")
    public ResponseEntity<Long> reviseProject(@RequestBody ProjectDto projectDto){
        if(projectService.modifyProject(projectDto) != null){
            return new ResponseEntity<>(projectDto.getId(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{project_id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable("project_id")Long projectId){
        if(projectService.deleteProject(projectId) != null){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/top")
    public ResponseEntity<List<ProjectDto>> getTopProject(){
        List<ProjectDto> topProjects = projectService.findTopProjects();
        return new ResponseEntity<>(topProjects,HttpStatus.OK);
    }

    // 프로젝트에 참여하는 멤버를 불러옴
    @GetMapping("/member/{project_id}")
    public ResponseEntity<List<MemberDto>> getParticipateMember(@PathVariable("project_id") Long projectId) {
        List<MemberDto> participatedMember = projectService.findParticipatedMember(projectId);
        return new ResponseEntity<>(participatedMember, HttpStatus.OK);
    }

    // 프로젝트 디테일 페이지에서 기술스택을 불러옴
    @GetMapping("/skill/{project_id}")
    public ResponseEntity<List<SkillSetDto>> getSkillSetForProject(@PathVariable("project_id") Long projectId) {
        List<SkillSetDto> projectSkillSet = projectService.findProjectSkillSet(projectId);
        return new ResponseEntity<>(projectSkillSet, HttpStatus.OK);
    }

    @GetMapping("/favorite/{nickname}")
    public ResponseEntity<List<ProjectDto>> getFavoriteProject(@PathVariable("nickname") String nickname) {
        List<ProjectDto> likedProjectList = projectService.findLikedProjectList(nickname);
        return new ResponseEntity<>(likedProjectList, HttpStatus.OK);
    }

    @GetMapping("/view/{project_id}")
    public ResponseEntity<Void> incViewCount(@PathVariable("project_id") Long projectId) {
        projectService.updateViewCount(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProjectDto>> getProjectByTitle(@RequestParam("title") String title){
        List<ProjectDto> projectDtoList = projectService.findProjectByTitle(title);
        return new ResponseEntity<>(projectDtoList, HttpStatus.OK);
    }

}

package com.jambit.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.*;
import com.jambit.project.dto.*;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final FileHandler fileHandler;
    private final ImageRepository imageRepository;
    private final ProjectParticipateRepository participateRepository;
    private final MemberRepository memberRepository;
    private final SkillResolveRepository skillResolveRepository;
    private final SkillSetRepository skillSetRepository;
    private final RecommendRepository recommendRepository;

    @Transactional
    public ProjectDto getProject(Long project_id) {
        Optional<Project> findProjectWrapper = projectRepository.findById(project_id);
        if(findProjectWrapper.isPresent()){
            Project findProject = findProjectWrapper.get();
            findProject.setViewCount(findProject.getViewCount() + 1L);
            ProjectDto projectDto = Project.toDto(findProject);
            List<String> fileNameList = imageRepository.findAllImageListByTargetIdAndTargetType(projectDto.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
            List<String> imageList = new ArrayList<>();
            if (!fileNameList.isEmpty()) {
                fileNameList.forEach(file -> {
                    imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + file);
                });
            }
            else {
                imageList.add("/static/media/defaultImg.85ba799a.png");
            }
            projectDto.setImgList(imageList);
            return projectDto;
        }
        return null;
    }

    @Transactional
    public List<ProjectDto> findLikedProjectList(String nickname) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        List<Long> idList = recommendRepository.findByTargetTypeAndNickname(TargetType.PROJECT, nickname).stream().map(Recommend::getRefId).collect(Collectors.toList());
        idList.forEach(id -> {
            Optional<Project> byId = projectRepository.findById(id);
            byId.ifPresent(project -> {
                ProjectDto projectDto = Project.toDto(project);
                List<String> fileNameList = imageRepository.findAllImageListByTargetIdAndTargetType(project.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
                List<String> imageList = new ArrayList<>();
                if (!fileNameList.isEmpty()) {
                    fileNameList.forEach(fileName -> {
                        imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + fileName);
                    });
                }
                else {
                    imageList.add("/static/media/defaultImg.85ba799a.png");
                }
                projectDto.setImgList(imageList);
                projectDtoList.add(projectDto);
            });
        });
        return projectDtoList;
    }

    @Transactional
    public List<MemberDto> findParticipatedMember(Long projectId) {
        List<MemberDto> memberList = new ArrayList<>();
        List<ProjectParticipate> participateList = participateRepository.findByProjectIdAndIsDeletedFalse(projectId);
        List<Long> memberIdList = participateList.stream().map(ProjectParticipate::getMemberId).collect(Collectors.toList());
        memberIdList.forEach(memberId -> {
            Optional<Member> findMember = memberRepository.findById(memberId);
            findMember.ifPresent(member -> {
                MemberDto memberDto = Member.toDto(member);
                memberList.add(memberDto);
            });
        });
        return memberList;
    }

    @Transactional
    public List<SkillSetDto> findProjectSkillSet(Long projectId) {
        List<SkillSetDto> skillSetList = new ArrayList<>();
        List<SkillResolve> byProjectId = skillResolveRepository.findByProjectIdAndIsDeletedFalse(projectId);
        List<Long> skillIdList = byProjectId.stream().map(SkillResolve::getSkillId).collect(Collectors.toList());
        skillIdList.forEach(skillId -> {
            Optional<SkillSet> findSkill = skillSetRepository.findById(skillId);
            findSkill.ifPresent(skill -> {
                SkillSetDto skillSetDto = SkillSet.toDto(skill);
                skillSetList.add(skillSetDto);
            });
        });
        return skillSetList;
    }

    @Transactional
    public Long createProject(String projectDto, MultipartFile[] files) throws Exception {
        if(projectDto != null) {
            ProjectDto projectDto1 = new ObjectMapper().readValue(projectDto, ProjectDto.class);
            projectDto1.setLikesCount(0L);
            projectDto1.setReplyCount(0L);
            projectDto1.setViewCount(0L);
            Project project = ProjectDto.toEntity(projectDto1);
            projectRepository.save(project);
            String participatedIds = projectDto1.getParticipatedNickname();
            StringTokenizer stringTokenizer = new StringTokenizer(participatedIds, "#");
            while (stringTokenizer.hasMoreTokens()) {
                String memberId = stringTokenizer.nextToken();
                ProjectParticipate participate = ProjectParticipate.builder()
                        .projectId(project.getId())
                        .memberId(Long.valueOf(memberId))
                        .isDeleted(false)
                        .build();
                participateRepository.save(participate);
                memberRepository.incProjectCount(Long.valueOf(memberId));
            }

            String techStack = projectDto1.getTechStack();
            stringTokenizer = new StringTokenizer(techStack, "#");
            while (stringTokenizer.hasMoreTokens()) {
                String skillId = stringTokenizer.nextToken();
                SkillResolve skillResolve = SkillResolve.builder()
                        .projectId(project.getId())
                        .skillId(Long.valueOf(skillId))
                        .isDeleted(false)
                        .build();
                skillResolveRepository.save(skillResolve);
            }

            if(files != null) {
                List<ImageDto> imageList = fileHandler.parseFileInfo(project.getId(), TargetType.PROJECT, files);

                if (!imageList.isEmpty()) {
                    for (ImageDto imageDto : imageList) {
                        Image image = ImageDto.toEntity(imageDto);
                        imageRepository.save(image);
                    }
                }
            }
            return project.getId();
        }
        return null;
    }

    @Transactional
    public Long modifyProject(ProjectDto projectDto){
        Optional<Project> findProject = projectRepository.findById(projectDto.getId());
        if(findProject.isPresent()) {
            List<SkillResolve> byProjectId = skillResolveRepository.findByProjectId(projectDto.getId());
            byProjectId.forEach(skill -> {
                if (!skill.getIsDeleted()) {
                    skill.setIsDeleted(true);
                }
            });
            List<ProjectParticipate> byProjectId1 = participateRepository.findByProjectId(projectDto.getId());
            byProjectId1.forEach(member -> {
                if (!member.getIsDeleted()) {
                    member.setIsDeleted(true);
                }
            });
            Project project = findProject.get();
            project.update(projectDto);

            StringTokenizer stringTokenizer = new StringTokenizer(projectDto.getTechStack(), "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                skillResolveRepository.save(SkillResolve.builder()
                        .skillId(skillId)
                        .projectId(projectDto.getId())
                        .isDeleted(false)
                        .build());
            }

            stringTokenizer = new StringTokenizer(projectDto.getParticipatedNickname(), "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long memberId = Long.valueOf(stringTokenizer.nextToken());
                participateRepository.save(ProjectParticipate.builder()
                        .projectId(projectDto.getId())
                        .memberId(memberId)
                        .isDeleted(false)
                        .build());
            }
            return project.getId();
        }
        return null;
    }

    @Transactional
    public Long deleteProject(Long project_id){
        Optional<Project> targetProject = projectRepository.findById(project_id);
        if(targetProject.isPresent()){
            List<ProjectParticipate> participateList = participateRepository.findByProjectId(project_id);
            List<Image> imageList = imageRepository.findAllImageListByTargetIdAndTargetType(project_id, TargetType.PROJECT);
            participateList.forEach(participate -> {
                memberRepository.decProjectCount(participate.getMemberId());
                participateRepository.delete(participate);
            });
            imageRepository.deleteAll(imageList);
            projectRepository.deleteById(project_id);
            return project_id;
        }
        return null;
    }

    @Transactional
    public Page<ProjectDto> findAllProjects(Pageable pageable) {
        Page<Project> findProjectList = projectRepository.findAllByOrderByLikesCountDesc(pageable);
        Page<ProjectDto> projectDtoList = findProjectList.map(Project::toDto);
        projectDtoList.forEach(p -> {
            List<String> imageList = new ArrayList<>();
            List<String> fileNameList = imageRepository.findAllImageListByTargetIdAndTargetType(p.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
            if (!fileNameList.isEmpty()) {
                fileNameList.forEach(file -> {
                    imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + file);
                });
            }
            else {
                imageList.add("/static/media/defaultImg.85ba799a.png");
            }
            p.setImgList(imageList);
        });
        return projectDtoList;
    }

    @Transactional
    public List<ProjectDto> findProjectByFilter(String type, String payload) {
        switch(type) {
            case "TITLE":
                List<ProjectDto> findProjectList = projectRepository.findByProjectNameContainingIgnoreCase(payload).stream().map(Project::toDto).collect(Collectors.toList());
                findProjectList.forEach(p -> {
                    List<String> imageList = new ArrayList<>();
                    List<String> fileNameList = imageRepository.findAllImageListByTargetIdAndTargetType(p.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
                    if (!fileNameList.isEmpty()) {
                        fileNameList.forEach(file -> {
                            imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + file);
                        });
                    }
                    else {
                        imageList.add("/static/media/defaultImg.85ba799a.png");
                    }
                    p.setImgList(imageList);
                });
                return findProjectList;
            case "STACK":
                List<Long> skillIdList = skillSetRepository.findBySkillNameContainingIgnoreCase(payload).stream().map(SkillSet::getId).collect(Collectors.toList());
                List<Long> projectIdList = skillResolveRepository.findBySkillIdInAndIsDeletedFalseAndMemberIdIsNullAndPostIdIsNull(skillIdList).stream().map(SkillResolve::getProjectId).collect(Collectors.toList());
                Set<Long> projectSet = new HashSet<>(projectIdList);
                List<Long> uniqueProjectIdList = new ArrayList<>(projectSet);
                List<ProjectDto> returnList = new ArrayList<>();
                uniqueProjectIdList.forEach(projectId -> {
                    Optional<Project> findProject = projectRepository.findById(projectId);
                    findProject.ifPresent(p -> {
                        ProjectDto projectDto = Project.toDto(p);
                        List<String> imageList = new ArrayList<>();
                        List<String> fileNameList = imageRepository.findAllImageListByTargetIdAndTargetType(p.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
                        if (!fileNameList.isEmpty()) {
                            fileNameList.forEach(file -> {
                                imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + file);
                            });
                        }
                        else {
                            imageList.add("/static/media/defaultImg.85ba799a.png");
                        }
                        projectDto.setImgList(imageList);
                        returnList.add(projectDto);
                    });
                });
                return returnList;
        }
        return new ArrayList<>();
    }

    @Transactional
    public void updateViewCount(Long projectId) {
        Optional<Project> findProject = projectRepository.findById(projectId);
        findProject.ifPresent(p -> {
            projectRepository.incProjectViewCount(p.getId());
        });
    }
}

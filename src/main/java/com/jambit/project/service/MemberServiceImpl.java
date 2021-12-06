package com.jambit.project.service;

import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.*;
import com.jambit.project.dto.*;
import com.jambit.project.security.JwtTokenProvider;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ProjectParticipateRepository participateRepository;
    private final ProjectRepository projectRepository;
    private final ReplyRepository replyRepository;
    private final RecommendRepository recommendRepository;
    private final SkillResolveRepository skillResolveRepository;
    private final SkillSetRepository skillSetRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;
    private final FileHandler fileHandler;
    private final BoardRepository boardRepository;

    @Transactional
    public MemberDto findMember(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        MemberDto memberDto = findMember.map(Member::toDto).orElse(null);
        if (memberDto != null) {
            List<String> newSkillList = new ArrayList<>();
            StringTokenizer stringTokenizer = new StringTokenizer(memberDto.getSkillSet(), "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                Optional<SkillSet> findSkill = skillSetRepository.findById(skillId);
                findSkill.ifPresent(f -> newSkillList.add(f.getSkillName()));
            }
            memberDto.setSkillList(newSkillList);
        }
        return memberDto;
    }

    @Transactional
    public MemberDto findMemberByUserId(String userId) {
        Optional<Member> byUserId = memberRepository.findByUserId(userId);
        return byUserId.map(Member::toDto).orElse(null);
    }

    @Transactional
    public Boolean checkDuplicateNickname(String nickname) {
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);
        return byNickname.isPresent();
    }

    @Transactional
    public Page<MemberDto> getRecommendMember(Pageable pageable) {
        Page<Member> recommendedMember = memberRepository.findAllByOrderByProjectCntDesc(pageable);
        Page<MemberDto> memberDto = recommendedMember.map(Member::toDto);
        return memberDto.map(m -> {
            String techStack = m.getSkillSet();
            StringTokenizer stringTokenizer = new StringTokenizer(techStack, "#");
            List<String> newSkillList = new ArrayList<>();
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                Optional<SkillSet> findSkill = skillSetRepository.findById(skillId);
                findSkill.ifPresent(f -> newSkillList.add(f.getSkillName()));
            }
            m.setSkillList(newSkillList);
            return m;
        });
    }

    @Transactional
    public List<MemberDto> searchMemberList(String nickname) {
        List<Member> findMemberList = memberRepository.findByNicknameContaining(nickname);
        return findMemberList.stream().map(Member::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<ProjectDto> getMyProjectList(Long memberId) {
        List<ProjectDto> projectList = new ArrayList<>();
        List<ProjectParticipate> byMemberId = participateRepository.findByMemberId(memberId);
        List<Long> findProjectIdList = byMemberId.stream().map(ProjectParticipate::getProjectId).collect(Collectors.toList());
        findProjectIdList.forEach(projectId -> {
            Optional<Project> findProject = projectRepository.findById(projectId);
            findProject.ifPresent(p -> {
                ProjectDto projectDto = Project.toDto(p);
                List<String> imageList = new ArrayList<>();
                List<String> fileName = imageRepository.findAllImageListByTargetIdAndTargetType(projectDto.getId(), TargetType.PROJECT).stream().map(Image::getFileName).collect(Collectors.toList());
                if (!fileName.isEmpty()) {
                    fileName.forEach(file -> {
                        imageList.add("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + file);
                    });
                }
                else {
                    imageList.add("/static/media/defaultImg.85ba799a.png");
                }
                projectDto.setImgList(imageList);
                projectList.add(projectDto);
            });
        });
        return projectList;
    }

    @Transactional
    public List<SkillSetDto> getMemberSkillSet(Long memberId) {
        List<SkillSetDto> skillList = new ArrayList<>();
        List<SkillResolve> byMemberId = skillResolveRepository.findByMemberIdAndIsDeletedFalse(memberId);
        List<Long> skillIdList = byMemberId.stream().map(SkillResolve::getSkillId).collect(Collectors.toList());
        skillIdList.forEach(skillId -> {
            Optional<SkillSet> findSkill = skillSetRepository.findById(skillId);
            findSkill.ifPresent(skill -> {
                SkillSetDto skillSetDto = SkillSet.toDto(skill);
                skillList.add(skillSetDto);
            });
        });
        return skillList;
    }

    @Transactional
    public String generateToken(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        if (findMember.isPresent()) {
            return jwtTokenProvider.createToken(nickname);
        }
        return null;
    }

    @Transactional
    public Long createMember(MemberDto memberDto) {
        if (memberDto != null) {
            memberDto.setProjectCnt(0L);
            Member member = MemberDto.toEntity(memberDto);
            memberRepository.save(member);
            return member.getId();
        }

        return null;
    }

    @Transactional
    public Boolean registerSkill(RegisterSkillDto skillDto) {
        Optional<Member> findMember = memberRepository.findById(skillDto.getMemberId());
        List<SkillResolve> byMemberId = skillResolveRepository.findByMemberId(skillDto.getMemberId());
        byMemberId.forEach(s -> {
            if (!s.getIsDeleted()) {
                s.setIsDeleted(true);
            }
        });

        if (findMember.isPresent()) {
            StringTokenizer stringTokenizer = new StringTokenizer(skillDto.getSkill(), "#");
            while (stringTokenizer.hasMoreTokens()) {
                String skillId = stringTokenizer.nextToken();
                skillResolveRepository.save(SkillResolve.builder()
                        .skillId(Long.valueOf(skillId))
                        .memberId(skillDto.getMemberId())
                        .isDeleted(false)
                        .build());
            }
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean registerImage(Long memberId, MultipartFile[] files) throws Exception{
        List<Image> originalList = imageRepository.findAllImageListByTargetIdAndTargetType(memberId, TargetType.USER);
        imageRepository.deleteAll(originalList);
        List<ImageDto> imageList = fileHandler.parseFileInfo(memberId, TargetType.USER, files);
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (!imageList.isEmpty()) {
            if (findMember.isPresent()) {
                for (ImageDto imageDto : imageList) {
                    Image image = ImageDto.toEntity(imageDto);
                    Member member = findMember.get();
                    member.setProfileImage("http://15.165.194.66:8080/image/get-image-with-media-type?fileName=" + image.getFileName());
                    imageRepository.save(image);
                }
                return true;
            }
        }
        return false;
    }

    @Transactional
    public MemberDto modify(MemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findById(memberDto.getId());
        if (findMember.isPresent()) {
            Member member = findMember.get();
            String exNickname = member.getNickname();
            if (!exNickname.equals(memberDto.getNickname())) {
                String newNickname = memberDto.getNickname();
                List<Project> byProjectManager = projectRepository.findByProjectManager(exNickname);
                List<Reply> byReplyWriter = replyRepository.findAllReplyListByNickname(exNickname);
                List<Follow> followingList = followRepository.findByNickname(exNickname);
                List<Follow> followerList = followRepository.findByFollowee(exNickname);
                List<Recommend> recommendList = recommendRepository.findByNickname(exNickname);
                List<Board> boardList = boardRepository.findByNickname(exNickname);
                byProjectManager.forEach(project -> project.setProjectManager(newNickname));
                byReplyWriter.forEach(reply -> reply.setNickname(newNickname));
                followingList.forEach(follow -> follow.setNickname(newNickname));
                followerList.forEach(follow -> follow.setFollowee(newNickname));
                recommendList.forEach(recommend -> recommend.setNickname(newNickname));
                boardList.forEach(board -> board.setNickname(newNickname));
                //TODO: 게시판 바꾸면 닉네임 또 바꿔주기
            }
            member.update(memberDto);
        }
        return findMember.map(Member::toDto).orElse(null);
    }

    @Transactional
    public void delete(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        findMember.ifPresent(memberRepository::delete);
    }
}

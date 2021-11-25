package com.jambit.project.service;

import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.*;
import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.SkillSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SkillResolveRepository skillResolveRepository;
    private final SkillSetRepository skillSetRepository;

    @Transactional
    public MemberDto findMember(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        return findMember.map(Member::toDto).orElse(null);
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
                projectList.add(projectDto);
            });
        });
        return projectList;
    }

    @Transactional
    public List<SkillSetDto> getMemberSkillSet(Long memberId) {
        List<SkillSetDto> skillList = new ArrayList<>();
        List<SkillResolve> byMemberId = skillResolveRepository.findByMemberId(memberId);
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
    public Boolean registerSkill(Long memberId, String skillSet) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isPresent()) {
            StringTokenizer stringTokenizer = new StringTokenizer(skillSet, "#");
            while (stringTokenizer.hasMoreTokens()) {
                String skillId = stringTokenizer.nextToken();
                skillResolveRepository.save(SkillResolve.builder().skillId(Long.valueOf(skillId)).memberId(memberId).build());
            }
            return true;
        }
        return false;
    }

    @Transactional
    public MemberDto modify(MemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findById(memberDto.getId());
        findMember.ifPresent(m -> m.update(memberDto));
        return findMember.map(Member::toDto).orElse(null);
    }

    @Transactional
    public void delete(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        findMember.ifPresent(memberRepository::delete);
    }
}

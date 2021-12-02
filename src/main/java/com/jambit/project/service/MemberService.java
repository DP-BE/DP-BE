package com.jambit.project.service;

import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.RegisterSkillDto;
import com.jambit.project.dto.SkillSetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberDto modify(MemberDto memberDto);
    MemberDto findMember(String nickname);
    void delete(Long memberId);
    List<MemberDto> searchMemberList(String nickname);
    List<ProjectDto> getMyProjectList(Long memberId);
    List<SkillSetDto> getMemberSkillSet(Long memberId);
    Boolean registerSkill(RegisterSkillDto registerSkillDto);
    Long createMember(MemberDto memberDto);
    String generateToken(String nickname);
    MemberDto findMemberByUserId(String userId);
    Page<MemberDto> getRecommendMember(Pageable pageable);
}

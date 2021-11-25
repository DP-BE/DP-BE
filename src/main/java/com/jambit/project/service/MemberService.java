package com.jambit.project.service;

import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.SkillSetDto;

import java.util.List;

public interface MemberService {
    MemberDto modify(MemberDto memberDto);
    MemberDto findMember(String nickname);
    void delete(Long memberId);
    List<MemberDto> searchMemberList(String nickname);
    List<ProjectDto> getMyProjectList(Long memberId);
    List<SkillSetDto> getMemberSkillSet(Long memberId);
    Boolean registerSkill(Long memberId, String skillSet);
}

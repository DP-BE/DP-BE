package com.jambit.project.controller;

import com.jambit.project.dto.MemberDto;
import com.jambit.project.dto.ProjectDto;
import com.jambit.project.dto.SkillSetDto;
import com.jambit.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{nickname}")
    public ResponseEntity<MemberDto> getMemberWithNickname(@PathVariable("nickname") String nickname) {
        MemberDto findMember = memberService.findMember(nickname);
        if (findMember != null) {
            return new ResponseEntity<>(findMember, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project/{member_id}")
    public ResponseEntity<List<ProjectDto>> getMyProject(@PathVariable("member_id") Long memberId) {
        List<ProjectDto> myProjectList = memberService.getMyProjectList(memberId);
        return new ResponseEntity<>(myProjectList, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> searchMemberList(@RequestParam("nickname") String nickname) {
        return new ResponseEntity<>(memberService.searchMemberList(nickname), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<MemberDto> modify(@RequestBody MemberDto memberDto) {
        MemberDto modifiedMember = memberService.modify(memberDto);
        if (modifiedMember != null) {
            return new ResponseEntity<>(modifiedMember, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/skill/me")
    public ResponseEntity<List<SkillSetDto>> getMySkillSet(@RequestParam("member_id") Long memberId) {
        List<SkillSetDto> memberSkillSet = memberService.getMemberSkillSet(memberId);
        return new ResponseEntity<>(memberSkillSet, HttpStatus.OK);
    }

    @PostMapping("/skill")
    public ResponseEntity<Boolean> registerSkill(@RequestParam("member_id") Long memberId, @RequestParam("skill") String skill) {
        Boolean isSuccess = memberService.registerSkill(memberId, skill);
        return new ResponseEntity<>(isSuccess, HttpStatus.OK);
    }

    @DeleteMapping("/{member_id}")
    public ResponseEntity<Void> delete(@PathVariable("member_id") Long memberId) {
        memberService.delete(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

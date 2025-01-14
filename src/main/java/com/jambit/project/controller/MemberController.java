package com.jambit.project.controller;

import com.jambit.project.dto.*;
import com.jambit.project.service.MemberService;
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

    @GetMapping("/recommend")
    public ResponseEntity<Page<MemberDto>> getMemberWithRecommend(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberDto> recommendMember = memberService.getRecommendMember(pageable);
        return new ResponseEntity<>(recommendMember, HttpStatus.OK);
    }

    @GetMapping("/check/{user_id}")
    public ResponseEntity<MemberDto> getMemberWithUserId(@PathVariable("user_id") String userId) {
        MemberDto findMember = memberService.findMemberByUserId(userId);
        return new ResponseEntity<>(findMember, HttpStatus.OK);
    }

    @GetMapping("/duplicate/{nickname}")
    public ResponseEntity<Boolean> isDuplicateNickname(@PathVariable("nickname") String nickname) {
        Boolean isDuplicated = memberService.checkDuplicateNickname(nickname);
        return new ResponseEntity<>(isDuplicated, HttpStatus.OK);
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

    // 내 기술스택을 불러옴
    @GetMapping("/skill/me")
    public ResponseEntity<List<SkillSetDto>> getMySkillSet(@RequestParam("member_id") Long memberId) {
        List<SkillSetDto> memberSkillSet = memberService.getMemberSkillSet(memberId);
        return new ResponseEntity<>(memberSkillSet, HttpStatus.OK);
    }

    // access-token 발급
    @GetMapping("/access-token")
    public ResponseEntity<String> getAccessToken(@RequestParam("nickname") String nickname) {
        String accessToken = memberService.generateToken(nickname);
        if (accessToken != null) {
            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null ,HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<MemberDto>> searchMemberByFilter(@RequestBody SearchDto searchDto) {
        List<MemberDto> memberList = memberService.searchMemberWithType(searchDto.getType(), searchDto.getPayload());
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> registerMember(@RequestBody MemberDto memberDto) {
        Long registerId = memberService.createMember(memberDto);
        if (registerId != null) {
            return new ResponseEntity<>(registerId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // 기술스택 등록할 때 skill에 해당 스킬 id들이 #으로 붙어서 등록되어야 함.
    @PostMapping("/skill")
    public ResponseEntity<Boolean> registerSkill(@RequestBody RegisterSkillDto skillDto) {
        Boolean isSuccess = memberService.registerSkill(skillDto);
        return new ResponseEntity<>(isSuccess, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<Boolean> registerImage(@RequestParam(value = "memberId") Long memberId,
                                                 @RequestParam(value = "image") MultipartFile[] files) throws Exception {
        Boolean isSuccess = memberService.registerImage(memberId, files);
        return new ResponseEntity<>(isSuccess, HttpStatus.OK);
    }

    @DeleteMapping("/{member_id}")
    public ResponseEntity<Void> delete(@PathVariable("member_id") Long memberId) {
        memberService.delete(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

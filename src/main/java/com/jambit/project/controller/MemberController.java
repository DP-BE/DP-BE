package com.jambit.project.controller;

import com.jambit.project.dto.MemberDto;
import com.jambit.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{nickname}")
    public ResponseEntity<MemberDto> getMember(@PathVariable("nickname") String nickname) {
        return null;
    }

    // 추가정보 받을 때
    @PutMapping("")
    public ResponseEntity<Long> modify(@RequestBody MemberDto memberDto) {
        return null;
    }

    @DeleteMapping("/{member_id}")
    public ResponseEntity<Boolean> delete(@PathVariable("member_id") Long memberId) {
        return null;
    }

}

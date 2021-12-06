package com.jambit.project.controller;

import com.jambit.project.dto.RecruitPositionDto;
import com.jambit.project.service.RecruitPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/position")
@Slf4j
public class RecruitPositionController {
    private final RecruitPositionService recruitPositionService;

    @GetMapping("/list")
    public ResponseEntity<List<RecruitPositionDto>> getPositionList(@RequestParam("postId") Long postId) {
        List<RecruitPositionDto> positionList = recruitPositionService.getPositionList(postId);
        return new ResponseEntity<>(positionList, HttpStatus.OK);
    }

}

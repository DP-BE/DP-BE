package com.jambit.project.controller;

import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.RecommendDto;
import com.jambit.project.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/count/post/{post_id}")
    public ResponseEntity<Integer> countPostRecommend(@PathVariable("post_id") Long postId) {
        Integer totalRecommend = recommendService.countRecommendByType(postId, TargetType.POST);
        if (totalRecommend != null) {
            return new ResponseEntity<>(totalRecommend, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping("/count/project/{project_id}")
    public ResponseEntity<Integer> countProjectRecommend(@PathVariable("project_id") Long projectId) {
        Integer totalRecommend = recommendService.countRecommendByType(projectId, TargetType.PROJECT);
        if (totalRecommend != null) {
            return new ResponseEntity<>(totalRecommend, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping("/count/reply/{reply_id}")
    public ResponseEntity<Integer> countReplyRecommend(@PathVariable("reply_id") Long replyId) {
        Integer totalRecommend = recommendService.countRecommendByType(replyId, TargetType.REPLY);
        if (totalRecommend != null) {
            return new ResponseEntity<>(totalRecommend, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping("/post/{post_id}/{nickname}")
    public ResponseEntity<Boolean> getPostRecommend(@PathVariable("post_id") Long postId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(postId, TargetType.POST, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    @GetMapping("/project/{project_id}/{nickname}")
    public ResponseEntity<Boolean> getProjectRecommend(@PathVariable("project_id") Long projectId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(projectId, TargetType.PROJECT, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    @GetMapping("/reply/{reply_id}/{nickname}")
    public ResponseEntity<Boolean> getReplyRecommend(@PathVariable("reply_id") Long replyId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(replyId, TargetType.REPLY, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> registerRecommend(@RequestBody RecommendDto recommendDto) {
        Long recommendId = recommendService.create(recommendDto);
        if (recommendId != null) {
            return new ResponseEntity<>(recommendId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{recommend_id}")
    public ResponseEntity<Boolean> deleteRecommend(@PathVariable("recommend_id") Long recommendId) {
        Boolean isDeleted = recommendService.delete(recommendId);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

}

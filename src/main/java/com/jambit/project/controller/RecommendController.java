package com.jambit.project.controller;

import com.jambit.project.domain.entity.Recommend;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.FollowDto;
import com.jambit.project.dto.RecommendDto;
import com.jambit.project.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {
    private final RecommendService recommendService;

    // 구인글 좋아요 수
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

    // 프로젝트의 좋아요 수
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

    // 댓글의 좋아요
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

    // 구인글에 내가 좋아요 여부
    @GetMapping("/post/{post_id}/{nickname}")
    public ResponseEntity<Boolean> getPostRecommend(@PathVariable("post_id") Long postId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(postId, TargetType.POST, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    // 프로젝트에 내가 좋아요 여부
    @GetMapping("/project/{project_id}/{nickname}")
    public ResponseEntity<Boolean> getProjectRecommend(@PathVariable("project_id") Long projectId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(projectId, TargetType.PROJECT, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    // 댓글에 좋아요 여부
    @GetMapping("/reply/{reply_id}/{nickname}")
    public ResponseEntity<Boolean> getReplyRecommend(@PathVariable("reply_id") Long replyId, @PathVariable("nickname") String nickname) {
        Boolean isRecommend = recommendService.getRecommendByTypeAndUser(replyId, TargetType.REPLY, nickname);
        return new ResponseEntity<>(isRecommend, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<RecommendDto>> getRecommendList(@RequestParam TargetType targetType,
                                                               @RequestParam String nickname){
        List<RecommendDto> recommendList = recommendService.findRecommendListByUserId(targetType, nickname);
        return new ResponseEntity<>(recommendList, HttpStatus.OK);
    }

    // 좋아요 등록
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

    // 좋아요 취소
    @DeleteMapping("/{recommend_id}")
    public ResponseEntity<Boolean> deleteRecommend(@PathVariable("recommend_id") Long recommendId) {
        Boolean isDeleted = recommendService.delete(recommendId);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

}

package com.jambit.project.controller;

import com.jambit.project.dto.FollowDto;
import com.jambit.project.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {
    private final FollowService followService;

    @GetMapping("/total/follower/{nickname}")
    public ResponseEntity<Integer> countFollower(@PathVariable String nickname) {
        return new ResponseEntity<>(followService.countFollowerListByUserId(nickname), HttpStatus.OK);
    }

    @GetMapping("/total/following/{nickname}")
    public ResponseEntity<Integer> countFollowing(@PathVariable String nickname) {
        return new ResponseEntity<>(followService.countFollowingListByUserId(nickname), HttpStatus.OK);
    }

    @GetMapping("/follower/{nickname}")
    public ResponseEntity<List<FollowDto>> getFollowerList(@PathVariable String nickname) {
        List<FollowDto> followerList = followService.findFollowerListByUserId(nickname);
        return new ResponseEntity<>(followerList, HttpStatus.OK);
    }

    @GetMapping("/following/{nickname}")
    public ResponseEntity<List<FollowDto>> getFollowingList(@PathVariable String nickname){
        List<FollowDto> followingList = followService.findFollowingListByUserId(nickname);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody FollowDto followDto) {
        Long registerId = followService.create(followDto);
        if (registerId != null) {
            return new ResponseEntity<>(registerId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{follow_id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable("follow_id") Long followId) {
        followService.delete(followId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

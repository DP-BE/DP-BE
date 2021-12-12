package com.jambit.project.controller;

import com.jambit.project.dto.ReplyDto;
import com.jambit.project.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
@Slf4j
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/total/post/{post_id}")
    public ResponseEntity<Integer> countAllPostReplies(@PathVariable("post_id") Long postId) {
        List<ReplyDto> allPostReplies = replyService.findAllPostRepliesList(postId);
        return new ResponseEntity<>(allPostReplies.size(), HttpStatus.OK);
    }

    @GetMapping("/total/project/{project_id}")
    public ResponseEntity<Integer> countAllProjectReplies(@PathVariable("project_id") Long projectId) {
        List<ReplyDto> allProjectRepliesList = replyService.findAllProjectRepliesList(projectId);
        return new ResponseEntity<>(allProjectRepliesList.size(), HttpStatus.OK);
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<List<ReplyDto>> getAllPostReplies(@PathVariable("post_id") Long postId) {
        List<ReplyDto> findAllReplies = replyService.findAllPostRepliesList(postId);
        return new ResponseEntity<>(findAllReplies, HttpStatus.OK);
    }

    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<ReplyDto>> getAllProjectReplies(@PathVariable("project_id") Long projectId) {
        List<ReplyDto> allProjectRepliesList = replyService.findAllProjectRepliesList(projectId);
        return new ResponseEntity<>(allProjectRepliesList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> registerReply(@RequestBody ReplyDto replyDto) {
        if(replyService.create(replyDto) != null) {
            return new ResponseEntity<>(replyDto.getNickname(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("")
    public ResponseEntity<String> modifyReply(@RequestBody ReplyDto replyDto) {
        if (replyService.modify(replyDto) != null) {
            return new ResponseEntity<>(replyDto.getNickname(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{reply_id}")
    public ResponseEntity<Boolean> deleteReply(@PathVariable("reply_id") Long reply_id) {
        boolean isDeleted = replyService.deleteReply(reply_id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }
}

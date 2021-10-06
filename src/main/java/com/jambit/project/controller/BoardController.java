package com.jambit.project.controller;

import com.jambit.project.dto.BoardDto;
import com.jambit.project.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{post_id}")
    public ResponseEntity<BoardDto> getPost(@PathVariable("post_id") Long post_id) {
        BoardDto findPost = boardService.getPost(post_id);
        if (findPost != null) {
            return new ResponseEntity<>(findPost, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

package com.jambit.project.controller;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<String> registerBoard(@RequestBody BoardDto boardDto){
        if(boardService.create(boardDto)!=null){
            return new ResponseEntity<>(boardDto.getTitle(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("")
    public ResponseEntity<String> modifyBoard(@RequestBody BoardDto boardDto){
        if(boardService.modify(boardDto)!=null){
            return new ResponseEntity<>(boardDto.getTitle(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("post_id") Long post_id){
        boardService.deleteById(post_id);
        return ResponseEntity.ok().build();
    }

}

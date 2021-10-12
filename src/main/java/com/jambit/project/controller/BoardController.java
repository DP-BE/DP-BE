package com.jambit.project.controller;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{post_id}")
    public ResponseEntity<BoardDto> getPost(@PathVariable("post_id") Long post_id) {
        BoardDto findPost = boardService.findPost(post_id);
        if (findPost != null) {
            return new ResponseEntity<>(findPost, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Long> registerBoard(@RequestBody BoardDto boardDto){
        if(boardService.createPost(boardDto)!=null){
            return new ResponseEntity<>(boardDto.getId(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> modifyBoard(@RequestBody BoardDto boardDto){
        if(boardService.modifyPost(boardDto)!=null){
            return new ResponseEntity<>(boardDto.getId(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("post_id") Long post_id){
        boolean isDeleted = boardService.deletePost(post_id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    //모든 게시글
    @GetMapping("/list")
    public ResponseEntity<List<BoardDto>> getAllPosts(){
        List<BoardDto> allPosts = boardService.findAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    //닉네임으로 찾기
    @GetMapping("/{user_nickname}")
    public ResponseEntity<List<BoardDto>> getUserPost(@PathVariable("user_nickname")String userNickname){
        List<BoardDto> userPosts = boardService.findPostListByUserNickname(userNickname);
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }

    //제목으로 찾기
    @GetMapping("/{title}")
    public ResponseEntity<List<BoardDto>> getPostByTitle(@PathVariable("title")String title){
        List<BoardDto> findPostList = boardService.findPostByTitle(title);
        return new ResponseEntity<>(findPostList, HttpStatus.OK);
    }
}

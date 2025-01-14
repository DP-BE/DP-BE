package com.jambit.project.controller;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.service.BoardService;
import com.jambit.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
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

    @GetMapping("/me/{nickname}")
    public ResponseEntity<List<BoardDto>> getMyPostList(@PathVariable("nickname") String nickname) {
        List<BoardDto> postList = boardService.findPostList(nickname);
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/favorite/{nickname}")
    public ResponseEntity<List<BoardDto>> getFavoritePostList(@PathVariable("nickname") String nickname) {
        List<BoardDto> likedPostList = boardService.findLikedPostList(nickname);
        return new ResponseEntity<>(likedPostList, HttpStatus.OK);
    }

    @GetMapping("/skill/{post_id}")
    public ResponseEntity<List<String>> getPostSkill(@PathVariable("post_id") Long postId) {
        List<String> postSkillSet = boardService.findPostSkillSet(postId);
        return new ResponseEntity<>(postSkillSet, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> registerBoard(@RequestPart(value = "boardDto") String boardDto) throws Exception{
        Long postId = boardService.createPost(boardDto);
        if(postId != null){
            return new ResponseEntity<>(postId, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("")
    public ResponseEntity<Long> modifyBoard( @RequestBody BoardDto boardDto){
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
    public ResponseEntity<Page<BoardDto>> getAllPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardDto> allPosts = boardService.findAllPosts(pageable);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<BoardDto>> filterPost(@RequestParam("type") String type,
                                                     @RequestParam("payload") String payload,
                                                     @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                             Pageable pageable) {
        Page<BoardDto> filteredPost = boardService.findFilteredPost(type, payload, pageable);
        if (filteredPost != null) {
            return new ResponseEntity<>(filteredPost, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

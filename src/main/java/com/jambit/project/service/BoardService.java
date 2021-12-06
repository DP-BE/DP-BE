package com.jambit.project.service;

import com.jambit.project.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardDto findPost(Long post_id);

    List<BoardDto> findPostList(String nickname);

    List<BoardDto> findLikedPostList(String nickname);

    Long createPost(String boardDto) throws Exception;

    Long modifyPost(BoardDto boardDto);

    boolean deletePost(Long post_id);

    List<String> findPostSkillSet(Long postId);

    Page<BoardDto> findAllPosts(Pageable pageable);

    Page<BoardDto> findFilteredPost(String type, String payload, Pageable pageable);
}

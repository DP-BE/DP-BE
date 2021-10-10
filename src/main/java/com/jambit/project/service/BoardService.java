package com.jambit.project.service;

import com.jambit.project.dto.BoardDto;

public interface BoardService {
    BoardDto getPost(Long post_id);
    String create(BoardDto boardDto);
    String modify(BoardDto boardDto);
    void deleteById(Long post_id);
}

package com.jambit.project.service;

import com.jambit.project.dto.BoardDto;

public interface BoardService {
    BoardDto getPost(Long post_id);
}

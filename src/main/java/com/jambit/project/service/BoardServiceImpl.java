package com.jambit.project.service;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.repository.BoardRepository;
import com.jambit.project.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BoardDto getPost(Long post_id) {
        Optional<Board> findPostWrapper = boardRepository.findById(post_id);
        if (findPostWrapper.isPresent()) {
            Board findPost = findPostWrapper.get();
            return Board.toDto(findPost);
        }
        return null;
    }


}

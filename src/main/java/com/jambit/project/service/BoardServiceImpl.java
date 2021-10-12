package com.jambit.project.service;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.Reply;
import com.jambit.project.domain.repository.BoardRepository;
import com.jambit.project.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BoardDto findPost(Long post_id) {
        Optional<Board> findPostWrapper = boardRepository.findById(post_id);
        if (findPostWrapper.isPresent()) {
            Board findPost = findPostWrapper.get();
            return Board.toDto(findPost);
        }
        return null;
    }

    @Transactional
    public Long createPost(BoardDto boardDto){
        if (boardDto!=null) {
            boardDto.setLikes(0L);
            Board board = BoardDto.toEntity(boardDto);
            boardRepository.save(board);
            return board.getId();
        }
        return null;
    }

    @Transactional
    public Long modifyPost(BoardDto boardDto){
        Optional<Board> findModifyingBoard = boardRepository.findById(boardDto.getId());
        if (findModifyingBoard.isPresent()) {
            Board findBoard = findModifyingBoard.get();
            boardRepository.save(findBoard);
            return findBoard.getId();
        }
        return null;
    }

    @Transactional
    public boolean deletePost(Long post_id){
        Optional<Board> deleteBoard = boardRepository.findById(post_id);
        if(deleteBoard.isPresent()){
            boardRepository.deleteById(post_id);
            return true;
        }
        return false;
    }

    @Transactional
    public List<BoardDto> findAllPosts() {
        List<Board> findPostsList = boardRepository.findAll();
        return findPostsList.stream()
                .map(Board::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDto> findPostListByUserNickname(String nickname) {
        List<Board> findPostsListByUser = boardRepository.findAllBoardListByNickname(nickname);
        if(findPostsListByUser!=null){
            return findPostsListByUser.stream()
                    .map(Board::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public BoardDto findPostByTitle(String title) {
        Optional<Board> targetPostWrapper = boardRepository.findByTitle(title);
        if(targetPostWrapper.isPresent()){
            Board targetPost = targetPostWrapper.get();
            return Board.toDto(targetPost);
        }
        return null;
    }

}

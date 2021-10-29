package com.jambit.project.service;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.Reply;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.BoardRepository;
import com.jambit.project.domain.repository.ImageRepository;
import com.jambit.project.dto.BoardDto;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final FileHandler fileHandler;

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
    public Long createPost(BoardDto boardDto, List<MultipartFile> files) throws Exception {
        if (boardDto != null) {
            boardDto.setLikesCount(0L);
            boardDto.setViewCount(0L);
            boardDto.setReplyCount(0L);
            Board board = BoardDto.toEntity(boardDto);

            List<Image> imageList = fileHandler.parseFileInfo(board.getId(), TargetType.POST, files);

            if(!imageList.isEmpty()){
                for(Image image : imageList){
                    imageRepository.save(image);
                }
            }

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
            // 수정되는 속성인 content와 title에 대한 수정 로직
            findBoard.update(boardDto);
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

    //TODO: 페이지네이션 -> Spring data JPA를 활용하여
    @Transactional
    public Page<BoardDto> findAllPosts(Pageable pageable) {
        Page<Board> findPostsPage = boardRepository.findAll(pageable);
        return findPostsPage.map(Board::toDto);
    }

    @Transactional
    public List<BoardDto> findPostListByUserNickname(String nickname) {
        List<Board> findPostsListByUser = boardRepository.findAllBoardListByNickname(nickname);
        return findPostsListByUser.stream()
                .map(Board::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDto> findPostByTitle(String title) {
        List<Board> targetPostList = boardRepository.findByTitleContaining(title);
        return targetPostList.stream()
                .map(Board::toDto)
                .collect(Collectors.toList());
    }

}

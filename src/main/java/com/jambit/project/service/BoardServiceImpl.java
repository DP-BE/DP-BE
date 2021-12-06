package com.jambit.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.*;
import com.jambit.project.dto.*;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final RecommendRepository recommendRepository;
    private final FileHandler fileHandler;
    private final SkillResolveRepository skillResolveRepository;
    private final RecruitPositionRepository recruitPositionRepository;
    private final SkillSetRepository skillSetRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardDto findPost(Long post_id) {
        Optional<Board> findPostWrapper = boardRepository.findById(post_id);
        if (findPostWrapper.isPresent()) {
            Board findPost = findPostWrapper.get();
            BoardDto boardDto = Board.toDto(findPost);
            String nickname = findPost.getNickname();
            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
            byNickname.ifPresent(member -> boardDto.setProfileImage(member.getProfileImage()));
            return boardDto;
        }
        return null;
    }

    @Transactional
    public List<BoardDto> findPostList(String nickname) {
        List<Board> byNickname = boardRepository.findByNickname(nickname);
        return byNickname.stream().map(Board::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDto> findLikedPostList(String nickname) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<Long> idList = recommendRepository.findByTargetTypeAndNickname(TargetType.POST, nickname).stream().map(Recommend::getRefId).collect(Collectors.toList());
        idList.forEach(id -> {
            Optional<Board> byId = boardRepository.findById(id);
            byId.ifPresent(post -> {
                BoardDto boardDto = Board.toDto(post);
                boardDtoList.add(boardDto);
            });
        });
        return boardDtoList;
    }

    @Transactional
    public List<String> findPostSkillSet(Long postId) {
        List<String> skillList = new ArrayList<>();
        List<Long> skillIdList = skillResolveRepository.findByPostIdAndIsDeletedFalse(postId).stream().map(SkillResolve::getSkillId).collect(Collectors.toList());
        skillIdList.forEach(skillId -> {
            Optional<SkillSet> findSkill = skillSetRepository.findById(skillId);
            findSkill.ifPresent(s -> {
                skillList.add(s.getSkillName());
            });
        });
        return skillList;
    }

    @Transactional
    public Long createPost(String boardDto) throws Exception {
        if (boardDto != null) {
            BoardDto boardDto1 = new ObjectMapper().readValue(boardDto, BoardDto.class);
            boardDto1.setLikesCount(0L);
            boardDto1.setViewCount(0L);
            boardDto1.setReplyCount(0L);
            Board board = BoardDto.toEntity(boardDto1);

            boardRepository.save(board);

            StringTokenizer stringTokenizer = new StringTokenizer(boardDto1.getSkillSet(), "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                skillResolveRepository.save(SkillResolve.builder().postId(board.getId()).skillId(skillId).build());
            }

            List<Object> positionList = boardDto1.getPositionList();
            for (Object object : positionList) {
                RecruitPositionDto recruitPositionDto = new ObjectMapper().readValue(object.toString(), RecruitPositionDto.class);
                recruitPositionDto.setPostId(board.getId());
                recruitPositionRepository.save(RecruitPositionDto.toEntity(recruitPositionDto));
            }

            return board.getId();
        }
        return null;
    }

    @Transactional
    public Long modifyPost(BoardDto boardDto){
        Optional<Board> findModifyingBoard = boardRepository.findById(boardDto.getId());
        if (findModifyingBoard.isPresent()) {
            Board findBoard = findModifyingBoard.get();
            findBoard.update(boardDto);

            List<SkillResolve> byPostId = skillResolveRepository.findByPostId(boardDto.getId());
            byPostId.forEach(s -> {
                if (s.getIsDeleted())
                    s.setIsDeleted(true);
            });

            String skillSet = boardDto.getSkillSet();
            StringTokenizer stringTokenizer = new StringTokenizer(skillSet, "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                skillResolveRepository.save(SkillResolve.builder().skillId(skillId).postId(boardDto.getId()).build());
            }

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
    public Page<BoardDto> findAllPosts(Pageable pageable) {
        Page<BoardDto> findPostList = boardRepository.findAll(pageable).map(Board::toDto);
        findPostList.forEach(board -> {
                String nickname = board.getNickname();
                Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
        });
        return findPostList;
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

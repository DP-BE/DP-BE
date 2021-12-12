package com.jambit.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.*;
import com.jambit.project.dto.*;
import com.jambit.project.utility.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final RecommendRepository recommendRepository;
    private final SkillResolveRepository skillResolveRepository;
    private final RecruitPositionRepository recruitPositionRepository;
    private final SkillSetRepository skillSetRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardDto findPost(Long post_id) {
        Optional<Board> findPostWrapper = boardRepository.findById(post_id);
        if (findPostWrapper.isPresent()) {
            Board findPost = findPostWrapper.get();
            findPost.setViewCount(findPost.getViewCount()+1);
            BoardDto boardDto = Board.toDto(findPost);
            String nickname = findPost.getNickname();
            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
            byNickname.ifPresent(member -> boardDto.setProfileImage(member.getProfileImage()));
            boardDto.setSkillList(findPostSkillSet(boardDto.getId()));
            return boardDto;
        }
        return null;
    }

    @Transactional
    public List<BoardDto> findPostList(String nickname) {
        List<BoardDto> postList = boardRepository.findByNickname(nickname).stream().map(Board::toDto).collect(Collectors.toList());
        postList.forEach(board -> {
            String name = board.getNickname();
            Optional<Member> byNickname = memberRepository.findByNickname(name);
            byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
            board.setSkillList(findPostSkillSet(board.getId()));
        });
        return postList;
    }

    @Transactional
    public List<BoardDto> findLikedPostList(String nickname) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<Long> idList = recommendRepository.findByTargetTypeAndNickname(TargetType.POST, nickname).stream().map(Recommend::getRefId).collect(Collectors.toList());
        idList.forEach(id -> {
            Optional<Board> byId = boardRepository.findById(id);
            byId.ifPresent(post -> {
                BoardDto boardDto = Board.toDto(post);
                boardDto.setSkillList(findPostSkillSet(boardDto.getId()));
                Optional<Member> byNickname = memberRepository.findByNickname(post.getNickname());
                byNickname.ifPresent(member -> boardDto.setProfileImage(member.getProfileImage()));
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
        log.info(skillList.toString());
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
                skillResolveRepository.save(SkillResolve.builder().postId(board.getId()).skillId(skillId).isDeleted(false).build());
            }

            List<Object> positionList = boardDto1.getPositionList();
            for (Object object : positionList) {
                RecruitPositionDto recruitPositionDto = new ObjectMapper().readValue(object.toString(), RecruitPositionDto.class);
                recruitPositionDto.setPostId(board.getId());
                recruitPositionDto.setIsDeleted(false);
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
                if (!s.getIsDeleted())
                    s.setIsDeleted(true);
            });

            String skillSet = boardDto.getSkillSet();
            StringTokenizer stringTokenizer = new StringTokenizer(skillSet, "#");
            while (stringTokenizer.hasMoreTokens()) {
                Long skillId = Long.valueOf(stringTokenizer.nextToken());
                skillResolveRepository.save(SkillResolve.builder().skillId(skillId).postId(boardDto.getId()).isDeleted(false).build());
            }

            List<RecruitPosition> findPositions = recruitPositionRepository.findByPostId(boardDto.getId());
            findPositions.forEach(r -> {
                if (!r.getIsDeleted()) {
                    r.setIsDeleted(true);
                }
            });

            List<Object> positionList = boardDto.getPositionList();
            for (Object object : positionList) {
                try {
                    RecruitPositionDto recruitPositionDto = new ObjectMapper().readValue(object.toString(), RecruitPositionDto.class);
                    recruitPositionDto.setPostId(boardDto.getId());
                    recruitPositionDto.setIsDeleted(false);
                    recruitPositionRepository.save(RecruitPositionDto.toEntity(recruitPositionDto));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                log.info(findPostSkillSet(board.getId()).toString());
                board.setSkillList(findPostSkillSet(board.getId()));
        });
        return findPostList;
    }

    @Transactional
    public Page<BoardDto> findFilteredPost(String type, String payload, Pageable pageable) {
        switch(type) {
            case "NICKNAME":
                Page<BoardDto> findBoardByNickname = boardRepository.findPageByNicknameContaining(payload, pageable).map(Board::toDto);
                findBoardByNickname.forEach(board -> {
                    String nickname = board.getNickname();
                    Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                    byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
                    board.setSkillList(findPostSkillSet(board.getId()));
                });
                return findBoardByNickname;
            case "TITLE":
                Page<BoardDto> boardPage = boardRepository.findPageByTitleContaining(payload, pageable).map(Board::toDto);
                boardPage.forEach(board -> {
                    String nickname = board.getNickname();
                    Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                    byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
                    board.setSkillList(findPostSkillSet(board.getId()));
                });
                return boardPage;
            case "PROGRESS":
                switch(payload) {
                    case "ONGOING":
                        Page<BoardDto> findOngoingPage = boardRepository.findPageByProgressType(ProgressType.ONGOING, pageable).map(Board::toDto);
                        findOngoingPage.forEach(board -> {
                            String nickname = board.getNickname();
                            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                            byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
                            board.setSkillList(findPostSkillSet(board.getId()));
                        });
                        return findOngoingPage;
                    case "COMPLETE":
                        Page<BoardDto> findCompletePage = boardRepository.findPageByProgressType(ProgressType.COMPLETE, pageable).map(Board::toDto);
                        findCompletePage.forEach(board -> {
                            String nickname = board.getNickname();
                            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                            byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
                            board.setSkillList(findPostSkillSet(board.getId()));
                        });
                        return findCompletePage;
                }
            case "TECH":
                Optional<SkillSet> findSkill = skillSetRepository.findBySkillName(payload);
                Long skillId = findSkill.map(SkillSet::getId).orElse(null);
                if (skillId != null) {
                    List<SkillResolve> postSkill = skillResolveRepository.findPostSkill(skillId);
                    List<BoardDto> boardList = new ArrayList<>();
                    postSkill.forEach(skill -> {
                        Long postId = skill.getPostId();
                        Optional<Board> findPost = boardRepository.findById(postId);
                        findPost.ifPresent(board -> {
                            boardList.add(Board.toDto(board));
                        });
                    });
                    boardList.forEach(board -> {
                        String nickname = board.getNickname();
                        Optional<Member> byNickname = memberRepository.findByNickname(nickname);
                        byNickname.ifPresent(member -> board.setProfileImage(member.getProfileImage()));
                        board.setSkillList(findPostSkillSet(board.getId()));
                    });
                    return new PageImpl<>(boardList.subList(0, Math.min(pageable.getPageSize(), boardList.size())), pageable, boardList.size());
                }
        }
        return null;
    }

}

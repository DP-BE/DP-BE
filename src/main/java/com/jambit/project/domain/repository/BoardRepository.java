package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.ProgressType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllBoardListByNickname(String nickname);
    Page<Board> findAllBoardPageByNickname(String nickname, Pageable pageable);
    List<Board> findByTitleContaining(String title);
    Page<Board> findBoardPageByTitleContaining(String title, Pageable pageable);
    List<Board> findByNickname(String nickname);
    Page<Board> findPageByNicknameContaining(String nickname, Pageable pageable);
    Page<Board> findPageByTitleContaining(String title, Pageable pageable);
    Page<Board> findPageByProgressType(ProgressType progressType, Pageable pageable);
}

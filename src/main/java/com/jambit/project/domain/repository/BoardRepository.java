package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Board;
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
}

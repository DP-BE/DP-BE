package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();
    Optional<Board> findById(Long id);
    Optional<Board> findByTitle(String title);
}

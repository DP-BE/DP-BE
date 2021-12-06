package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.RecruitPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitPositionRepository extends JpaRepository<RecruitPosition, Long> {
    List<RecruitPosition> findByPostId(Long postId);
    List<RecruitPosition> findByPostIdAndIsDeletedFalse(Long postId);
}

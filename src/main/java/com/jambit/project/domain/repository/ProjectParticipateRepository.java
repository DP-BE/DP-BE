package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.ProjectParticipate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectParticipateRepository extends JpaRepository<ProjectParticipate, Long> {
    List<ProjectParticipate> findByMemberId(Long memberId);
    List<ProjectParticipate> findByProjectId(Long projectId);
}

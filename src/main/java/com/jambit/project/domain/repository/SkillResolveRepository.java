package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.SkillResolve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillResolveRepository extends JpaRepository<SkillResolve, Long> {
    List<SkillResolve> findByMemberIdAndIsDeletedFalse(Long memberId);
    List<SkillResolve> findByProjectIdAndIsDeletedFalse(Long projectId);
    List<SkillResolve> findByMemberId(Long memberId);
    List<SkillResolve> findByProjectId(Long projectId);
}

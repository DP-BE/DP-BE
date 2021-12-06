package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.SkillResolve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillResolveRepository extends JpaRepository<SkillResolve, Long> {
    List<SkillResolve> findByMemberIdAndIsDeletedFalse(Long memberId);
    List<SkillResolve> findByProjectIdAndIsDeletedFalse(Long projectId);
    List<SkillResolve> findByMemberId(Long memberId);
    List<SkillResolve> findByProjectId(Long projectId);
    List<SkillResolve> findByPostId(Long postId);
    List<SkillResolve> findByPostIdAndIsDeletedFalse(Long postId);
    List<SkillResolve> findBySkillIdInAndIsDeletedFalseAndProjectIdIsNull(List<Long> idList);
    List<SkillResolve> findBySkillIdInAndIsDeletedFalseAndMemberIdIsNull(List<Long> idList);

    @Query("SELECT s FROM SkillResolve s " +
            "WHERE s.memberId IS NULL " +
            "AND s.projectId IS NULL " +
            "AND s.postId IS NOT NULL " +
            "AND s.skillId = :skillId")
    List<SkillResolve> findPostSkill(@Param("skillId") Long skillId);
}

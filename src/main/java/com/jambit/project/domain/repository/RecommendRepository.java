package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    List<Recommend> findByPostId(Long postId);
    List<Recommend> findByProjectId(Long projectId);
    List<Recommend> findByReplyId(Long replyId);
    Optional<Recommend> findByPostIdAndNicknameAndIsDeletedFalse(Long postId, String nickname);
    Optional<Recommend> findByProjectIdAndNicknameAndIsDeletedFalse(Long projectId, String nickname);
    Optional<Recommend> findByReplyIdAndNicknameAndIsDeletedFalse(Long replyId, String nickname);
}

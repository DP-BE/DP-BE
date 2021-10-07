package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllReplyListByPostId(Long postId);
    Page<Reply> findAllReplyPageByPostIdAndReferenceIdNull(Long postId, Pageable pageable);
    List<Reply> findByReferenceId(Long referenceId);
    Optional<Reply> findByIdAndReferenceId(Long id, Long referenceId);
}

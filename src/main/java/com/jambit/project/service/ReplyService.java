package com.jambit.project.service;

import com.jambit.project.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyService {
    List<ReplyDto> findAllPostRepliesList(Long postId);

    List<ReplyDto> findAllReferenceRepliesList(Long referenceId);

    List<ReplyDto> findAllProjectRepliesList(Long projectId);

    Page<ReplyDto> findAllPostRepliesPage(Long postId, Pageable pageable);

    String create(ReplyDto replyDto);

    String modify(ReplyDto replyDto);

    boolean deleteReply(Long reply_id);

}

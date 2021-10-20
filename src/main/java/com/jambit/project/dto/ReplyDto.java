package com.jambit.project.dto;

import com.jambit.project.domain.entity.Reply;
import com.jambit.project.domain.entity.TargetType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyDto {

    private Long id;
    private String nickname;
    private String content;
    private Long postId;
    private Long referenceId;
    private Boolean isDeleted;
    private Long likesCount;
    private TargetType targetType;
    private Long projectId;

    public static Reply toEntity(ReplyDto replyDto) {
        return Reply.builder()
                .id(replyDto.getId())
                .nickname(replyDto.getNickname())
                .content(replyDto.getContent())
                .postId(replyDto.getPostId())
                .referenceId(replyDto.getReferenceId())
                .isDeleted(replyDto.getIsDeleted())
                .likesCount(replyDto.getLikesCount())
                .targetType(replyDto.getTargetType())
                .projectId(replyDto.getProjectId())
                .build();
    }
}

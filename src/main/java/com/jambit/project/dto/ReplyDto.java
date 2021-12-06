package com.jambit.project.dto;

import com.jambit.project.domain.entity.Reply;
import com.jambit.project.domain.entity.TargetType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReplyDto {

    private Long id;
    private String nickname;
    private String content;
    private Long postId;
    private Boolean isDeleted;
    private Long likesCount;
    private TargetType targetType;
    private Long projectId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String profileImage;

    public static Reply toEntity(ReplyDto replyDto) {
        return Reply.builder()
                .id(replyDto.getId())
                .nickname(replyDto.getNickname())
                .content(replyDto.getContent())
                .postId(replyDto.getPostId())
                .isDeleted(replyDto.getIsDeleted())
                .likesCount(replyDto.getLikesCount())
                .targetType(replyDto.getTargetType())
                .projectId(replyDto.getProjectId())
                .build();
    }
}

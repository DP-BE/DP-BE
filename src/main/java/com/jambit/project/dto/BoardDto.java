package com.jambit.project.dto;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.ProgressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String skillSet;
    private String profileImage;
    private String contact;
    private Long projectRefId;
    private Long viewCount;
    private Long replyCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long likesCount;
    private Boolean isPublic;
    private ProgressType progressType;

    private List<Object> positionList;
    private List<String> skillList;

    public static Board toEntity(BoardDto boardDto) {
        return Board.builder()
                .id(boardDto.getId())
                .nickname(boardDto.getNickname())
                .title(boardDto.getTitle())
                .skillSet(boardDto.getSkillSet())
                .projectRefId(boardDto.getProjectRefId())
                .content(boardDto.getContent())
                .contact(boardDto.getContact())
                .viewCount(boardDto.getViewCount())
                .replyCount(boardDto.getReplyCount())
                .likesCount(boardDto.getLikesCount())
                .isPublic(boardDto.getIsPublic())
                .progressType(boardDto.getProgressType())
                .build();
    }
}

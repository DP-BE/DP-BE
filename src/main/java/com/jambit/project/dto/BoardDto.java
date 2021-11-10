package com.jambit.project.dto;

import com.jambit.project.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private String nickname;
    private String title;
    private String content;
    private Long viewCount;
    private Long replyCount;
    private LocalDateTime createdDate;
    private Long likesCount;

    public static Board toEntity(BoardDto boardDto) {
        return Board.builder()
                .id(boardDto.getId())
                .nickname(boardDto.getNickname())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .viewCount(boardDto.getViewCount())
                .replyCount(boardDto.getReplyCount())
                .likesCount(boardDto.getLikesCount())
                .build();
    }
}

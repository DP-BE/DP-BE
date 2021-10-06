package com.jambit.project.dto;

import com.jambit.project.domain.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDto {

    private Long id;
    private String nickname;
    private String content;
    private Long likes;
    private Long viewCount;
    private Long replyCount;
    private LocalDateTime createdDate;

    public static Board toEntity(BoardDto boardDto) {
        return Board.builder()
                .id(boardDto.getId())
                .nickname(boardDto.getNickname())
                .content(boardDto.getContent())
                .likes(boardDto.getLikes())
                .viewCount(boardDto.getViewCount())
                .replyCount(boardDto.getReplyCount())
                .build();
    }
}

package com.jambit.project.domain.entity;

import com.jambit.project.dto.BoardDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Board extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long likes;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long replyCount;

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .nickname(board.getNickname())
                .content(board.getContent())
                .likes(board.getLikes())
                .viewCount(board.getViewCount())
                .replyCount(board.getReplyCount())
                .createdDate(board.getCreatedDate())
                .build();
    }
}
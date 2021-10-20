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
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Long viewCount;

    @Column
    private Long replyCount;

    public void update(BoardDto boardDto){
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
    }

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .nickname(board.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .replyCount(board.getReplyCount())
                .createdDate(board.getCreatedDate())
                .build();
    }
}

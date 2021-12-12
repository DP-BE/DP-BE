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

    @Column(nullable = false)
    private Long projectRefId;

    @Column
    private String skillSet;

    @Column
    private Long viewCount;

    @Column
    private Long replyCount;

    @Column
    private Long likesCount;

    @Column
    private Boolean isPublic;

    @Column
    private String contact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressType progressType;

    public void update(BoardDto boardDto){
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.progressType = boardDto.getProgressType();
        this.content = boardDto.getContent();
        this.skillSet = boardDto.getSkillSet();
        this.projectRefId = boardDto.getProjectRefId();
    }

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .nickname(board.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .skillSet(board.getSkillSet())
                .projectRefId(board.getProjectRefId())
                .contact(board.getContact())
                .viewCount(board.getViewCount())
                .replyCount(board.getReplyCount())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getLastModifiedDate())
                .likesCount(board.getLikesCount())
                .isPublic(board.getIsPublic())
                .progressType(board.getProgressType())
                .build();
    }
}

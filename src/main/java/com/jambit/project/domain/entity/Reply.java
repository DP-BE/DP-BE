package com.jambit.project.domain.entity;

import com.jambit.project.dto.ReplyDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String content;

    @Column
    private Long postId;

    @Column
    private Long projectId;

    @Column
    private Boolean isDeleted;

    @Column
    private Long likesCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    public static ReplyDto toDTO(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .nickname(reply.getNickname())
                .content(reply.getContent())
                .postId(reply.getPostId())
                .isDeleted(reply.getIsDeleted())
                .likesCount(reply.getLikesCount())
                .targetType(reply.getTargetType())
                .projectId(reply.getProjectId())
                .createdDate(reply.getCreatedDate())
                .build();
    }
}

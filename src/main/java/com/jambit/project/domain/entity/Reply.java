package com.jambit.project.domain.entity;

import com.jambit.project.dto.ReplyDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Reply extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String content;

    @Column
    private Long postId;

    @Column
    private Long referenceId;

    @Column
    private Boolean isDeleted;

    public static ReplyDto toDTO(Reply reply){
        return ReplyDto.builder()
                .id(reply.getId())
                .nickname(reply.getNickname())
                .content(reply.getContent())
                .postId(reply.getPostId())
                .referenceId(reply.getReferenceId())
                .isDeleted(reply.getIsDeleted())
                .build();
    }
}

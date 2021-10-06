package com.jambit.project.domain.entity;

import com.jambit.project.dto.MemberDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String nickname;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .build();
    }
}

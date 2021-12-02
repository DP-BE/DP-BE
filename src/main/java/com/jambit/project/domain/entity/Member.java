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

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String description;

    @Column
    private String skillSet;

    @Column
    private Long projectCount;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .skillSet(member.getSkillSet())
                .projectCount(member.getProjectCount())
                .build();
    }

    public void update(MemberDto memberDto) {
        this.nickname = memberDto.getNickname();
        this.userId = memberDto.getUserId();
        this.description = memberDto.getDescription();
        this.skillSet = memberDto.getSkillSet();
    }

}

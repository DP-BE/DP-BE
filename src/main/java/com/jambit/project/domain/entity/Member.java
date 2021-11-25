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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String picture;

    @Column
    private String description;

    @Column
    private String skillSet;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .skillSet(member.getSkillSet())
                .build();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Member updateGoogle(String name, String picture) {
        this.nickname = name;
        this.picture = picture;
        return this;
    }

    public Member updateGithub(String name) {
        this.userId = name;
        return this;
    }

    public void update(MemberDto memberDto) {
        this.description = memberDto.getDescription();
        this.skillSet = memberDto.getSkillSet();
    }

}

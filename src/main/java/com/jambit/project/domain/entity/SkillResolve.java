package com.jambit.project.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SkillResolve {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resolve_id")
    private Long id;

    @Column
    private Long projectId;

    @Column(nullable = false)
    private Long skillId;

    @Column
    private Long memberId;

    @Column
    private Long postId;

    @Column
    private Boolean isDeleted;
}

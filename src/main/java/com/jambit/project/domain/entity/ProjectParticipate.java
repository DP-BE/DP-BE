package com.jambit.project.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectParticipate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participate_id")
    private Long id;

    @Column
    private Long projectId;

    @Column
    private Long memberId;
}

package com.jambit.project.domain.entity;

import com.jambit.project.dto.SkillSetDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class SkillSet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    @Column(nullable = false)
    private String skillName;

    public static SkillSetDto toDto(SkillSet skillSet){
        return SkillSetDto.builder()
                .id(skillSet.getId())
                .skillName(skillSet.getSkillName())
                .build();
    }
}

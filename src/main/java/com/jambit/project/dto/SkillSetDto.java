package com.jambit.project.dto;

import com.jambit.project.domain.entity.SkillSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillSetDto {

    private Long id;
    private String skillName;

    public static SkillSet toEntity(SkillSetDto skillSetDto){
        return SkillSet.builder()
                .id(skillSetDto.getId())
                .skillName(skillSetDto.getSkillName())
                .build();
    }
}

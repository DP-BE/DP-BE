package com.jambit.project.service;

import com.jambit.project.dto.SkillSetDto;

import java.util.List;

public interface SkillSetService {
    Long create(SkillSetDto skillSetDto);

    void delete(Long skill_id);

    List<SkillSetDto> findAllSkillSetList();

    List<SkillSetDto> findSkillSetWithName(String skillName);
}

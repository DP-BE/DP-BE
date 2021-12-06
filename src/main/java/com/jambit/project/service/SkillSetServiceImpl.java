package com.jambit.project.service;

import com.jambit.project.domain.entity.SkillSet;
import com.jambit.project.domain.repository.SkillSetRepository;
import com.jambit.project.dto.SkillSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillSetServiceImpl implements SkillSetService{
    private final SkillSetRepository skillSetRepository;

    @Transactional
    public Long create(SkillSetDto skillSetDto) {
        if (skillSetDto != null) {
            SkillSet skillSet = SkillSetDto.toEntity(skillSetDto);
            skillSetRepository.save(skillSet);
            return skillSet.getId();
        }
        return null;
    }

    @Transactional
    public void delete(Long skill_id) {
        Optional<SkillSet> findSkill = skillSetRepository.findById(skill_id);
        findSkill.ifPresent(skillSetRepository::delete);
    }

    @Transactional
    public List<SkillSetDto> findAllSkillSetList() {
        List<SkillSet> allSkill = skillSetRepository.findAll();
        return allSkill.stream().map(SkillSet::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<SkillSetDto> findSkillSetWithName(String skillName) {
        List<SkillSet> bySkillNameContaining = skillSetRepository.findBySkillNameContainingIgnoreCase(skillName);
        return bySkillNameContaining.stream().map(SkillSet::toDto).collect(Collectors.toList());
    }
}

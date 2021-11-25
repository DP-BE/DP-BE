package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.SkillSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillSetRepository extends JpaRepository<SkillSet, Long> {
    List<SkillSet> findBySkillNameContaining(String skillName);
}

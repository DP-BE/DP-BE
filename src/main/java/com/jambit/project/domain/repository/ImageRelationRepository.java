package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.ImageRelation;
import com.jambit.project.domain.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRelationRepository extends JpaRepository<ImageRelation, Long> {
    List<Image> findImageIdByTargetIdAndTargetType(Long targetId, TargetType targetType);
}

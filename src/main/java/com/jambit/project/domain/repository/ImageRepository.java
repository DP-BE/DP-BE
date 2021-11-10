package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllImageListByTargetIdAndTargetType(Long targetId, TargetType targetType);
}

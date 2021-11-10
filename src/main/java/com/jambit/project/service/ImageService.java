package com.jambit.project.service;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.ImageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
    List<ImageDto> getImage(TargetType targetType, Long targetId);
}

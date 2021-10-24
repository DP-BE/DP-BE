package com.jambit.project.service;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
    String uploadImage(TargetType targetType, Long targetId, MultipartFile file);
    String modifyImage(TargetType targetType, Long targetId, List<MultipartFile> files);
    List<Image> getImage(TargetType targetType, Long targetId);
}

package com.jambit.project.dto;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ImageDto {
    private Long id;
    private String originFileName;
    private String fileName;
    private String storedPath;
/*
    public static Image toEntity(ImageDto imageDto){
        return Image.builder()
                .id(imageDto.getId())
                .originFileName(imageDto.originFileName)
                .fileName(imageDto.getFileName())
                .targetType(imageDto.getTargetType())
                .storedPath(imageDto.getStoredPath())
                .build();
    }

 */
}

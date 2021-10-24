package com.jambit.project.domain.entity;

import com.jambit.project.dto.ImageDto;
import jdk.jshell.Snippet;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder

public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storedPath;

/*
    public static ImageDto toDto(Image image){
        return ImageDto.builder()
                .id(image.getId())
                .originFileName(image.getOriginFileName())
                .fileName(image.getFileName())
                .targetType(image.getTargetType())
                .storedPath(image.getStoredPath())
                .build();
    }
*/
}

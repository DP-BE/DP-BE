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

public class Image extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storedPath;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    public static ImageDto toDto(Image image){
        return ImageDto.builder()
                .id(image.getId())
                .originFileName(image.getOriginFileName())
                .fileName(image.getFileName())
                .targetType(image.getTargetType())
                .targetId(image.getTargetId())
                .storedPath(image.getStoredPath())
                .build();
    }

}

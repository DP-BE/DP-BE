package com.jambit.project.utility;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.ImageDto;
import com.jambit.project.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileHandler {

    public List<ImageDto> parseFileInfo(Long targetId, TargetType targetType, MultipartFile[] multipartFiles) throws Exception {
        List<ImageDto> fileList = new ArrayList<>();

        if(multipartFiles.length != 0) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            String path = "images" + File.separator;
            File file = new File(path);

            if(!file.exists()){
                boolean wasSuccessful = file.mkdirs();

                if(!wasSuccessful)
                    log.error("ERROR");
            }

            for(MultipartFile multipartFile : multipartFiles) {

                String originalFileExtension;
                String originalFilename = multipartFile.getOriginalFilename();
                String contentType = multipartFile.getContentType();

                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                else {
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else break;
                }

                String filename = UUID.randomUUID() + originalFileExtension;

                ImageDto imageDto = ImageDto.builder()
                        .originFileName(originalFilename)
                        .fileName(filename)
                        .storedPath(path + File.separator + filename)
                        .targetId(targetId)
                        .targetType(targetType)
                        .build();

                fileList.add(imageDto);

                file = new File(absolutePath+path + File.separator + filename);
                multipartFile.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }

}

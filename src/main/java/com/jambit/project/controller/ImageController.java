package com.jambit.project.controller;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.ImageDto;
import com.jambit.project.service.BoardService;
import com.jambit.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final String ABSOLUTE_PATH = "/home/ec2-user";

    @GetMapping("")
    public ResponseEntity<List<ImageDto>> getImage(@RequestParam TargetType targetType,
                                                @RequestParam Long targetId){
        List<ImageDto> images = imageService.getImage(targetType, targetId);
        if(images != null){
            return new ResponseEntity<>(images,HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping(
            value = "/get-image-with-media-type",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam String fileName) throws IOException {
        InputStream imageStream = new FileInputStream(ABSOLUTE_PATH + "/images" + File.separator + fileName);
        return IOUtils.toByteArray(imageStream);
    }

}

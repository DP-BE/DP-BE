package com.jambit.project.controller;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.dto.ImageDto;
import com.jambit.project.service.BoardService;
import com.jambit.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin // 다른 origin으로부터의 요청이 차단되는 오류를 해결하기 위함
@Slf4j
public class ImageController {
    private ImageService imageService;

    /*
    @PostMapping("/upload") // 매핑 어떻게 할 것인지?
    public ResponseEntity<String> registerImage(@RequestParam TargetType targetType,
                                                @RequestParam Long targetId,
                                                @RequestParam MultipartFile file){
        String path = imageService.uploadImage(targetType, targetId, file);
        if(path != null){
            return new ResponseEntity<>(path, HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
     */

    @GetMapping("/image")
    public ResponseEntity<List<Image>> getImage(@RequestParam TargetType targetType,
                                                @RequestParam Long targetId){
        List<Image> images = imageService.getImage(targetType, targetId);
        if(images != null){
            return new ResponseEntity<>(images,HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

}
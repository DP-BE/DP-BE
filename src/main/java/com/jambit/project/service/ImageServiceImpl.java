package com.jambit.project.service;

import com.jambit.project.domain.entity.Image;
import com.jambit.project.domain.entity.ImageRelation;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.ImageRelationRepository;
import com.jambit.project.domain.repository.ImageRepository;
import com.jambit.project.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl {
    private final ImageRepository imageRepository;
    private final ImageRelationRepository imageRelationRepository;

    @Transactional
    public void modifyImage(TargetType targetType, Long targetId, List<MultipartFile> files) throws Exception {
        List<Image> images = imageRelationRepository.findImageIdByTargetIdAndTargetType(targetId, targetType);

        // 원래 저장된 이미지가 없고, 새로 등록한 이미지는 존재하는 경우 이미지 생성
        if(CollectionUtils.isEmpty(images) && !CollectionUtils.isEmpty(files)){
            for(MultipartFile multipartFile: files) {
                String path = uploadImage(targetType, targetId, multipartFile);
            }
        }
        else if(!CollectionUtils.isEmpty(images)){
            if(CollectionUtils.isEmpty(files)){
                for(Image image : images){
                    removeFile(image);
                }
            }
            else{
                List<String> dbOriginNameList = new ArrayList<>();
                for(Image image : images){
                    String dbOriginName = image.getOriginFileName();
                    if(!files.contains(dbOriginName)){
                        removeFile(image);
                    }
                    else dbOriginNameList.add(dbOriginName);
                }

                for(MultipartFile multipartFile : files){
                    String filename = multipartFile.getOriginalFilename();
                    if(!dbOriginNameList.contains(filename)){
                        String path = uploadImage(targetType,targetId,multipartFile);
                    }
                }
            }
        }
    }

    private void removeFile(Image image){
        String path = image.getStoredPath();
        File file = new File(path);
        if(file.exists()) file.delete();
        imageRepository.deleteById(image.getId());
        imageRelationRepository.deleteById(image.getId());
    }

    @Transactional
    public String uploadImage(TargetType targetType, Long targetId, MultipartFile file) throws Exception {
        if(!file.isEmpty()) {
            String fileName = createFileName(file.getOriginalFilename());
            String dirPath = System.getProperty("user.dir") + "\\files";
            if (!new File(dirPath).exists()) {
                new File(dirPath).mkdir();
            }

            String path = dirPath + "\\" + fileName;
            file.transferTo(new File(path));

            Image image = Image.builder()
                    .originFileName(file.getOriginalFilename())
                    .storedPath(path)
                    .fileName(fileName)
                    .build();

            ImageRelation imageRelation = ImageRelation.builder()
                    .imageId(image.getId())
                    .targetId(targetId)
                    .targetType(targetType)
                    .build();

            imageRepository.save(image);
            imageRelationRepository.save(imageRelation);

            return path;
        }
        else return null;
    }

    @Transactional
    public List<Image> getImage(TargetType targetType, Long targetId){
        List<Image> images = imageRelationRepository.findImageIdByTargetIdAndTargetType(targetId, targetType);
        if(!images.isEmpty()) {
            return images;
        }
        else return null;
    }

    private String createFileName(String originalFilename){
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    private String getFileExtension(String filename){
        try {
            return filename.substring(filename.lastIndexOf('.'));
        }
        catch (StringIndexOutOfBoundsException e){
            return null;
        }
    }
}

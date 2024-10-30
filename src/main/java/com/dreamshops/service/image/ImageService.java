package com.dreamshops.service.image;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Image getImageByUrl(String downloadUrl);

    Image getImageById(Long imageId);

    void deleteImageById(Long imageId);

    List<ImageDto> saveImage(List<MultipartFile> file, Long productId);

    Image updateImage(MultipartFile file, Long imageId);

}

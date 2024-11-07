package com.dreamshops.service.image;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Image getImageByUrl(String downloadUrl);

    Image getImageById(int imageId);

    void deleteImageById(int imageId);

    List<ImageDto> saveImage(List<MultipartFile> file, int productId);

    Image updateImage(MultipartFile file, int imageId);

}

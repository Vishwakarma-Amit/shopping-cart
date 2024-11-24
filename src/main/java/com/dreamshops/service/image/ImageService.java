package com.dreamshops.service.image;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ImageService {

    Image getImageByUrl(String downloadUrl);

    Image getImageById(int imageId);

    void deleteImageById(int imageId);

    List<ImageDto> saveImage(List<MultipartFile> file, int productId) throws IOException, SQLException;

    void updateImage(MultipartFile file, int imageId) throws IOException, SQLException;

}

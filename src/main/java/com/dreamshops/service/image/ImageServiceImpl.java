package com.dreamshops.service.image;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.ImageRepository;
import com.dreamshops.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Image getImageByUrl(String downloadUrl) {
        return imageRepository.findByDownloadUrl(downloadUrl);
    }

    @Override
    public Image getImageById(int imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found with id: "+imageId));
    }

    @Override
    public void deleteImageById(int imageId) {
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete,()->{
                    throw new ResourceNotFoundException("Image not found with id: "+imageId);
                });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, int productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for(MultipartFile file: files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";

                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getImageId());
                imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getImageId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setFileType(savedImage.getFileType());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                imageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDtos;
    }

    @Override
    public Image updateImage(MultipartFile file, int imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found with id: "+imageId));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

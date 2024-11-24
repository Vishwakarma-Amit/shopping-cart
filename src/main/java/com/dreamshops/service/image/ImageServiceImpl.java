package com.dreamshops.service.image;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.dto.ProductDto;
import com.dreamshops.entity.Image;
import com.dreamshops.entity.Product;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.ImageRepository;
import com.dreamshops.service.product.ProductService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Override
    public Image getImageByUrl(String downloadUrl) {
        return imageRepository.findByDownloadUrl(downloadUrl);
    }

    @Override
    public Image getImageById(int imageId) {
        final String methodName = "getImageById";
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(Message.IMAGE_NOT_FOUND + imageId));
        log.info("{} - Image retrieved by id - {}", methodName, imageId);
        return image;
    }

    @Override
    public void deleteImageById(int imageId) {
        final String methodName = "deleteImageById";
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete,()->{
                    throw new ResourceNotFoundException(Message.IMAGE_NOT_FOUND +imageId);
                });
        log.info("{} - Image with id - {} deleted!", methodName, imageId);
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, int productId) throws IOException, SQLException {
        final String methodName = "saveImage";
        log.info("{} - execution starts", methodName);
        ProductDto product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for(MultipartFile file: files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImageFile(new SerialBlob(file.getBytes()));
                image.setProduct(modelMapper.map(product, Product.class));
                log.info("{} - created image", methodName);

                String buildDownloadUrl = "/api/v1/images/download/";

                Image savedImage = imageRepository.save(image);
                log.info("{} - Image persisted into db", methodName);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getImageId());
                imageRepository.save(savedImage);
                log.info("{} - Image download url - {}", methodName, buildDownloadUrl);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getImageId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setFileType(savedImage.getFileType());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                imageDtos.add(imageDto);

            } catch (IOException e) {
                throw new IOException(e.getMessage());
            } catch (SQLException ex){
                throw new SQLException(ex.getMessage());
            }
        }
        log.info("{} - Execution finished!", methodName);
        return imageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, int imageId) throws IOException, SQLException{
        final String methodName = "updateImage";
        Image image = imageRepository.findById(imageId)
                .orElseThrow(()-> new ResourceNotFoundException(Message.IMAGE_NOT_FOUND+imageId));
        log.info("{} - image with id - {} retrieved", methodName, imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageFile(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
            log.info("{} - image updated successfully!", methodName);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
    }
}

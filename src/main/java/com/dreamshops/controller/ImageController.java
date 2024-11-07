package com.dreamshops.controller;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.image.ImageService;
import com.dreamshops.utility.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam int productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_SUCCESSFUL, imageDtos), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_FAILED, e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName()+ "\"").body(resource);
    }

    @PutMapping("/update/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable int imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image!=null ){
                imageService.updateImage(file, imageId);
                return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL, null), HttpStatus.OK);
            }
        } catch (ResourceNotFoundException  e) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable int imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image!=null ){
                imageService.deleteImageById(imageId);
                return new ResponseEntity<>(new ApiResponse(Message.DELETE_SUCCESSFUL, null), HttpStatus.OK);
            }
        } catch (ResourceNotFoundException  e) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

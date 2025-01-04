package com.dreamshops.controller;

import com.dreamshops.dto.ImageDto;
import com.dreamshops.entity.Image;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.image.ImageService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> saveImages(@RequestPart List<MultipartFile> files, @RequestParam int productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_SUCCESSFUL, imageDtos), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_FAILED, e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImageFile().getBytes(1, (int)image.getImageFile().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName()+ "\"").body(resource);
    }

    @PutMapping("/update/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable int imageId, @RequestBody MultipartFile file) {
        try {
            imageService.updateImage(file, imageId);
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL, null), HttpStatus.OK);
        } catch ( SQLException e) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable int imageId) {
        try {
            imageService.deleteImageById(imageId);
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_SUCCESSFUL, null), HttpStatus.OK);
        } catch (ResourceNotFoundException  e) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

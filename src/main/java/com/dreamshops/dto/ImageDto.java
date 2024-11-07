package com.dreamshops.dto;

import lombok.Data;

@Data
public class ImageDto {
    private int imageId;
    private String fileName;
    private String fileType;
    private String downloadUrl;
}

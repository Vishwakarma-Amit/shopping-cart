package com.dreamshops.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse {


    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private Object data;

    public ApiResponse( String message, Object data) {
        this.data = data;
        this.message = message;
    }

}

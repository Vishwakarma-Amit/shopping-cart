package com.dreamshops.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private int userId;
    private String firstName;
    private String lastName;

    private String email;

    private List<OrderDto> orders;

    private CartDto cart;
}

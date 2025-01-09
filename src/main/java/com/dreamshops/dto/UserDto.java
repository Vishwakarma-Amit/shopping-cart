package com.dreamshops.dto;

import com.dreamshops.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {

    private int userId;
    private String firstName;
    private String lastName;

    private String email;

    private Set<Role> roles;

    private List<OrderDto> orders = new ArrayList<>();

    private CartDto cart;
}

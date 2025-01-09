package com.dreamshops.service.user;

import com.dreamshops.dto.UserDto;
import com.dreamshops.entity.User;
import com.dreamshops.request.UserRequest;

public interface UserService {
    UserDto getUserById(int userId);
    UserDto createUser(UserRequest userRequest);
    UserDto updateUser(UserRequest request, int userId);
    void deleteUser(int userId);
    UserDto getUserByEmail(String email);

    User getAuthenticatedUser();
}

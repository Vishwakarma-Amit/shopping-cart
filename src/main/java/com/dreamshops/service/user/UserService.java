package com.dreamshops.service.user;

import com.dreamshops.entity.User;
import com.dreamshops.request.UserRequest;

public interface UserService {
    User getUserById(int userId);
    User createUser(UserRequest userRequest);
    User updateUser(UserRequest request, int userId);
    void deleteUser(int userId);
}

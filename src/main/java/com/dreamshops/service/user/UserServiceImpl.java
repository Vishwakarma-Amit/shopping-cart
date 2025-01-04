package com.dreamshops.service.user;

import com.dreamshops.dto.UserDto;
import com.dreamshops.entity.User;
import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.UserRepository;
import com.dreamshops.request.UserRequest;
import com.dreamshops.utility.Converter;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final Converter userConverter;

    @Override
    public UserDto getUserById(int userId) {
        final String methodName = "getUserById";
        log.info("{}} - user fetched by user id - {}",methodName, userId);
        User saveUser = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException(Message.USER_NOT_FOUND+userId));
        return userConverter.convertToDto(saveUser);
    }

    @Override
    public UserDto createUser(UserRequest userRequest) {
        final String methodName = "createUser";
        log.info("{} - creating user", methodName);
        User saveUser = Optional.of(userRequest)
                .filter(user -> !userRepository.existsByEmail(userRequest.getEmail()))
                .map(request -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstName(request.getFirstName());
                    log.info("{}", request.getFirstName());
                    user.setLastName(request.getLastName());
                    log.info("{} - User saved successfully!", methodName);
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistsException(Message.USER_ALREADY_EXISTS));

        return userConverter.convertToDto(saveUser);
    }

    @Override
    public UserDto updateUser(UserRequest request, int userId) {
        final String methodName = "updateUser";
        log.info("{} - updating user with user id - {}", methodName, userId);
        User saveUser = userRepository.findById(userId)
                .map(existingUser->{
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(()->new ResourceNotFoundException(Message.USER_NOT_FOUND+userId));

        return userConverter.convertToDto(saveUser);
    }

    @Override
    public void deleteUser(int userId) {
        final String methodName = "deleteUser";
        log.info("{} - invoked with user id - {}", methodName, userId);
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, ()-> {
                    throw new ResourceNotFoundException(Message.USER_NOT_FOUND+userId);
                });
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userConverter.convertToDto(userRepository.findByEmail(email));
    }

}

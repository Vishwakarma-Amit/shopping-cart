package com.dreamshops.controller;

import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.request.UserRequest;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.service.user.UserService;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserDetailsById(@PathVariable int userId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, userService.getUserById(userId)), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse> getUserDetailsByEmail(@RequestParam String email){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, userService.getUserByEmail(email)), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable int userId){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.UPDATE_SUCCESSFUL, userService.updateUser(userRequest, userId)), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.UPLOAD_FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN)")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable int userId){
        try{
            userService.deleteUser(userId);
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_SUCCESSFUL, null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(Message.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.DELETE_FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

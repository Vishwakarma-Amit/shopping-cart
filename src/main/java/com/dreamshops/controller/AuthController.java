package com.dreamshops.controller;

import com.dreamshops.exception.AlreadyExistsException;
import com.dreamshops.request.LoginRequest;
import com.dreamshops.request.UserRequest;
import com.dreamshops.response.ApiResponse;
import com.dreamshops.response.JwtResponse;
import com.dreamshops.security.jwt.JwtUtils;
import com.dreamshops.security.user.ShopUserDetail;
import com.dreamshops.service.user.UserService;
import com.dreamshops.utility.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetail userDetails = (ShopUserDetail) authentication.getPrincipal();
            JwtResponse response = new JwtResponse(
                    userDetails.getId(), jwt
            );
            return new ResponseEntity<>( new ApiResponse(Message.SUCCESS, response), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>( new ApiResponse(Message.FAILED, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        try{
            return new ResponseEntity<>(new ApiResponse(Message.SUCCESS, userService.createUser(userRequest)), HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(new ApiResponse(Message.USER_ALREADY_EXISTS, e.getMessage()), HttpStatus.CONFLICT);
        }catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(Message.FAILED, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


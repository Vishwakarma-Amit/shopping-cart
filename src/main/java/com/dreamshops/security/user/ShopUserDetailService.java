package com.dreamshops.security.user;

import com.dreamshops.entity.User;
import com.dreamshops.exception.ResourceNotFoundException;
import com.dreamshops.repository.UserRepository;
import com.dreamshops.utility.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(()->new ResourceNotFoundException(Message.USER_NOT_FOUND_WITH_EMAIL+email));

        log.info("loadUserByUsername - User from db: {}", user.getEmail());

        return ShopUserDetail.buildUserDetails(user);
    }
}

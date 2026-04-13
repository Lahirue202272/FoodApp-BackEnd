package com.phegon.FoodApp.security;

import com.phegon.FoodApp.auth_users.entity.User;
import com.phegon.FoodApp.auth_users.repository.UserRepository;
import com.phegon.FoodApp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService { //When Spring Security wants to log in a user, use this class to find that user from the database.

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //here, username actually contains the user’s email.

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return AuthUser.builder()
                .user(user)
                .build(); //Take the user that was found from the database, put it inside an AuthUser object, and return that AuthUser.
    }
}

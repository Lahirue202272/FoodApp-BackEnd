package com.phegon.FoodApp.security;

import com.phegon.FoodApp.auth_users.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Data
public class AuthUser implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()//Get the roles from your database user.
                .stream()//“Process the list one by one.”
                .map(role -> new SimpleGrantedAuthority(role.getName())) //Convert each role into a Spring Security authority object.
                .toList(); //Collect all converted authorities into a list.
    }//This tells Spring Security:“What permissions or roles does this user have?”

    @Override
    public String getPassword() {
        return user.getPassword();
    }//“This is the password of the user.”

    @Override
    public String getUsername() {
        return user.getEmail();
    }//Use the email as the username

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }//Is this account enabled and allowed to log in?
}

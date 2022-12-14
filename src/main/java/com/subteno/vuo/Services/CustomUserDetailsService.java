package com.subteno.vuo.Services;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.subteno.vuo.Model.CustomUserDetails;
import com.subteno.vuo.Model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServices userServices;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = (User) userServices.GetUser(username).getObject();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new CustomUserDetails(user);
    }

}

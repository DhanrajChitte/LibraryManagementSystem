package com.LibraryManagementApplication.LibraryManagementApplication.config;

import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService
{

    @Autowired
    private UserInfoRepository repository;

    //Load User by the username and give the details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<UserInfo>userInfo=repository.findByName(username);

        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found " +username));

    }




}

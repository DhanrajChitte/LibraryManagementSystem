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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        Optional<UserInfo>userInfo=repository.findByEmail(email);

        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found " + email));

    }


    public String getUserIdByEmail(String email)
    {
       Optional <UserInfo> userInfoOptional=repository.findByEmail(email);
        return userInfoOptional.map(UserInfo::getId)
                .orElse(null);
    }



}

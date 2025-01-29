package com.LibraryManagementApplication.LibraryManagementApplication.config;

import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoUserDetails implements UserDetails
{

    // From the database take the name, passs and role and convert to it and give to the
    // UserInfoUserDetails
    private String email;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(UserInfo userInfo)
    {
        this.email=userInfo.getEmail();
        this.password=userInfo.getPassword();
        //Using the list we define the multiple roles to the single user
        this.authorities= Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername()
        {
          return email;
        }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Default implementation, change based on your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Default implementation, change based on your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Default implementation, change based on your needs
    }
}

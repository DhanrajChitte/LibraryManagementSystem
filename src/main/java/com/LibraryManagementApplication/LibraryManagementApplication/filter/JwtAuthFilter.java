package com.LibraryManagementApplication.LibraryManagementApplication.filter;

import com.LibraryManagementApplication.LibraryManagementApplication.config.UserInfoUserDetailsService;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.AuthorServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter
{

    @Autowired
    @Lazy
    private AuthorServiceImpl authorService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader= request.getHeader("Authorization");
        String token=null;
        String email=null;
        String userId=null;
        String role=null;

        if(authHeader!=null && authHeader.startsWith("Bearer "))
        {
            //If bearer give in the bearer token then we want to discard the bearer token
            token=authHeader.substring(7);

            email= authorService.extractUsername(token);

            userId = authorService.extractUserId(token); // Extract userId from the token

            System.out.println("User Info:");
            System.out.println("Email: " + email);
            System.out.println("User ID: " + userId);
        }


        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=userDetailsService.loadUserByUsername(email);

            if(authorService.validateToken(token,userDetails))
            {
                //System.out.println("UserId: " + userId);  // You now have access to the userId from the token
                //System.out.println("Roles: " +userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken
                        (userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
       filterChain.doFilter(request,response);
    }
}

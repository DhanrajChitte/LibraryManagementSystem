package com.LibraryManagementApplication.LibraryManagementApplication.config;



import com.LibraryManagementApplication.LibraryManagementApplication.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurity
{
    @Autowired
    private JwtAuthFilter authFilter;
    //authentication
   //Store the user in memory first not in the database
    @Bean
    public UserDetailsService userDetailsService()
    {
     /*   UserDetails admin= User.withUsername("Dhanraj")
                .password(encoder.encode("Dhanraj@0304"))
                .roles("ADMIN")
                .build();

        UserDetails user= User.withUsername("Suyash")
                .password(encoder.encode ("Suyash@0304"))
                .roles("USER")  //Not good practice to direct give the password
                .build();

        return new InMemoryUserDetailsManager(admin,user); */
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http.csrf(AbstractHttpConfigurer::disable)
                  .authorizeHttpRequests(authorize->authorize
                  .requestMatchers("/api/authors/new","/api/authors/authenticate").permitAll()

                                  .anyRequest().authenticated()
                  )
              .sessionManagement(session->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
                .cors(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                 // .formLogin(Customizer.withDefaults())
                        .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //Define the bean for the AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

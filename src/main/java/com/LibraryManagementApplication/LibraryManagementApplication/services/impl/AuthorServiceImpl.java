package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.DataNotFoundException;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.InvalidAuthorDataException;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.AuthorRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.UserInfoRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.AuthorService;
import com.mongodb.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.codec.Decoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorServiceImpl implements AuthorService
{
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Author createAuthor(Author author)
    {
        if (author == null || author.getName() == null || author.getName().isEmpty())
        {
            //System.out.println("Author name must be required");
            throw new CustomExceptions.BadRequestException("Author details are invalid.");
        }

        if (authorRepository.existsById(author.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID" + author.getId() + " already exists.");
        }
            return authorRepository.save(author);
    }

   @Override
    public List <Author> getAllAuthors()
    {
        List<Author> authors=authorRepository.findAll();
        if(authors.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No Authors Found in the System");
        }
        return authors;
     }

    @Override
    public Author getAuthorById(String id)
    {
        return authorRepository.findById(id).orElseThrow(()->
                new CustomExceptions.ResourceNotFoundException("Author not found with ID: " + id));
    }



    @Override
    public Author updateAuthor(String id, Author author)
    {
        // Fetch the existing author or throw an exception if not found
        Author existingAuthor = getAuthorById(id);

        // Update only the modified fields
        if (author.getName() != null && !author.getName().isEmpty()) {
            existingAuthor.setName(author.getName());
        }
        if (author.getBio() != null && !author.getBio().isEmpty()) {
            existingAuthor.setBio(author.getBio());
        }
        // Save and return the updated author
        return authorRepository.save(existingAuthor);
    }


    @Override
    public void deleteAuthor(String id)
    {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }


    public String addUser(UserInfo userInfo)
    {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added to the System";
    }

    public static final String SECRET="f3e1b1e2d7c5a6f4b4d2e3c1a5b7f8e9c0d1e2f3a5b6c7d8e9f0a1b2c3d4e5f6";

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T>  T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && ! isTokenExpired(token));
    }


    public String generateToken(String userName)
    {
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName);
    }

    private String createToken(Map<String, Object> claims, String userName)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey()
    {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
       return Keys.hmacShaKeyFor(keyBytes);
    }
}

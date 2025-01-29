package com.LibraryManagementApplication.LibraryManagementApplication.services;

import com.LibraryManagementApplication.LibraryManagementApplication.dto.AuthRequest;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.mongodb.Function;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.List;
import java.util.Map;

public interface AuthorService
{
    public Author createAuthor(Author author);

    public List<Author> getAllAuthors();

    public Author getAuthorById(String id);

    public Author updateAuthor(String id, Author author);

    public void deleteAuthor(String id);

    public String addUser(UserInfo userInfo);

    public String generateToken(String username,String token);

    public String extractUsername(String token);

    public <T>  T extractClaim(String token, Function<Claims,T> claimsResolver);

    public Claims extractAllClaims(String token);

    public Boolean isTokenExpired(String token);

    public Boolean validateToken(String token, UserDetails userDetails);

    public String createToken(Map<String, Object> claims, String userName);

    public Key getSignKey();

    public String authenticateUser(AuthRequest authRequest);

    public List<Book> getBooksByAuthor(String authorId);


}

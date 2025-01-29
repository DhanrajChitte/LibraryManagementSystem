package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;

import com.LibraryManagementApplication.LibraryManagementApplication.config.UserInfoUserDetailsService;
import com.LibraryManagementApplication.LibraryManagementApplication.dto.AuthRequest;
import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.AuthorRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.BookRepository;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private BookRepository bookRepository;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Override
    public Author createAuthor(Author author)
    {

        UserInfo user=repository.findById(author.getId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found with given ID"));


        if(!"ROLE_AUTHOR".equals(user.getRoles()))
        {
            throw new CustomExceptions.BadRequestException("Only users with ROLE_AUTHOR can be assigned as authors.");
        }


        if (author == null || author.getName() == null || author.getName().isEmpty())
        {
            //System.out.println("Author name must be required");
            throw new CustomExceptions.BadRequestException("Author details are invalid.");
        }

        if (authorRepository.existsById(author.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID " + author.getId() + " already exists.");
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

    @Override
    public List<Book> getBooksByAuthor(String authorId) {
        List<Book> book = bookRepository.findByAuthorId(authorId);


        if (book.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("Book not found with author ID: " + authorId);
        }
        return book;
        //return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public String addUser(UserInfo userInfo)
    {

        if(userInfo.getEmail()==null || userInfo.getEmail().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Email is required");
        }

        if(userInfo.getPassword()==null || userInfo.getPassword().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Password is required");
        }


        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        try
        {
            repository.save(userInfo);
        }

        catch (DuplicateKeyException ex)
        {
            throw new CustomExceptions.BadRequestException("Email already exists in the system");
        }
        catch(Exception ex)
        {
            throw new CustomExceptions.InternalServerException("An error occur while saving the user");
        }
        return "User added to the System";
    }

    public static final String SECRET="f3e1b1e2d7c5a6f4b4d2e3c1a5b7f8e9c0d1e2f3a5b6c7d8e9f0a1b2c3d4e5f6";

    @Override
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token)
    {
        return extractClaim(token,claims -> claims.get("userId", String.class));
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    @Override
    public <T>  T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    @Override
    public Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && ! isTokenExpired(token));
    }


    @Override
    public String generateToken(String userName,String userId)
    {
        Map<String,Object> claims=new HashMap<>();
        claims.put("userId",userId);
        return createToken(claims,userName);
    }

    @Override
    public String createToken(Map<String, Object> claims, String userName)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public Key getSignKey()
    {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String authenticateUser(AuthRequest authRequest)
    {
        Authentication authentication= authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(authRequest.
                getEmail(),authRequest.getPassword()));


        if(!authentication.isAuthenticated())
        {
            throw new CustomExceptions.BadRequestException("Invalid Email or Password ");
        }

        // Fetch the userId based on the email after successful authentication
        String email = authRequest.getEmail();
        String userId = userDetailsService.getUserIdByEmail(email);

        return generateToken(email,userId);
    }
}

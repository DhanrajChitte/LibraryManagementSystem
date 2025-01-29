package com.LibraryManagementApplication.LibraryManagementApplication.controllers;

import com.LibraryManagementApplication.LibraryManagementApplication.dto.AuthRequest;
import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Response;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.AuthorServiceImpl;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController
{
    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Author>> createAuthor(@Valid @RequestBody Author author) {
        Response<Author> response = new Response<>();

            Author createdAuthor = authorService.createAuthor(author);

            response.setSuccess(true);
            response.setMessage("Author created successfully.");
            response.setData(createdAuthor);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','AUTHOR')")
    public ResponseEntity<Response<List<Author>>> getAllAuthors() {
        Response<List<Author>> response = new Response<>();

            List<Author> authors = authorService.getAllAuthors();

            response.setSuccess(true);
            response.setMessage("Authors retrieved successfully.");
            response.setData(authors);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','AUTHOR')")
    public ResponseEntity<Response<Author>> getAuthorById(@PathVariable String id) {
        Response<Author> response = new Response<>();

            Author author = authorService.getAuthorById(id);
            response.setSuccess(true);
            response.setMessage("Author retrieved successfully.");
            response.setData(author);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Author>> updateAuthor(@PathVariable String id, @Valid @RequestBody Author author) {
        Response<Author> response = new Response<>();

            Author updatedAuthor = authorService.updateAuthor(id, author);


            response.setSuccess(true);
            response.setMessage("Author updated successfully.");
            response.setData(updatedAuthor);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Void>> deleteAuthor(@PathVariable String id) {
        Response<Void> response = new Response<>();

            authorService.deleteAuthor(id);

            response.setSuccess(true);
            response.setMessage("Author deleted successfully.");
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @GetMapping("/{authorId}/books")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    public ResponseEntity<Response<List<Book>>> getBooksByAuthor(@PathVariable String authorId) {
        try {
            Response<List<Book>> response = new Response<>();
            List<Book> books = authorService.getBooksByAuthor(authorId);
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Books fetched  successfully with author ID " + authorId);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Book not found with author ID: " + authorId);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Response <String>> addNewUser(@RequestBody UserInfo userInfo)
    {
        Response<String> response = new Response<>();
        //try {
        // Directly call the service method
        String result = authorService.addUser(userInfo);

        response.setSuccess(true);
        response.setMessage("User added successfully for the authentication");
        response.setData(result);
        response.setError(null);
        response.setHttpErrorCode(HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Response<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest)
    {
        Response<String> response=new Response<>();

        String token = authorService.authenticateUser(authRequest);

        response.setSuccess(true);
        response.setMessage("Authentication successful");
        response.setData(token);
        response.setError(null);
        response.setHttpErrorCode(HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package com.LibraryManagementApplication.LibraryManagementApplication.controllers;

import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Response;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.AuthorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorServiceImpl authorService;

    @PostMapping
    public ResponseEntity<Response<Author>> createAuthor(@Valid @RequestBody Author author) {
        Response<Author> response = new Response<>();
        try {
            // Directly call the service method
            Author createdAuthor = authorService.createAuthor(author);

            response.setSuccess(true);
            response.setMessage("Author created successfully.");
            response.setData(createdAuthor);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.BadRequestException e) {
            throw new CustomExceptions.BadRequestException("Author details are invalid.");
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID " + author.getId() + " already exists.");
        }
    }

    @GetMapping
    public ResponseEntity<Response<List<Author>>> getAllAuthors() {
        Response<List<Author>> response = new Response<>();
        try {
            List<Author> authors = authorService.getAllAuthors();
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Authors retrieved successfully.");
            response.setData(authors);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("No Authors Found in the System");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Response<Author>> getAuthorById(@PathVariable String id) {
        Response<Author> response = new Response<>();
        try {
            //Call the service method
            Author author = authorService.getAuthorById(id);
            response.setSuccess(true);
            response.setMessage("Author retrieved successfully.");
            response.setData(author);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception and throw with a custom message
            throw new CustomExceptions.ResourceNotFoundException("No Author Found with ID:" + id);
        }
    }


    // PUT: /api/authors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Response<Author>> updateAuthor(@PathVariable String id, @Valid @RequestBody Author author) {
        Response<Author> response = new Response<>();
        try {
            // Call the service method
            Author updatedAuthor = authorService.updateAuthor(id, author);

            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Author updated successfully.");
            response.setData(updatedAuthor);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Author not found with ID: " + id);
        }
    }

    // DELETE: /api/authors/{id}
    @DeleteMapping("/{id}")

    public ResponseEntity<Response<Void>> deleteAuthor(@PathVariable String id) {
        Response<Void> response = new Response<>();
        try {
            // Call the service method
            authorService.deleteAuthor(id);
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Author deleted successfully.");
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } 
        catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Author not found with ID: " + id);
        }

    }
}
package com.LibraryManagementApplication.LibraryManagementApplication.controllers;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.Response;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.AuthorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController
{
    @Autowired
    private AuthorServiceImpl authorService;

    @PostMapping
    public ResponseEntity<Response<Author>> createAuthor(@Valid @RequestBody Author author)
    {
        // Directly call the service method
        Author createdAuthor = authorService.createAuthor(author);

        // Prepare a success response
        Response<Author> response = new Response<>();
        response.setSuccess(true);
        response.setMessage("Author created successfully.");
        response.setData(createdAuthor);
        response.setError(null);
        response.setHttpErrorCode(HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity <Author> createAuthor(@Valid @RequestBody Author author)
    {
        return ResponseEntity.ok(authorService.createAuthor(author));
    }*/

    @GetMapping
    public ResponseEntity <List<Author>> getAllAuthors()
    {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable String id)
    {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    // PUT: /api/authors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable String id, @Valid @RequestBody Author author)
    {
        Author updatedAuthor = authorService.updateAuthor(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    // DELETE: /api/authors/{id}
    @DeleteMapping("/{id}")

    public ResponseEntity <Void> deleteAuthor(@PathVariable String id)
    {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}

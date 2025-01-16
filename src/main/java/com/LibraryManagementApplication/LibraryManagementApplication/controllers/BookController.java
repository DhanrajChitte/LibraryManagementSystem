package com.LibraryManagementApplication.LibraryManagementApplication.controllers;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Response;
import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController
{
    @Autowired
    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService)
    {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book)
    {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks()

    {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id)
    {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book updatedBook)
    {
        return ResponseEntity.ok(bookService.updateBook(id, updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id)
    {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String authorId)
    {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response<List<Book>>> filterBooks(@RequestParam(required = false) String title)
    {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.filterBooks(title);
            // Success response (handled by GlobalExceptionHandler for exceptions)
           // Response<List<Book>> response = new Response<>();
            response.setSuccess(true);
            response.setMessage("Books filtered successfully.");
           // response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            //return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (CustomExceptions.ResourceNotFoundException e)
        {
            System.out.println("No books found with the title: " + title);
            throw new CustomExceptions.ResourceNotFoundException("No books found with the title: " + title);

        }
        catch(CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException ("Invalid request parameters: " + e.getMessage());
            //response.setError(Ba);

        }

        catch (CustomExceptions.InternalServerException e)
        {
            throw new CustomExceptions.InternalServerException("An unexpected error occurred while filtering books.");
        }
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getHttpErrorCode()));
    }
}

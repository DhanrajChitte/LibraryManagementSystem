package com.LibraryManagementApplication.LibraryManagementApplication.controllers;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Response;
import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.BookRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;


    public BookController(BookServiceImpl bookService)
    {
        this.bookService = bookService;
    }


    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('AUTHOR')")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<Response<Book>> createBook(@Valid @RequestBody Book book) {
        Response<Book> response = new Response<>();

            Book createBook = bookService.createBook(book);

            response.setSuccess(true);
            response.setMessage("Book added to the library successfully");
            response.setData(createBook);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    public ResponseEntity<Response<List<Book>>> getAllBooks() {
        Response<List<Book>> response = new Response<>();

            List<Book> books = bookService.getAllBooks();
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Books retrieved successfully.");
            response.setData(books);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    public ResponseEntity<Response<Book>> getBookById(@PathVariable String id) {
        Response<Book> response = new Response<>();

            Book book = bookService.getBookById(id);
            response.setSuccess(true);
            response.setMessage("Book retrieved successfully.");
            response.setData(book);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<Response<Book>> updateBook(@PathVariable String id, @Valid @RequestBody Book book) {
        Response<Book> response = new Response<>();

            Book updatebook = bookService.updateBook(id, book);


            response.setSuccess(true);
            response.setMessage("Book Information updated successfully.");
            response.setData(updatebook);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public ResponseEntity<Response<Void>> deleteBook(@PathVariable String id) {
        Response<Void> response = new Response<>();

            bookService.deleteBook(id);

            response.setSuccess(true);
            response.setMessage("Book deleted successfully.");
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.OK);

    }



    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR','USER')")
    public ResponseEntity<Response<List<Book>>> filterBooks
            (@RequestParam(required = false) String title,
             @RequestParam(required = false) String genre,
             @RequestParam(required = false) Integer publishedYear,
             @RequestParam(required = false) Integer startYear,
             @RequestParam(required = false) Integer endYear,
             @RequestParam(required = false, defaultValue = "title") String sortBy,
             @RequestParam(required = false, defaultValue = "asc") String sortOrder,
             @RequestParam(required = false, defaultValue = "4") int limit,
             @RequestParam(required = false, defaultValue = "0") int offset) {
        Response<List<Book>> response = new Response<>();

        List<Book> books=bookService.filterBooks(title,genre,publishedYear,startYear,endYear);

        books=bookService.getSortedOrderBooks(books,sortBy,sortOrder);

        // Apply pagination (limit and offset)
            int fromIndex = Math.min(offset, books.size());
            int toIndex = Math.min(offset + limit, books.size());
            books = books.subList(fromIndex, toIndex);

        response.setSuccess(true);
        response.setHttpErrorCode(HttpStatus.OK.value());
        response.setMessage("Books filtered successfully by the given filter criteria");
        response.setData(books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}




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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;


    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }


    @PostMapping
    public ResponseEntity<Response<Book>> createBook(@Valid @RequestBody Book book) {
        Response<Book> response = new Response<>();
        try {
            // Directly call the service method
            Book createBook = bookService.createBook(book);

            response.setSuccess(true);
            response.setMessage("Book added to the library successfully");
            response.setData(createBook);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.BadRequestException e) {
            throw new CustomExceptions.BadRequestException("Book Title must required");
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("Book with ID " + book.getId() + " already exists.");
        }
    }

    @GetMapping
    public ResponseEntity<Response<List<Book>>> getAllBooks() {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.getAllBooks();
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Books retrieved successfully.");
            response.setData(books);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("No Books Found in the System");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Book>> getBookById(@PathVariable String id) {
        Response<Book> response = new Response<>();
        try {
            //Call the service method
            Book book = bookService.getBookById(id);
            response.setSuccess(true);
            response.setMessage("Book retrieved successfully.");
            response.setData(book);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("No Book Find With given ID");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Book>> updateBook(@PathVariable String id, @Valid @RequestBody Book book) {
        Response<Book> response = new Response<>();
        try {
            // Call the service method
            Book updatebook = bookService.updateBook(id, book);

            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Book Information updated successfully.");
            response.setData(updatebook);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Book not found with ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteBook(@PathVariable String id) {
        Response<Void> response = new Response<>();
        try {
            // Call the service method
            bookService.deleteBook(id);
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Book deleted successfully.");
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Book not found with ID: " + id);
        }
    }

    @GetMapping("author/{authorId}")
    public ResponseEntity<Response<List<Book>>> getBooksByAuthor(@PathVariable String authorId) {
        try {
            Response<List<Book>> response = new Response<>();
            List<Book> books = bookService.getBooksByAuthor(authorId);
            // Populate the success response
            response.setSuccess(true);
            response.setMessage("Books fatched  successfully with with author ID " + authorId);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            // Handle specific exception
            throw new CustomExceptions.ResourceNotFoundException("Book not found with author ID: " + authorId);
        }
    }

    @GetMapping("/filter")
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

        List<Book> books=bookService.getAllBooks();
      /*  if (title == null && genre == null && publishedYear == null && startYear == null && endYear == null)
        {
            books = bookService.getSortedOrderBooks("title", "asc");// Sort by title in ascending order
        } */
       //else
       //{
           // books = new ArrayList<>();
           // books=bookService.getAllBooks();
            if (title != null)
            {
                books = bookService.filterBooksByTitle(title); // Call title filter logic
            }
            if (genre != null) {
                books = bookService.filterBooksByGenre(genre); // Call genre filter logic
            }

            if (publishedYear != null) {
                books = bookService.filterBooksByYear(publishedYear);
            }

            if (startYear != null || endYear != null) {
                books = bookService.filterBooksByYearRange(startYear, endYear);
            }


          //  if (sortBy != null) {
             //   books = bookService.getSortedBooks(sortBy);
           // }

            if (sortBy != null || sortOrder != null) {
                books = bookService.getSortedOrderBooks(sortBy, sortOrder);
            }

          /* if (limit > 0) {
                books = bookService.getBooksLimits(limit);
            }*/

            /*if (offset > 0) {
                books = bookService.getBooksOffset(offset);
            }*/

        // Apply pagination (limit and offset)
        if (!books.isEmpty()) {
            int fromIndex = Math.min(offset, books.size());
            int toIndex = Math.min(offset + limit, books.size());
            books = books.subList(fromIndex, toIndex);
        }
       // }

        response.setSuccess(true);
        response.setHttpErrorCode(HttpStatus.OK.value());
        response.setMessage("Books filtered successfully by the given filter criteria");
        response.setData(books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}




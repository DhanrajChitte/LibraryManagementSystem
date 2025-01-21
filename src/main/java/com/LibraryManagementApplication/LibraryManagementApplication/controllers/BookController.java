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
       // try {
            // Directly call the service method
            Book createBook = bookService.createBook(book);

            response.setSuccess(true);
            response.setMessage("Book added to the library successfully");
            response.setData(createBook);
            response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
<<<<<<< HEAD
        } catch (CustomExceptions.BadRequestException e) {
=======
     /*   }
        catch (CustomExceptions.BadRequestException e) {
>>>>>>> 8d09903d79e43fdaabb2233e37d86f0b66e78fee
            throw new CustomExceptions.BadRequestException("Book Title must required");
        } catch (CustomExceptions.ResourceNotFoundException e) {
            throw new CustomExceptions.ResourceNotFoundException("Book with ID " + book.getId() + " already exists.");
        }*/
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
<<<<<<< HEAD
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
=======
    public ResponseEntity<Response<List<Book>>> filterBooks(@RequestParam(required=false) String title,
             @RequestParam(required = false) String genre) {
        Response<List<Book>> response = new Response<>();
        List<Book> books=null;

        if (title != null && !title.trim().isEmpty())
        {
            books= bookService.filterBooksByTitle(title); // Call title filter logic
            response.setMessage("Books filtered successfully by the title");
        }
        if (genre != null && !genre.trim().isEmpty())
        {
            books = bookService.filterBooksByGenre(genre); // Call genre filter logic
            response.setMessage("Books filtered successfully by the genre");
        }
        //try {
        //List<Book> books = bookService.filterBooksByTitle(title);
        // Success response (handled by GlobalExceptionHandler for exceptions)
        //Response<List<Book>> response = new Response<>();
        response.setSuccess(true);
        //response.setMessage("Books filtered successfully by the title");
        // response.setError(null);
        response.setHttpErrorCode(HttpStatus.OK.value());
        response.setData(books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
       /* }
        catch (CustomExceptions.ResourceNotFoundException e)
        {
            System.out.println("No books found with the given title"+title);
            throw new CustomExceptions.ResourceNotFoundException("No books found with the given title "+title);

        }
        catch(CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException ("Invalid request parameters: " + e.getMessage());
            //response.setError(Ba);

        }*/


    /*@GetMapping("/filterbooksbygenre")
    public ResponseEntity<Response<List<Book>>> filterBooksByGenre(@RequestParam(required=false) String genre)
    {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.filterBooksByGenre(genre);//conditions here
            // Success response (handled by GlobalExceptionHandler for exceptions)
            //Response<List<Book>> response = new Response<>();
            response.setSuccess(true);
            response.setMessage("Books filtered successfully by the genre");
            // response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (CustomExceptions.ResourceNotFoundException e)
        {
            System.out.println("No books found with the given genre" +genre);
            throw new CustomExceptions.ResourceNotFoundException("No books found with the given genre " + genre);

        }
        catch(CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException ("Invalid request parameters: " + e.getMessage());
            //response.setError(Ba);

        }
    }*/

    @GetMapping("/filterbooksbypublishedyear")
    public ResponseEntity<Response<List<Book>>> filterBooksByPublishedYear(@RequestParam(required=false) Integer publishedYear) {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.filterBooksByYear(publishedYear);
            // Success response (handled by GlobalExceptionHandler for exceptions)
            //Response<List<Book>> response = new Response<>();
            response.setSuccess(true);
            response.setMessage("Books filtered successfully by the published year");
            // response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomExceptions.ResourceNotFoundException e) {
            //System.out.println("No books found with the given genre" +genre);
            throw new CustomExceptions.ResourceNotFoundException("No books found with the given published year " + publishedYear);

        } catch (CustomExceptions.BadRequestException e) {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
            //response.setError(Ba);

        }

    }

    @GetMapping("/filterBooksByYearRange")
    public ResponseEntity<Response<List<Book>>> filterBooksByYearRange(@RequestParam(required=false) Integer startYear,
                             @RequestParam (required = false) Integer endYear) {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.filterBooksByYearRange(startYear,endYear);
            // Success response (handled by GlobalExceptionHandler for exceptions)
            //Response<List<Book>> response = new Response<>();
            response.setSuccess(true);
            response.setMessage("Books filtered successfully by the given start and end year");
            // response.setError(null);
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (CustomExceptions.ResourceNotFoundException e) {
            //System.out.println("No books found with the given genre" +genre);
            throw new CustomExceptions.ResourceNotFoundException("No books found with the given startYear: " + startYear + "and endYear:" +endYear);

        } catch (CustomExceptions.BadRequestException e) {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
            //response.setError(Ba);

        }
    }

    @GetMapping("/bookssorted")
    public ResponseEntity<Response<List<Book>>> getBooks(@RequestParam(required = false, defaultValue = "title") String sortBy)
    {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.getSortedBooks(sortBy);
            response.setSuccess(true);
            response.setMessage("Books retrieved and sorted successfully");
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(CustomExceptions.ResourceNotFoundException e)
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found: " + e.getMessage());
        }

        catch(CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
        }
    }


    @GetMapping("/bookssortorder")
    public ResponseEntity<Response<List<Book>>> getSortedOrderBooks(@RequestParam(required = false, defaultValue = "title") String sortBy,
    @RequestParam(required = false, defaultValue = "asc") String sortOrder)
    {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.getSortedOrderBooks(sortBy,sortOrder);
            response.setSuccess(true);
            response.setMessage("Books retrieved and sorted successfully");
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(CustomExceptions.ResourceNotFoundException e)
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found: " + e.getMessage());
        }

        catch(CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
        }

    }

    @GetMapping("/booksbylimit")
    public ResponseEntity<Response<List<Book>>> getBooksLimits(
            @RequestParam(required = false, defaultValue = "4") int limit) {
        Response<List<Book>> response = new Response<>();
        try {
            List<Book> books = bookService.getBooksLimits(limit);
            response.setSuccess(true);
            response.setMessage("Books retrieved successfully");
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
        }

        catch(CustomExceptions.ResourceNotFoundException e)
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found in the database");
        }
    }

    @GetMapping("/booksbyoffset")
    public ResponseEntity<Response<List<Book>>> getBooksOffset(
            @RequestParam(required = false, defaultValue = "0") int offset) {
        Response<List<Book>> response = new Response<>();
//        try {
            List<Book> books = bookService.getBooksOffset(offset);
            response.setSuccess(true);
            response.setMessage("Books retrieved successfully");
            response.setHttpErrorCode(HttpStatus.OK.value());
            response.setData(books);
            return new ResponseEntity<>(response, HttpStatus.OK);
   /*     }
        catch (CustomExceptions.BadRequestException e)
        {
            throw new CustomExceptions.BadRequestException("Invalid request parameters: " + e.getMessage());
        }

        catch(CustomExceptions.ResourceNotFoundException e)
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found in the database");
>>>>>>> 8d09903d79e43fdaabb2233e37d86f0b66e78fee
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




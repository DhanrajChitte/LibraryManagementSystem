package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.AuthorRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.BookRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book)
    {
        boolean authorExists=authorRepository.existsById(book.getAuthorId());

        if(!authorExists)
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID " +book.getAuthorId() + " does not exits in the Author Collection ");
        }

        if (book == null || book.getTitle() == null || book.getTitle().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Book title must be required");
        }


        if (bookRepository.existsById(book.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Book with ID" + book.getId() + " already exists.");
        }

        //Fetch the currently logged in User
        //Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        //String loggedInUsername=authentication.getName();

       // Author author = authorRepository.findById(book.getAuthorId())
             //   .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Author not found."));

       //check if the logged in user is either admin or the author of the book

       // if (!authentication.getAuthorities().stream().anyMatch
            //    (grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            // Only allow the author of the book or admin to add this book
            //if (!author.getUserInfo().getName().equals(loggedInUsername)) {
              //  throw new CustomExceptions.ForbiddenException("You can only create books for your own authorship.");
           // }
        //}

        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books Found in the System");
        }
        return books;
    }

    @Override
    public Book getBookById(String id) {
        System.out.println("No books found with the given id:" + id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Book with ID " + id + " not found."));
    }

    @Override
    public Book updateBook(String id, Book book) {
        // Fetch the existing book or throw an exception if not found
        Book existingBook = getBookById(id);

       boolean authorExists=authorRepository.existsById(book.getAuthorId());

       if(!authorExists)
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID " +book.getAuthorId() + " does not exits in the Author Collection ");
        }

        // Update only the modified fields
        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            existingBook.setTitle(book.getTitle());
        }

        if (book.getAuthorId() != null && !book.getAuthorId().isEmpty()) {
            existingBook.setAuthorId(book.getAuthorId());
        }

        if (book.getGenre() != null && !book.getGenre().isEmpty()) {
            existingBook.setGenre(book.getGenre());
        }

        if (book.getPublishedYear() != null) {
            existingBook.setPublishedYear(book.getPublishedYear());
        }

        // Save and return the updated author
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }



    @Override
    public List<Book> filterBooks(String title, String genre, Integer publishedYear, Integer startYear, Integer endYear) {
        List<Book> books = bookRepository.findAll();

        if (title != null) {
            books = filterBooksByTitle(books, title);
        }

        if (genre != null) {
            books = filterBooksByGenre(books, genre);
        }

        if (publishedYear != null) {
            books = filterBooksByYear(books, publishedYear);
        }

        if (startYear != null || endYear != null) {
            books = filterBooksByYearRange(books, startYear, endYear);
        }
        return books;
    }


    @Override
    public List<Book> filterBooksByTitle(List<Book> books, String title) {

        if (title == null || title.trim().isEmpty()) {
            throw new CustomExceptions.BadRequestException("The title parameter cannot be empty.");
        }

        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.trim().toLowerCase()))
                .collect(Collectors.toList());

        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the title: " + title);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> filterBooksByGenre(List<Book> books, String genre) {

        if (genre == null || genre.trim().isEmpty()) {
            throw new CustomExceptions.BadRequestException("The genre parameter cannot be empty.");
        }

        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getGenre().toLowerCase().contains(genre.trim().toLowerCase()))
                .collect(Collectors.toList());

        // Throw exception if no books match the genre
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the genre: " + genre);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> filterBooksByYear(List<Book> books, Integer publishedYear) {
        // If published year not give then filter books fix this bug
        if (publishedYear == null) {
            throw new CustomExceptions.BadRequestException("The published year parameter cannot be null");
        }

        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getPublishedYear() != null && book.getPublishedYear().equals(publishedYear))
                .collect(Collectors.toList());

        // Throw exception if no books match the genre
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the published year: " + publishedYear);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> filterBooksByYearRange(List<Book> books, Integer startYear, Integer endYear) {

        if (startYear != null && endYear != null && startYear > endYear) {
            throw new CustomExceptions.BadRequestException("The startYear cannot be greter than a endYear");
        }

        List<Book> filteredBooks = books.stream()
                .filter(book -> (startYear == null || book.getPublishedYear() >= startYear) &&
                        (endYear == null || book.getPublishedYear() <= endYear))
                .collect(Collectors.toList());

        // Throw exception if no books match the genre
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the provided startYear: " + startYear + " endYear " + endYear);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> getSortedOrderBooks(List<Book> books, String sortBy, String sortOrder) {

        if (!sortBy.equalsIgnoreCase("title") && !sortBy.equalsIgnoreCase("publishedYear")) {
            throw new CustomExceptions.BadRequestException("Invalid sortBy parameter: " + sortBy);
        }

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new CustomExceptions.BadRequestException("Invalid sortOrder parameter: " + sortOrder);
        }

        if (books == null || books.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books available to sort.");
        }

        Comparator<Book> comparator;
        if ("publishedYear".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Book::getPublishedYear);
        } else if ("title".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Book::getTitle);
        } else {
            throw new IllegalArgumentException("Invalid sortBy parameter");
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return books.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

    }

}

    /*@Override
    public List<Book> getBooksLimits(int limit)
    {
        if (limit<=0)
        {
            throw new CustomExceptions.BadRequestException("Limit must be greater than 0");
        }

        List<Book> books = bookRepository.findAll();

        if (books.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found in the database");
        }

        // Limit the results
        return books.stream().limit(limit).collect(Collectors.toList());
    }

   @Override
    public List<Book> getBooksOffset(int offset)
    {
        if (offset < 0)
        {
            throw new CustomExceptions.BadRequestException("Offset cannot be negative");
        }

        List<Book> books = bookRepository.findAll();

        if (books.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found in the database");
        }

        // Limit the results
        return books.stream().skip(offset).collect(Collectors.toList());
    }*/







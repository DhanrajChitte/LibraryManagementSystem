package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.BookRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService
{
    @Autowired
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository)

    {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book)
    {
        if(book == null || book.getTitle() == null || book.getTitle().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Book title must be required");
        }
        if (bookRepository.existsById(book.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Book with ID" + book.getId() + " already exists.");
        }
        return bookRepository.save(book);
    }

    @Override
    public List <Book> getAllBooks()
    {
        List<Book> books=bookRepository.findAll();
        if(books.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No books Found in the System");
        }
        return books;
    }

    @Override
    public Book getBookById(String id)
    {
        System.out.println("No books found with the given id:" +id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Book with ID " + id + " not found."));
    }

    @Override
    public Book updateBook(String id, Book book)
    {
        // Fetch the existing book or throw an exception if not found
        Book existingBook = getBookById(id);

        // Update only the modified fields
        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            existingBook.setTitle(book.getTitle());
        }

        if (book.getAuthorId() != null && !book.getAuthorId().isEmpty()) {
            existingBook.setAuthorId(book.getAuthorId());
        }

        if (book.getGenre() != null && !book.getGenre().isEmpty())
        {
            existingBook.setGenre(book.getGenre());
        }

        if (book.getPublishedYear()!=null)
        {
            existingBook.setPublishedYear(book.getPublishedYear());
        }

        // Save and return the updated author
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String id)
    {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public List<Book> getBooksByAuthor(String authorId)
    {
        List<Book>book=bookRepository.findByAuthorId(authorId);
        if(book.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("Book not found with author ID: " + authorId);
        }
        return book;
        //return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Book> filterBooksByTitle(String title)
    {

        // Check if the title parameter is empty or invalid
        //if (title == null || title.trim().isEmpty()) {
          //  throw new CustomExceptions.BadRequestException("The title parameter cannot be empty.");
        //}

        // Fetch all books and filter by title
        List<Book> books = bookRepository.findAll();

        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.trim().toLowerCase()))
                .collect(Collectors.toList());

        // Throw exception if no books match the title
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the title: " + title);
        }

        if (title == null || title.trim().isEmpty()) {
              throw new CustomExceptions.BadRequestException("The title parameter cannot be empty.");
            }

        return filteredBooks;
    }

    @Override
    public List<Book> filterBooksByGenre(String genre) {

        if (genre == null || genre.trim().isEmpty()) {
            throw new CustomExceptions.BadRequestException("The genre parameter cannot be empty.");
        }

        // Fetch all books and filter by genre
        List<Book> books = bookRepository.findAll();

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
    public List<Book> filterBooksByYear(Integer publishedYear)
    {

        if (publishedYear == null) {
            throw new CustomExceptions.BadRequestException("The published year parameter cannot be null");
        }

        // Fetch all books and filter by genre
        List<Book> books = bookRepository.findAll();

        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getPublishedYear()!=null && book.getPublishedYear().equals( publishedYear))
                .collect(Collectors.toList());

        // Throw exception if no books match the genre
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the published year: " + publishedYear);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> filterBooksByYearRange(Integer startYear,Integer endYear)
    {

        if (startYear !=null && endYear!=null && startYear>endYear) {
            throw new CustomExceptions.BadRequestException("The startYear cannot be greter than a endYear");
        }

        // Fetch all books and filter by genre
        List<Book> books = bookRepository.findAll();

        List<Book> filteredBooks = books.stream()
                .filter(book -> (startYear==null || book.getPublishedYear()>=startYear) &&
                        (endYear==null || book.getPublishedYear()<=endYear))
                .collect(Collectors.toList());

        // Throw exception if no books match the genre
        if (filteredBooks.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No books found with the provided startYear:" + startYear + "endYear" +endYear);
        }

        return filteredBooks;
    }

    @Override
    public List<Book> getSortedBooks(String sortBy) {
        // Fetch all books
        List<Book> books = bookRepository.findAll();

        // Check if no books are found
        if (books.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No books found in the database.");
        }

        // Apply sorting: Default is by title, otherwise sort by publishedYear if specified
        if (sortBy == null || sortBy.isEmpty() || sortBy.equalsIgnoreCase("title")) {
            books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        }
        else if (sortBy.equalsIgnoreCase("publishedYear"))
        {
            books.sort(Comparator.comparing(Book::getPublishedYear));
        }
        else
        {
            // If sortBy is invalid, throw BadRequestException
            throw new CustomExceptions.BadRequestException("Invalid sortBy parameter: " + sortBy);
        }

        return books;
    }

    @Override
    public List<Book> getSortedOrderBooks(String sortBy,String sortOrder)
    {
      if(sortBy==null || sortBy.isEmpty())
      {
          sortBy= "title";
      }

      else if (!sortBy.equalsIgnoreCase("title") && !sortBy.equalsIgnoreCase("publishedYear")) {
          throw new CustomExceptions.BadRequestException("Invalid sortBy parameter: " + sortBy);
      }

      if(sortOrder==null || sortOrder.isEmpty())
      {
          sortOrder="asc";
      }

      List<Book> books=bookRepository.findAll();

      if(books.isEmpty())
      {
          throw new CustomExceptions.ResourceNotFoundException("No books find in the database");
      }

      Comparator<Book>comparator;
      switch(sortBy.toLowerCase())
      {
          case "publishedyear":
              comparator = Comparator.comparing(Book::getPublishedYear);
              break;
          default:
              comparator = Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
      }

        // Apply sortOrder
        if (sortOrder.equalsIgnoreCase("desc"))
        {
            comparator = comparator.reversed();
        }
        else if (!sortOrder.equalsIgnoreCase("asc"))
        {
            throw new CustomExceptions.BadRequestException("Invalid sortOrder parameter: " + sortOrder);
        }

        // Sort the books
        books.sort(comparator);
        return books;
      }

    @Override
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
    }

}





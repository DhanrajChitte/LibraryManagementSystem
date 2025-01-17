package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.BookRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;
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
        System.out.println("No books found with the given id:"+id);
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
    public List<Book> filterBooks(String title,String genre)
    {


        List<Book> books = bookRepository.findAll();

        // Filter logic
        List<Book> filteredBooks = books.stream()
                .filter(book -> {
                    boolean matchesTitle = true;
                    boolean matchesGenre = true;
                    if (title !=null && !title.trim().isEmpty())
                    {
                     matchesTitle= book.getTitle().toLowerCase().contains(title.trim().toLowerCase());
                    }

                    if (genre !=null && !genre.trim().isEmpty())
                    {
                        matchesGenre= book.getGenre().toLowerCase().contains(genre.trim().toLowerCase());
                    }

                   return matchesTitle || matchesGenre;
                }).collect(Collectors.toList());


        // Check if filtered list is empty
        if (filteredBooks.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No books find with the given criteria");
        }
            //throw new CustomExceptions.InternalServerException("An unexpected error occurred while filtering books.");
        return filteredBooks;
    }
}

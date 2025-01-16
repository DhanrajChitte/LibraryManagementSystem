package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
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
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks()
    {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(String id)
    {
        System.out.println("No books found with the given id:"+id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Book with ID " + id + " not found."));
    }

    @Override
    public Book updateBook(String id, Book updatedBook)
    {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthorId(updatedBook.getAuthorId());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPublishedYear(updatedBook.getPublishedYear());
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
        return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Book> filterBooks(String title)
    {
        if (title == null || title.trim().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Title cannot be null or empty.");
        }

        List<Book> books = bookRepository.findAll();

        // Filter logic
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());


        // Check if filtered list is empty
        if (filteredBooks.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("Book with title " + title + " not found.");
        }

            //throw new CustomExceptions.InternalServerException("An unexpected error occurred while filtering books.");
        return filteredBooks;
    }
}

package com.LibraryManagementApplication.LibraryManagementApplication.services;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;

import java.util.List;

public interface BookService
{
    public Book createBook(Book book);

    public List<Book> getAllBooks();

    public Book getBookById(String id);

    public Book updateBook(String id, Book updatedBook);

    public void deleteBook(String id);

    public List<Book> getBooksByAuthor(String authorId);

    List<Book> filterBooks(String title);
}

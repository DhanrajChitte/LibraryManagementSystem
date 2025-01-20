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

    List<Book> filterBooksByTitle(String title);

    List<Book> filterBooksByGenre(String genre);

    List<Book> filterBooksByYear(Integer publishedYear);

    List<Book> filterBooksByYearRange(Integer startYear,Integer endYear);

    List<Book> getSortedBooks(String sortBy);

    List<Book> getSortedOrderBooks(String sortBy,String sortOrder);

    List<Book> getBooksLimits(int limit);

    List<Book> getBooksOffset(int offset);

    //List<Book> getBooksLimitOffset(int limit,int offset);

}

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



    List<Book> filterBooksByTitle(List<Book>books,String title);

    List<Book> filterBooksByGenre(List<Book>books,String genre);

    List<Book> filterBooksByYear(List<Book>books,Integer publishedYear);

    List<Book> filterBooksByYearRange(List<Book> books,Integer startYear,Integer endYear);



    List<Book> getSortedOrderBooks(List<Book> books,String sortBy,String sortOrder);


    List<Book> filterBooks(String title,String genre,Integer publishedYear,Integer startYear,Integer endYear);



}

package com.LibraryManagementApplication.LibraryManagementApplication.services;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;

import java.util.List;

public interface AuthorService
{
    public Author createAuthor(Author author);

    public List<Author> getAllAuthors();

    public Author getAuthorById(String id);

    public Author updateAuthor(String id, Author author);

    public void deleteAuthor(String id);
}

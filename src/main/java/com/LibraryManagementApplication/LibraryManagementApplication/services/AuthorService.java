package com.LibraryManagementApplication.LibraryManagementApplication.services;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;

import java.util.List;

public interface AuthorService
{
    public Author createAuthor(Author author);

    public List<Author> getAllAuthors();

    public Author getAuthorById(String id);

    public Author updateAuthor(String id, Author author);

    public void deleteAuthor(String id);

    public String addUser(UserInfo userInfo);

    public String generateToken(String username);

    public String extractUsername(String token);
}

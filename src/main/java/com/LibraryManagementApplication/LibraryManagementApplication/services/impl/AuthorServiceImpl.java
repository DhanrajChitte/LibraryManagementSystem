package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.DataNotFoundException;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.InvalidAuthorDataException;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.AuthorRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService
{
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author createAuthor(Author author) {
        if (author == null || author.getName() == null || author.getName().isEmpty())
        {
            throw new CustomExceptions.BadRequestException("Author details are invalid.");
        }

        if (authorRepository.existsById(author.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID " + author.getId() + " already exists.");
        }
        try
        {
            return authorRepository.save(author);
        }
        catch (Exception ex) {
            throw new CustomExceptions.InternalServerException("Error occurred while saving the author: " + ex.getMessage());
        }
    }


  /*@Override
    public Author createAuthor(Author author)
    {
        // Check if the author already exists (based on some business rule)
        if (authorRepository.existsById(author.getId())) {
            throw new CustomExceptions.ResourceNotFoundException ("Author with ID already exists.");
        }
        return authorRepository.save(author);
    }*/

   @Override
    public List <Author> getAllAuthors()
    {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(String id)
    {
        return authorRepository.findById(id).orElseThrow(()->
                new CustomExceptions.ResourceNotFoundException("Author not found with ID: " + id));
    }

    @Override
    public Author updateAuthor(String id, Author author)
    {
        Author existingAuthor = getAuthorById(id);
        existingAuthor.setName(author.getName());
        existingAuthor.setBio(author.getBio());
        return authorRepository.save(existingAuthor);
    }

    @Override
    public void deleteAuthor(String id)
    {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }
}

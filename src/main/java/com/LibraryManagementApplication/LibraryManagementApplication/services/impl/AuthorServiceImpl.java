package com.LibraryManagementApplication.LibraryManagementApplication.services.impl;


import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.CustomExceptions;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.DataNotFoundException;
//import com.LibraryManagementApplication.LibraryManagementApplication.exceptions.InvalidAuthorDataException;
import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.AuthorRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.repositories.UserInfoRepository;
import com.LibraryManagementApplication.LibraryManagementApplication.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService
{
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Author createAuthor(Author author)
    {
        if (author == null || author.getName() == null || author.getName().isEmpty())
        {
            //System.out.println("Author name must be required");
            throw new CustomExceptions.BadRequestException("Author details are invalid.");
        }

        if (authorRepository.existsById(author.getId()))
        {
            throw new CustomExceptions.ResourceNotFoundException("Author with ID" + author.getId() + " already exists.");
        }
            return authorRepository.save(author);
    }

   @Override
    public List <Author> getAllAuthors()
    {
        List<Author> authors=authorRepository.findAll();
        if(authors.isEmpty())
        {
            throw new CustomExceptions.ResourceNotFoundException("No Authors Found in the System");
        }
        return authors;
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
        // Fetch the existing author or throw an exception if not found
        Author existingAuthor = getAuthorById(id);

        // Update only the modified fields
        if (author.getName() != null && !author.getName().isEmpty()) {
            existingAuthor.setName(author.getName());
        }
        if (author.getBio() != null && !author.getBio().isEmpty()) {
            existingAuthor.setBio(author.getBio());
        }
        // Save and return the updated author
        return authorRepository.save(existingAuthor);
    }


    @Override
    public void deleteAuthor(String id)
    {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }


    public String addUser(UserInfo userInfo)
    {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added to the System";
    }
}

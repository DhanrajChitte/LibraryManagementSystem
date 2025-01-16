package com.LibraryManagementApplication.LibraryManagementApplication.repositories;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends MongoRepository<Author,String>
{

}

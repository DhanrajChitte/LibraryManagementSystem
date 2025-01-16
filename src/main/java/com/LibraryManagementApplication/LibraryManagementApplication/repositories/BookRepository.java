package com.LibraryManagementApplication.LibraryManagementApplication.repositories;

import com.LibraryManagementApplication.LibraryManagementApplication.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book,String>
{
    List<Book> findByAuthorId(String authorId);
}

package com.LibraryManagementApplication.LibraryManagementApplication.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.PrePersist;
import java.util.UUID;

@Document(collection="books")
public class Book
{
    @Id
    private String id;


    private String title;


    private String authorId ;

    private String genre;

    private Integer publishedYear;

    public Book()
    {

        this.id = UUID.randomUUID().toString(); // Auto-generate ID
    }

    public Book(String id,String title,String authorId,String genre,Integer publishedYear)
    {
        this.id = id;
        this.title=title;
        this.authorId=authorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
}


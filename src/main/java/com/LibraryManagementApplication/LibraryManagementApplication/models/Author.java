package com.LibraryManagementApplication.LibraryManagementApplication.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.PrePersist;
import java.util.UUID;

    @Document(collection="authors")
    public class Author
    {
        @Id
        private String id;


        private String name;

        private String bio;

        public Author()
        {
            this.id = UUID.randomUUID().toString(); // Auto-generate ID
        }

        public Author(String id,String name,String bio)
        {
            this.id = id;
            this.name=name;
            this.bio=bio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }


    }


package com.LibraryManagementApplication.LibraryManagementApplication.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UserInfo")
public class UserInfo
{
    @Id
    private String id;

    private String name;

    private String password;

    @Indexed(unique = true)
    private String email;

    private String roles;

    public UserInfo(String id,String name,String password,String email,String roles)
    {
        this.id = id;
        this.name=name;
        this.password=password;
        this.email=email;
        this.roles=roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}

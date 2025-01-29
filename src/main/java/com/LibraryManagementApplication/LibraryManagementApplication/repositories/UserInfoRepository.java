package com.LibraryManagementApplication.LibraryManagementApplication.repositories;

import com.LibraryManagementApplication.LibraryManagementApplication.models.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,String>
{
    //Declare a method to find a user by their name
    Optional<UserInfo> findByEmail(String email);
}

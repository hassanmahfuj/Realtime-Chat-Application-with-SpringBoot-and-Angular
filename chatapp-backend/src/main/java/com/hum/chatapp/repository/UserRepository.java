package com.hum.chatapp.repository;

import com.hum.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userId <> ?1")
    List<User> findAllUsersExceptThisUserId(int userId);
}

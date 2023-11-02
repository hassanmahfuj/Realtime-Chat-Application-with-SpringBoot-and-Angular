package com.hum.chatapp.dao;

import com.hum.chatapp.entity.User;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing user-related operations.
 */
public interface UserDao {

    /**
     * Save a user in the database.
     *
     * @param user The user to be saved.
     * @return The saved user.
     */
    User saveUser(User user);

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return The found user, or throw UserNotFoundException if not found.
     */
    User findUserByEmail(String email);

    /**
     * Retrieve a list of all users in the database.
     *
     * @return A list of all users.
     */
    List<User> findAllUsers();

    /**
     * Retrieve a list of all users except the user with the specified userId.
     *
     * @param userId The userId to exclude from the list.
     * @return A list of users excluding the specified user.
     */
    List<User> findAllUsersExceptThisUserId(int userId);

    /**
     * Find the conversation ID between two users based on their user IDs.
     *
     * @param user1 The user ID of the first user.
     * @param user2 The user ID of the second user.
     * @return The conversation ID between the two users, or insert a new one if not found.
     */
    int findConversationIdByUser1IdAndUser2Id(int user1, int user2);
}

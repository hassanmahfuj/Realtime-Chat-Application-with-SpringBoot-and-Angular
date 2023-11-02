package com.hum.chatapp.service;

import com.hum.chatapp.dto.ApiResponse;
import com.hum.chatapp.entity.User;
import org.springframework.http.ResponseEntity;

/**
 * An interface for user-related operations and services.
 */
public interface UserService {

    /**
     * Save a user to the system.
     *
     * @param user The User object representing the user to be saved.
     * @return ResponseEntity containing an ApiResponse indicating the result of the operation.
     */
    ResponseEntity<ApiResponse> saveUser(User user);

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to be found.
     * @return ResponseEntity containing an ApiResponse with the user information if found.
     */
    ResponseEntity<ApiResponse> findUserByEmail(String email);

    /**
     * Retrieve a list of all users in the system.
     *
     * @return ResponseEntity containing an ApiResponse with a list of User objects representing all users.
     */
    ResponseEntity<ApiResponse> findAllUsers();

    /**
     * Retrieve a list of all users except the user with a specific user ID.
     *
     * @param userId The ID of the user to be excluded from the list.
     * @return ResponseEntity containing an ApiResponse with a list of User objects representing all users except the specified user.
     */
    ResponseEntity<ApiResponse> findAllUsersExceptThisUserId(int userId);

    /**
     * Find or create a conversation ID for a pair of users based on their user IDs.
     *
     * @param user1Id The ID of the first user in the conversation.
     * @param user2Id The ID of the second user in the conversation.
     * @return ResponseEntity containing an ApiResponse with the conversation ID for the user pair.
     */
    ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(int user1Id, int user2Id);
}

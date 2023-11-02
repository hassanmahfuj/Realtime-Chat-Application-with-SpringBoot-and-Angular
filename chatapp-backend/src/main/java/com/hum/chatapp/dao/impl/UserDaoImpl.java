package com.hum.chatapp.dao.impl;

import com.hum.chatapp.dao.UserDao;
import com.hum.chatapp.exception.InternalServerErrorException;
import com.hum.chatapp.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hum.chatapp.entity.User;
import com.hum.chatapp.config.JdbcDataSource;

/**
 * Implementation of the UserDao interface for managing user-related operations.
 */
@RequiredArgsConstructor
@Service
public class UserDaoImpl implements UserDao {
    private PreparedStatement pst;
    private final JdbcDataSource db;

    /**
     * Saves a new user to the database.
     *
     * @param user The user object to be saved.
     * @return The user object with the generated user ID.
     * @throws InternalServerErrorException if there is an internal server error during the operation.
     */
    public User saveUser(User user) {
        try {
            pst = db.get().prepareStatement("INSERT INTO user (first_name, last_name, email) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            int x = pst.executeUpdate();
            if (x != -1) {
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                }
            }
            return user;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Find a user by their email in the database.
     *
     * @param email The email of the user to find.
     * @return The User object if found.
     * @throws UserNotFoundException if the user is not found.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    public User findUserByEmail(String email) {
        try {
            pst = db.get().prepareStatement("SELECT user_id, first_name, last_name, email FROM user WHERE email = ?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));
                rs.close();
                return user;
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Retrieve a list of all users from the database.
     *
     * @return List of User objects representing all users.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    public List<User> findAllUsers() {
        try {
            List<User> list = new ArrayList<>();
            pst = db.get().prepareStatement("SELECT user_id, first_name, last_name, email FROM user");
            ResultSet rs = pst.executeQuery();
            User user;
            while (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));

                list.add(user);
            }
            rs.close();
            return list;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Retrieve a list of all users except the user with the given user ID.
     *
     * @param userId The user ID to exclude from the list.
     * @return List of User objects representing users.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    public List<User> findAllUsersExceptThisUserId(int userId) {
        try {
            List<User> list = new ArrayList<>();
            pst = db.get().prepareStatement("SELECT user_id, first_name, last_name, email FROM user WHERE user_id != ?");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            User user;
            while (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));

                list.add(user);
            }
            rs.close();
            return list;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Find a conversation ID between two users by their user IDs or create a new
     * conversation if none exists.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     * @return The ID of the conversation between the two users.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public int findConversationIdByUser1IdAndUser2Id(int user1, int user2) {
        try {
            int conversationId = -1;
            pst = db.get().prepareStatement(
                    """
                            SELECT conversation_id
                            FROM conversation
                            WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)
                            """
            );
            pst.setInt(1, user1);
            pst.setInt(2, user2);
            pst.setInt(3, user2);
            pst.setInt(4, user1);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                conversationId = rs.getInt(1);
            } else {
                pst = db.get().prepareStatement(
                        "INSERT INTO conversation (user1_id, user2_id) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, user1);
                pst.setInt(2, user2);
                int x = pst.executeUpdate();
                if (x != -1) {
                    ResultSet generatedKeys = pst.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        conversationId = generatedKeys.getInt(1);
                    }
                    generatedKeys.close();
                }
            }
            rs.close();
            return conversationId;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

package com.hum.chatapp.dao.impl;

import com.hum.chatapp.config.JdbcDataSource;
import com.hum.chatapp.dao.MessageDao;
import com.hum.chatapp.dto.*;
import com.hum.chatapp.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the MessageDao interface that interacts with the database to
 * perform message-related operations.
 */
@RequiredArgsConstructor
@Service
public class MessageDaoImpl implements MessageDao {
    private PreparedStatement pst;
    private final JdbcDataSource db;

    /**
     * Retrieve a list of conversations associated with a specific user by their user ID.
     *
     * @param userId The ID of the user for whom to find conversations.
     * @return A list of ConversationResponse objects representing the user's conversations.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public List<ConversationResponse> findConversationsByUserId(int userId) {
        try {
            List<ConversationResponse> list = new ArrayList<>();
            pst = db.get().prepareStatement(
                    """
                            SELECT
                            	C.conversation_id,
                                U.user_id AS receiver_id,
                            	CONCAT(U.first_name, ' ', U.last_name) AS receiver_name,
                            	M.message,
                            	M.timestamp
                            FROM conversation AS C
                                                        
                            INNER JOIN User AS U
                            ON (C.user1_id = U.user_id OR C.user2_id = U.user_id) AND U.user_id != ?
                                                        
                            LEFT JOIN (
                                SELECT
                                    conversation_id,
                                    (SELECT message FROM Message M2 WHERE M2.conversation_id = M.conversation_id ORDER BY M2.timestamp DESC LIMIT 1) AS message,
                                    MAX(timestamp) AS timestamp
                                FROM Message M
                                GROUP BY conversation_id
                            ) AS M
                            ON C.conversation_id = M.conversation_id
                                                        
                            WHERE C.user1_id = ? OR C.user2_id = ?
                            ORDER BY M.timestamp DESC;
                            """
            );
            pst.setInt(1, userId);
            pst.setInt(2, userId);
            pst.setInt(3, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(ConversationResponse.builder()
                        .conversationId(rs.getInt(1))
                        .otherUserId(rs.getInt(2))
                        .otherUserName(rs.getString(3))
                        .lastMessage(rs.getString(4))
                        .lastMessageTimestamp(rs.getString(5))
                        .build()
                );
            }
            rs.close();
            return list;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Delete a conversation by its unique conversation ID.
     *
     * @param conversationId The ID of the conversation to be deleted.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public void deleteConversationByConversationId(int conversationId) {
        try {
            pst = db.get().prepareStatement("DELETE FROM message WHERE conversation_id = ?");
            pst.setInt(1, conversationId);
            pst.executeUpdate();

            pst = db.get().prepareStatement("DELETE FROM conversation WHERE conversation_id = ?");
            pst.setInt(1, conversationId);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Save a new message to the database.
     *
     * @param msg The MessageRequest object containing the message details.
     * @return A MessageResponse object representing the saved message.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public MessageResponse saveMessage(MessageRequest msg) {
        try {
            pst = db.get().prepareStatement(
                    "INSERT INTO message (conversation_id, message, timestamp, sender_id, receiver_id) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            pst.setInt(1, msg.getConversationId());
            pst.setString(2, msg.getMessage());
            pst.setTimestamp(3, new Timestamp(msg.getTimestamp().getTime()));
            pst.setInt(4, msg.getSenderId());
            pst.setInt(5, msg.getReceiverId());
            pst.executeUpdate();
            // get generated id
            ResultSet generatedKeys = pst.getGeneratedKeys();
            int generatedId = -1;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
            generatedKeys.close();
            return MessageResponse.builder()
                    .messageId(generatedId)
                    .message(msg.getMessage())
                    .senderId(msg.getSenderId())
                    .timestamp(msg.getTimestamp())
                    .receiverId(msg.getReceiverId())
                    .build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Retrieve a list of messages within a specific conversation by its conversation ID.
     *
     * @param conversationId The ID of the conversation for which to retrieve messages.
     * @return A list of MessageResponse objects representing the conversation's messages.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public List<MessageResponse> findMessagesByConversationId(int conversationId) {
        try {
            List<MessageResponse> list = new ArrayList<>();
            pst = db.get().prepareStatement(
                    """
                            SELECT
                            	message_id,
                                sender_id,
                            	message,
                            	timestamp
                            FROM Message
                            WHERE conversation_id = ?
                            ORDER BY timestamp DESC;
                            """
            );
            pst.setInt(1, conversationId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(MessageResponse.builder()
                        .messageId(rs.getInt(1))
                        .senderId(rs.getInt(2))
                        .message(rs.getString(3))
                        .timestamp(rs.getTimestamp(4))
                        .build()
                );
            }
            rs.close();
            return list;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Delete a message by its unique message ID.
     *
     * @param messageId The ID of the message to be deleted.
     * @throws InternalServerErrorException if there is an internal server error.
     */
    @Override
    public void deleteMessageByMessageId(int messageId) {
        try {
            pst = db.get().prepareStatement("DELETE FROM message WHERE message_id = ?");
            pst.setInt(1, messageId);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

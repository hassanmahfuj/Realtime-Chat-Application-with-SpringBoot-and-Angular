package com.hum.chatapp.dao;

import com.hum.chatapp.dto.ConversationResponse;
import com.hum.chatapp.dto.MessageRequest;
import com.hum.chatapp.dto.MessageResponse;

import java.util.List;

/**
 * An interface for interacting with the database to perform message-related operations.
 */
public interface MessageDao {

    /**
     * Find conversations associated with a specific user by their user ID.
     *
     * @param userId The ID of the user for whom to find conversations.
     * @return A list of ConversationResponse objects representing the user's conversations.
     */
    List<ConversationResponse> findConversationsByUserId(int userId);

    /**
     * Delete a conversation by its unique conversation ID.
     *
     * @param conversationId The ID of the conversation to be deleted.
     */
    void deleteConversationByConversationId(int conversationId);

    /**
     * Save a new message to the database.
     *
     * @param msg The MessageRequest object containing the message details.
     * @return A MessageResponse object representing the saved message.
     */
    MessageResponse saveMessage(MessageRequest msg);

    /**
     * Find messages within a specific conversation by its conversation ID.
     *
     * @param conversationId The ID of the conversation for which to retrieve messages.
     * @return A list of MessageResponse objects representing the conversation's messages.
     */
    List<MessageResponse> findMessagesByConversationId(int conversationId);

    /**
     * Delete a message by its unique message ID.
     *
     * @param messageId The ID of the message to be deleted.
     */
    void deleteMessageByMessageId(int messageId);
}

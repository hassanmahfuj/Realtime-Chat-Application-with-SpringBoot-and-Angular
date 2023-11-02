package com.hum.chatapp.service;

import com.hum.chatapp.dto.MessageRequest;

/**
 * An interface for handling real-time messaging functionality using web sockets.
 */
public interface MessageSocketService {

    /**
     * Send user conversations to a specific user by their user ID through a web socket.
     *
     * @param userId The ID of the user for whom to send conversations.
     */
    void sendUserConversationByUserId(int userId);

    /**
     * Send messages of a specific conversation to the connected users through a web socket.
     *
     * @param conversationId The ID of the conversation for which to send messages.
     */
    void sendMessagesByConversationId(int conversationId);

    /**
     * Save a new message using a web socket.
     *
     * @param msg The MessageRequest object containing the message details to be saved.
     */
    void saveMessage(MessageRequest msg);

    /**
     * Delete a conversation by its unique conversation ID using a web socket.
     *
     * @param conversationId The ID of the conversation to be deleted.
     * @param user1          The ID of a user to notify deleted message.
     * @param user2          The ID of a user to notify deleted message.
     */
    void deleteConversationByConversationId(int conversationId, int user1, int user2);

    /**
     * Delete a message by its unique message ID within a conversation using a web socket.
     *
     * @param conversationId The ID of the conversation to notify its listener.
     * @param messageId      The ID of the message to be deleted.
     */
    void deleteMessageByMessageId(int conversationId, int messageId);
}

package com.hum.chatapp.service.impl;

import com.hum.chatapp.dao.MessageDao;
import com.hum.chatapp.dto.ConversationResponse;
import com.hum.chatapp.dto.MessageRequest;
import com.hum.chatapp.dto.MessageResponse;
import com.hum.chatapp.dto.WebSocketResponse;
import com.hum.chatapp.service.MessageSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the MessageSocketService interface that handles real-time messaging functionality using web sockets.
 */
@Service
@RequiredArgsConstructor
public class MessageSocketServiceImpl implements MessageSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageDao messageDao;

    /**
     * Send user conversations to a specific user by their user ID through a web socket.
     *
     * @param userId The ID of the user for whom to send conversations.
     */
    @Override
    public void sendUserConversationByUserId(int userId) {
        List<ConversationResponse> conversation = messageDao.findConversationsByUserId(userId);
        messagingTemplate.convertAndSend("/topic/user/".concat(String.valueOf(userId)), WebSocketResponse.builder()
                .type("ALL")
                .data(conversation)
                .build()
        );
    }

    /**
     * Send messages of a specific conversation to the connected users through a web socket.
     *
     * @param conversationId The ID of the conversation for which to send messages.
     */
    @Override
    public void sendMessagesByConversationId(int conversationId) {
        List<MessageResponse> conversation = messageDao.findMessagesByConversationId(conversationId);
        messagingTemplate.convertAndSend("/topic/conv/".concat(String.valueOf(conversationId)), WebSocketResponse.builder()
                .type("ALL")
                .data(conversation)
                .build()
        );
    }

    /**
     * Save a new message using a web socket.
     *
     * @param msg The MessageRequest object containing the message details to be saved.
     */
    @Override
    public void saveMessage(MessageRequest msg) {
        MessageResponse res = messageDao.saveMessage(msg);
        // notify listener
        messagingTemplate.convertAndSend("/topic/conv/".concat(msg.getConversationId().toString()),
                WebSocketResponse.builder()
                        .type("ADDED")
                        .data(res)
                        .build()
        );
        sendUserConversationByUserId(msg.getSenderId());
        sendUserConversationByUserId(msg.getReceiverId());
    }

    /**
     * Delete a conversation by its unique conversation ID using a web socket.
     *
     * @param conversationId The ID of the conversation to be deleted.
     * @param user1          The ID of a user to notify deletion.
     * @param user2          The ID of a user to notify deletion.
     */
    @Override
    public void deleteConversationByConversationId(int conversationId, int user1, int user2) {
        messageDao.deleteConversationByConversationId(conversationId);
        // notify listener
        sendUserConversationByUserId(user1);
        sendUserConversationByUserId(user2);
    }

    /**
     * Delete a message by its unique message ID within a conversation using a web socket.
     *
     * @param conversationId The ID of the conversation to notify its listener.
     * @param messageId      The ID of the message to be deleted.
     */
    @Override
    public void deleteMessageByMessageId(int conversationId, int messageId) {
        messageDao.deleteMessageByMessageId(messageId);
        // notify listener
        sendMessagesByConversationId(conversationId);
    }
}

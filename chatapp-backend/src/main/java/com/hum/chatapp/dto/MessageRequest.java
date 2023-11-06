package com.hum.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    Integer conversationId;
    Integer senderId;
    Integer receiverId;
    String message;
    LocalDateTime timestamp;
}

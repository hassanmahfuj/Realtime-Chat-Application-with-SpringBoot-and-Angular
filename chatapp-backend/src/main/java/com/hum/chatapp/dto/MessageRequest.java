package com.hum.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    Integer conversationId;
    Integer senderId;
    Integer receiverId;
    String message;
    Date timestamp;
}

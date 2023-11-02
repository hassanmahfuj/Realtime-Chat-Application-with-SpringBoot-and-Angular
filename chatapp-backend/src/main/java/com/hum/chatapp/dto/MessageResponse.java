package com.hum.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    Integer messageId;
    Integer senderId;
    Integer receiverId;
    String message;
    Date timestamp;
}

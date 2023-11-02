package com.hum.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponse {
    Integer conversationId;
    Integer otherUserId;
    String otherUserName;
    String lastMessage;
    String lastMessageTimestamp;
}

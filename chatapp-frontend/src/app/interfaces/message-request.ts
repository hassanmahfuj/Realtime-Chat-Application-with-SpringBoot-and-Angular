export interface MessageRequest {
  conversationId: number;
  senderId: number;
  receiverId: number;
  message: string;
  timestamp: Date;
}

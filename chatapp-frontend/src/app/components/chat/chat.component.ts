import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { MessageRequest } from '../../interfaces/message-request';
import { ApiResponse } from 'src/app/interfaces/api-response';
import { User } from 'src/app/interfaces/user';
import { ConversationResponse } from 'src/app/interfaces/conversation-response';
import { MessageResponse } from 'src/app/interfaces/message-response';
import { StompService } from 'src/app/services/stomp.service';
import { Subscription } from 'rxjs';
import { WebSocketResponse } from 'src/app/interfaces/web-socket-response';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy {
  currentUser: User = {
    userId: 0,
    firstName: '',
    lastName: '',
    email: '',
  };
  // all users except current user
  users: User[] = [];
  // users all conversations
  userConversations: ConversationResponse[] = [];
  // current user conversation subscription
  stompUserSub: Subscription | undefined;

  // selected conversation
  selectedConversationId: number = -1;
  selectedConversationReceiverId: number = -1;
  selectedConversationReceiverName: string = '';
  // selected conversation messages
  selectedConversation: MessageResponse[] = [];
  // selected conversation messages subscription
  stompConvSub: Subscription | undefined;

  // Boolean flag to indicate whether showing users or conversation on left column
  showUserState: boolean = false;
  // Input field for send message
  message: string = '';

  constructor(
    private router: Router,
    private userService: UserService,
    private stomp: StompService
  ) {
    this.currentUser = userService.currentUser();
  }

  ngOnInit(): void {
    // Subscribe to userId websocket to get updated conversation when gets new messages
    this.subscribeToCurrentUserConversation();
  }

  ngOnDestroy(): void {
    // Unsubscribe from all channels onDestroy
    this.stompUserSub?.unsubscribe();
    this.stompConvSub?.unsubscribe();
  }

  // When click the new/add button Then get all users and set users list
  onShowHideUserConversation() {
    this.showUserState = !this.showUserState;
    if (this.showUserState) {
      this.userService
        .getAllUsersExceptCurrentUser()
        .subscribe((res: ApiResponse) => {
          this.users = res.data;
        });
    }
  }

  // Close a chat from dropdown menu
  onCloseChat() {
    this.stompConvSub?.unsubscribe();
    this.selectedConversationId = -1;
  }

  // When click logout button Then remove user from localStorage and navigate to homepage
  onUserLogout() {
    localStorage.removeItem('user');
    this.router.navigate(['.']);
  }

  subscribeToCurrentUserConversation() {
    // setting one second delayed to successfully connect the stomp to server
    setTimeout(() => {
      this.stompUserSub = this.stomp.subscribe(
        'user/' + this.currentUser.userId,
        (payload: any) => {
          let res: WebSocketResponse = payload;
          if (res.type == 'ALL') {
            this.userConversations = res.data;
            const found = this.userConversations.find(
              (item) => item.conversationId === this.selectedConversationId
            );
            if (found === undefined) {
              this.onCloseChat();
            }
          }
        }
      );
      // Notify that I'm subscribed to get initial data
      this.stomp.send('user', this.currentUser.userId);
    }, 1000);
  }

  // When new or exiting user selected Then set the variables and get the two users
  // conversationId from the database
  onUserSelected(receiverId: number, receiverName: string) {
    this.selectedConversationReceiverId = receiverId;
    this.selectedConversationReceiverName = receiverName;
    this.userService
      .getConversationIdByUser1IdAndUser2Id(receiverId, this.currentUser.userId)
      .subscribe((res: ApiResponse) => {
        this.selectedConversationId = res.data;
        this.onShowHideUserConversation();
        this.setConversation();
      });
  }

  // When user select a conversation from the list
  onConversationSelected(index: number) {
    this.selectedConversationId = this.userConversations[index].conversationId;
    this.selectedConversationReceiverId =
      this.userConversations[index].otherUserId;
    this.selectedConversationReceiverName =
      this.userConversations[index].otherUserName;

    this.setConversation();
  }

  // Set a conversation of selected conversationId
  setConversation() {
    // unsubscribe any previous subscription
    this.stompConvSub?.unsubscribe();
    // then subscribe to selected conversation
    // when get new message then add the message to first of the array
    this.stompConvSub = this.stomp.subscribe(
      'conv/' + this.selectedConversationId,
      (payload: any) => {
        let res: WebSocketResponse = payload;
        if (res.type == 'ALL') {
          this.selectedConversation = res.data;
        } else if (res.type == 'ADDED') {
          let msg: MessageResponse = res.data;
          this.selectedConversation.unshift(msg);
        }
      }
    );
    // Notify that I'm subscribed to get initial data
    this.stomp.send('conv', this.selectedConversationId);
  }

  // Send message to other user
  onSendMessage() {
    // If message field is empty then return
    if (this.message.trim().length == 0) return;

    const timestamp = new Date();
    let body: MessageRequest = {
      conversationId: this.selectedConversationId,
      senderId: this.userService.currentUser().userId,
      receiverId: this.selectedConversationReceiverId,
      message: this.message.trim(),
      timestamp: timestamp,
    };
    this.stomp.send('sendMessage', body);
    this.message = '';
  }

  // When click Delete chat from the dropdown menu Then delete the conversation
  // with it's all messages
  onDeleteConversation() {
    this.stomp.send('deleteConversation', {
      conversationId: this.selectedConversationId,
      user1Id: this.currentUser.userId,
      user2Id: this.selectedConversationReceiverId,
    });
  }

  // When click delete on a message menu Then delete from database Then refresh
  // conversation list
  onDeleteMessage(messageId: number) {
    this.stomp.send('deleteMessage', {
      conversationId: this.selectedConversationId,
      messageId: messageId,
    });
  }
}

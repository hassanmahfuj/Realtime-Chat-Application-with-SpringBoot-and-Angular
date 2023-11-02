import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../interfaces/login-request';
import { Observable } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl: string = 'http://localhost:8080/user';

  constructor(private http: HttpClient) {}

  // Attempt to log in a user with the provided credentials
  userLogin(body: LoginRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.baseUrl.concat('/login'), body);
  }

  // Register a new user with the provided user data
  userRegister(body: User): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.baseUrl.concat('/register'), body);
  }

  // Retrieve a list of all users except the currently logged-in user
  getAllUsersExceptCurrentUser(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      this.baseUrl.concat('/except/' + this.currentUser().userId)
    );
  }

  // Retrieve the conversation ID between two users
  getConversationIdByUser1IdAndUser2Id(
    user1Id: number,
    user2Id: number
  ): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(this.baseUrl.concat('/conversation/id'), {
      params: { user1Id: user1Id, user2Id: user2Id },
    });
  }

  // Retrieve the currently logged-in user from local storage
  currentUser(): User {
    return JSON.parse(localStorage.getItem('user') || '{}');
  }
}

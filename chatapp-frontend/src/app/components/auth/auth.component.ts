import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse } from 'src/app/interfaces/api-response';
import { LoginRequest } from 'src/app/interfaces/login-request';
import { User } from 'src/app/interfaces/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
})
export class AuthComponent {
  // Boolean flag to determine whether the user is in login or registration mode
  isLogin: boolean = true;

  // Boolean flag to indicate whether a user was not found during login
  userNotFound: boolean = false;

  // Input fields for user registration and login
  firstName: string = '';
  lastName: string = '';
  email: string = '';

  constructor(private userService: UserService, private router: Router) {
    // Check if the user is already logged in, and if so, redirect to the chat page
    if (localStorage.getItem('user') != null) {
      this.router.navigate(['chat']);
    }
  }

  // Toggle between login and registration modes
  toggleAuth(): void {
    this.isLogin = !this.isLogin;
    this.userNotFound = false;
  }

  // Handle user login
  onUserLogin(): void {
    let body: LoginRequest = {
      email: this.email,
    };
    // Call the userLogin method from the UserService and handle the response
    this.userService.userLogin(body).subscribe((res: ApiResponse) => {
      if (res.data != null) {
        // User is successfully logged in; store user data in local storage and navigate to the chat page
        localStorage.setItem('user', JSON.stringify(res.data));
        this.router.navigate(['chat']);
      } else {
        // User not found; set the 'userNotFound' flag to display an error message
        this.userNotFound = true;
      }
    });
  }

  // Handle user registration
  onUserRegister(): void {
    let body: User = {
      userId: 0,
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
    };
    // Call the userRegister method from the UserService and handle the response
    this.userService.userRegister(body).subscribe((res: ApiResponse) => {
      if (res.data != null) {
        // User is successfully registered; store user data in local storage and navigate to the chat page
        localStorage.setItem('user', JSON.stringify(res.data));
        this.router.navigate(['chat']);
      } else {
        // Registration failed
        console.log(res);
      }
    });
  }
}

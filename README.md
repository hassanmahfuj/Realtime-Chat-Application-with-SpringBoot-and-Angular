# Realtime Chat Application with SpringBoot and Angular

This is a real-time chat application with a client in the browser developed using Angular for the frontend and Spring Boot for the backend. The backend uses WebSocket (via Spring's WebSocket support) for real-time communication between users. It also integrates with MySQL to store user data and chat history. Users can register with their email, first name, and last name, and then initiate chats with other registered users. The application also supports viewing and deleting old chats and allows users to sign out.

### [LIVE PREVIEW](https://hassanmahfuj.github.io/chatapp-live)

## Screenshots

![RegisterPage](https://github.com/hassanmahfuj/Realtime-Chat-Application-with-SpringBoot-and-Angular/blob/main/RegisterPage.jpg)

![LoginPage](https://github.com/hassanmahfuj/Realtime-Chat-Application-with-SpringBoot-and-Angular/blob/main/LoginPage.jpg)

![ChatPage](https://github.com/hassanmahfuj/Realtime-Chat-Application-with-SpringBoot-and-Angular/blob/main/ChatPage.jpg)

![DeleteModal](https://github.com/hassanmahfuj/Realtime-Chat-Application-with-SpringBoot-and-Angular/blob/main/DeleteModal.jpg)

## Features

- User Registration:

  - New users can register with their email address, first name, and last name.

- Login:

  - Users can log in using their registered email address.

- Real-time Chat:

  - Users can start real-time chats with other registered users. Chats are always one-on-one.

- Chat History:

  - Users can view a list of old chats (conversations) and delete them. Single message can also be deleted.

- Sign Out:
  - Users can sign out from the application.

## Installation Guide

### Clone the Project

1. Open a terminal window.

2. Navigate to the directory where you want to store the project.

3. Clone the project repository using the following command:

   ```
   https://github.com/hassanmahfuj/Realtime-Chat-Application-with-SpringBoot-and-Angular.git
   ```

### Setting up MySQL Database (Database)

1. Install MySQL if not already installed.

2. Create a new database for the chat application:
   ```sql
   CREATE DATABASE chat_app;
   ```

### Setting up Spring Boot (Backend)

1. Open the `chatapp-backend` project in IntelliJ IDEA.

2. Update the database configuration in the Spring Boot backend `src/main/resources/application.properties`:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/chat_app
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

### Setting up Angular (Frontend)

1. Make sure you have Node.js and npm installed on your system. If not, install them.
2. Navigate to the frontend directory:

   ```
   cd chatapp-frontend
   ```

3. Install the required packages:
   ```
   npm install
   ```

## Running the Application

1. Open IntelliJ IDEA and run the Spring Boot application by pressing `Shift` + `F10` or the `Run` button.

2. Start the Angular frontend by running the following command in the chatapp-frontend directory:

   ```
   ng serve
   ```

3. Access the application in your web browser at http://localhost:4200.

4. You can now register, log in, and start chatting in real-time!

## Contact

If you have any questions or encounter issues, feel free to contact at humahfuj@example.com.

### Enjoy chatting!

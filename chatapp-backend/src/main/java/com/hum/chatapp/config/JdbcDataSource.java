package com.hum.chatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A Spring Bean representing a JDBC DataSource for a MySQL database.
 * This class manages the database connection using properties specified
 * in the application.properties file.
 */
@Component
public class JdbcDataSource {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPass;
    private Connection con;

    /**
     * Constructs a new database connection using the provided properties.
     *
     * @param dbUrl  The URL of the database.
     * @param dbUser The username for the database.
     * @param dbPass The password for the database.
     */
    public JdbcDataSource(
            @Value("${spring.datasource.url}") String dbUrl,
            @Value("${spring.datasource.username}") String dbUser,
            @Value("${spring.datasource.password}") String dbPass
    ) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        connect();
    }

    /**
     * Establishes a connection to the MySQL database using the provided URL,
     * username, and password.
     */
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the established database connection.
     *
     * @return The established database connection.
     */
    public Connection get() {
        return con;
    }
}
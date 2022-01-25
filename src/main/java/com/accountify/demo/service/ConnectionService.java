package com.accountify.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class ConnectionService {

    @Bean
    public Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "password");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/accountify", connectionProps);
        System.out.println("Connected to database");
        return conn;
    }
}



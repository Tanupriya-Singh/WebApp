package com.search.database;

import com.search.constants.DatabaseConstants;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnector {

    private static Connection connection;

    private JDBCConnector(){
        try {
            //Class.forName(DatabaseConstants.JDBC_DRIVER).newInstance();
            connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.USER, DatabaseConstants.PASS);

        } catch (Exception se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null){
            new JDBCConnector();
        }
        return connection;
    }
}

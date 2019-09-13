package com.search.constants;

public class DatabaseConstants {

    // JDBC driver name and database URL
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306";

    //  Database credentials
    public static final String USER = "root";
    public static final String PASS = "1234";

    //Query Strings
    public static final String REGISTERED_USERS_TABLE = "file_search.registered_users";
    public static final String SEARCH_HISTORY_TABLE = "file_search.search_history";
    public static final String USER_SEARCH_HISTORY_TABLE = "file_search.user_search_history";
    //Spaces are important here
    public static final String INSERT_NEW_USER = "INSERT INTO "+ REGISTERED_USERS_TABLE +" (username, password) VALUES (?,?)";
    public static final String SELECT_QUERY_REGISTERED_USERS = "SELECT username, password from "+ REGISTERED_USERS_TABLE;
    public static final String SEARCH_HISTORY_TABLE_USERNAME = "username";
    public static final String SEARCH_HISTORY_TABLE_PASSWORD = "password";
    public static final String SELECT_SEARCH_WORDS_BY_USER = "SELECT search_word FROM file_search.user_search_history where username = (?)";
    public static final String INSERT_SEARCH_HISTORY_OF_USER = "INSERT INTO file_search.user_search_history (username, search_word, row_creation_data) VALUES (?,?,?)";
    public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String SELECT_SEARCH_WORD = "SELECT searchWord FROM file_search.search_history";
}

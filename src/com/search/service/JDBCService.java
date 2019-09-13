package com.search.service;

import com.search.constants.Constants;
import com.search.constants.DatabaseConstants;
import com.search.database.JDBCConnector;
import com.search.exception.BadFormatException;
import com.search.model.FileObject;
import com.search.model.ResultHistory;
import com.search.persistence.User;
import com.search.utils.DeserializeJDBC;
import com.search.utils.SerializeJDBC;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JDBCService {

    //To check for no results
    private boolean flag;

    //To store the results
    private List<FileObject> matchResults;

    private int resultCounter;

    private PrintWriter out;

    private List<User> registeredUsers;

    private HttpSession session;

    //Constructor
    public JDBCService(HttpSession session, PrintWriter out) {
        registeredUsers = new ArrayList<>();
        flag = false;
        resultCounter = 0;
        matchResults = new ArrayList<>();
        this.out = out;
        this.session = session;
    }


    private void insertIntoUserHistory( String searchWord) {
//        out.println("Inside user history");
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(DatabaseConstants.dateFormat);

        String currentTime = sdf.format(dt);

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCConnector.getConnection();
            statement = connection.prepareStatement(DatabaseConstants.INSERT_SEARCH_HISTORY_OF_USER);
            statement.setString(1, String.valueOf(session.getAttribute(Constants.sessionUsername)));
            statement.setString(2, searchWord);
            statement.setString(3, currentTime);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally{
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se){
            }// do nothing
        }//end try

    }

    public String getHistoryOfUser(){
        //Make a join statement here
        String history = "";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCConnector.getConnection();
            statement = connection.prepareStatement(DatabaseConstants.SELECT_SEARCH_WORDS_BY_USER);
            statement.setString(1, String.valueOf(session.getAttribute(Constants.sessionUsername)));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                history = history+ ","+rs.getString("search_word");

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally{
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se){
            }// do nothing
        }//end try

        return history;

    }
    public boolean insertIntoDB(String usernameToInsert, String passwordToInsert) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCConnector.getConnection();
            statement = connection.prepareStatement(DatabaseConstants.INSERT_NEW_USER );
            statement.setString(1, usernameToInsert);
            statement.setString(2, passwordToInsert);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        finally{
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se){
            }// do nothing
        }//end try
        return true;
    }

    public boolean checkAuthorizedUsers(String enteredUsername, String enteredPassword, PrintWriter out) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBCConnector.getConnection();
            statement = connection.createStatement();
            String sql = DatabaseConstants.SELECT_QUERY_REGISTERED_USERS;
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String usernameInDB = rs.getString(DatabaseConstants.SEARCH_HISTORY_TABLE_USERNAME);
                String passwordInDB = rs.getString(DatabaseConstants.SEARCH_HISTORY_TABLE_PASSWORD);

                if (usernameInDB.equals(enteredUsername) && passwordInDB.equals(enteredPassword)) {
                    return true;
                }
            }
            rs.close();
            statement.close();
        } catch (Exception se) {
            se.printStackTrace();
        }//Handle errors for Class.forName

        finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
            }
        }
        return false;
    }

    public boolean checkBadFormatInput(String searchWord) {
        boolean statusOfInput = true;
        try {
            statusOfInput = (searchWord != null) && (!searchWord.equals("")) && (searchWord.matches(Constants.inputStringValidation));
            if (!statusOfInput) {
                throw new BadFormatException(Constants.badFormatExceptionMessage);
            }
        } catch (BadFormatException message) {
            out.println("\n" + message.getClass().getName() + ": " + message.getMessage());
            out.print(Constants.exitMessage);

        }
        return statusOfInput;
    }

    private void directoryCrawler(File directory, String searchWord) {

        // Step 1 - Get List of files in folder and print

        File[] filesAndDirs = directory.listFiles();
        /*
         * Iterate the list of files, if it is identified as not a file call
         * call the crawler method to list all the files in that directory.
         */
        for (File file : filesAndDirs) {
            if (file.isFile()) {
                performSearch(file, searchWord);
            } else {
                directoryCrawler(file, searchWord);
            }
        }
    }

    private void performSearch(File file, String searchWord) {

        String path = file.getAbsolutePath();
        /*
         * Declare the search pattern here
         */
        String searchPattern = Constants.regexPattern + searchWord + Constants.regexPattern;
        Scanner fileScanner;


        try {
            fileScanner = new Scanner(file);
            int lineID = 0;
            Pattern pattern = Pattern.compile(searchPattern);
            Matcher matcher = null;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                lineID++;
                matcher = pattern.matcher(line);

                if (matcher.find()) {
                    //If first result, display the table for better look
                    if (resultCounter == 0) {
                        if (out == null)
                            System.out.println("\n" + Constants.resultDisplayFormatting);
                        else
                            out.println("\n" + Constants.resultDisplayFormatting);
                    }
                    //Increment the number of results
                    resultCounter++;

                    /**
                     *Insertion happens here
                     */

                    //Add to the results
                    matchResults.add(new FileObject(lineID, path));
                    //Set the flag for no result
                    flag = true;

                    //Without this line, no results will be displayed for the first time.
                    if (out == null)
                        System.out.println(resultCounter + "\t\t\t " + lineID + " \t\t\t" + path);
                    else
                        out.println(resultCounter + "\t\t\t " + lineID + " \t\t\t" + path);

                    //Serialized data here

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkIfSerialized(String searchWord) {

        insertIntoUserHistory(searchWord);

        //TODO:Make case insensitive search
        //searchWord = searchWord.toLowerCase();
        //Making this true to test the DB
        boolean resultSerialized = readDBWithAllResults(searchWord);
        /*
        With the result query, check if present
         */

        if (!resultSerialized) {
            /*
             * Call the search class to crawl through the directory
             */
            File directory = new File(Constants.fileDirectory);
            directoryCrawler(directory, searchWord);

            if (!checkNoResult()) {
                out.println(Constants.noResultMessage);
                out.println(Constants.exitMessage);
                return;
            }
            serializeTheResults(searchWord);
        } else {
            out.println("Calling deserializer from JDBCService");
            DeserializeJDBC deserializeJDBC = new DeserializeJDBC();
            ResultHistory resultHistory = deserializeJDBC.deserializeResults(searchWord, out);
            displayHistoryResults(resultHistory);
        }
    }

    //To check if no result
    private boolean checkNoResult() {
        return flag;
    }

    //DONE This should be the SerializeJDBC. Serializing working perfectly fine
    private void serializeTheResults(String searchWord) {
        SerializeJDBC serializeJDBC = new SerializeJDBC();
        serializeJDBC.serializeBlobObjects(searchWord, matchResults);
    }



    /**
     * Here, read the database to check if the result has been serialized before.
     * Results are to be serialized as a blob object
     */

    private boolean readDBWithAllResults(String searchWord) {

        //Get all the values of the column Primary key(Keyword) over here
        ResultSet rs = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCConnector.getConnection();
            preparedStatement = connection.prepareStatement(DatabaseConstants.SELECT_SEARCH_WORD);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String searchWordInDB = rs.getString("searchWord");
                if (searchWord.equals(searchWordInDB)) {
                    return true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException se) {
            }// do nothing
        }//end try
        return false;

    }

    private void displayHistoryResults(ResultHistory resultHistory) {
        //Displaying the results
        List<FileObject> fileObjects = resultHistory.getResultsForKeyword();
        out.println(Constants.resultDisplayFormatting);
        out.println("Deserializing results for:" + resultHistory.getSearchKeyword() + "\n");
        int resultCounter = 0;

        for (FileObject fileObject : fileObjects) {
            resultCounter++;
            //Add the results for further operations
            matchResults.add(fileObject);
            out.println(resultCounter + "     " + fileObject.getLineID() + "     " + fileObject.getAbsolutePath());
        }
    }

    public boolean checkIfUserExists(String username){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCConnector.getConnection();
            statement = connection.createStatement();
            String sql = DatabaseConstants.SELECT_QUERY_REGISTERED_USERS;
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String usernameInDB = rs.getString(DatabaseConstants.SEARCH_HISTORY_TABLE_USERNAME);

                if (usernameInDB.equals(username)) {
                    return true;
                }
            }
            rs.close();
            statement.close();
        } catch (Exception se) {
            se.printStackTrace();
        }//Handle errors for Class.forName

        finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
            }
        }
        return false;
    }


    //TODO:Instantiate users from the database here

//    public void instantiateUserFromDatabase(){
//
//        Connection connection = null;
//        Statement statement = null;
//
//        try {
//            connection = JDBCConnector.getConnection();
//            statement = connection.createStatement();
//            String sql = DatabaseConstants.SELECT_QUERY;
//            ResultSet rs = statement.executeQuery(sql);
//
//            while (rs.next()) {
//                String usernameInDB = rs.getString("username");
//                String passwordInDB = rs.getString("password");
//                int id = rs.getInt("id");
//
//                //Initialize users here
//                registeredUsers.add(new User(id, usernameInDB, passwordInDB));
//            }
//            rs.close();
//            statement.close();
//        } catch (Exception se) {
//            se.printStackTrace();
//        }//Handle errors for Class.forName
//
//        finally {
//            //finally block used to close resources
//            try {
//                if (statement != null)
//                    statement.close();
//            } catch (SQLException se2) {
//            }
//        }
//    }

}
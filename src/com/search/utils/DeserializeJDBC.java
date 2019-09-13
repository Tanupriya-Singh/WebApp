package com.search.utils;

import com.search.constants.Constants;
import com.search.database.JDBCConnector;
import com.search.model.FileObject;
import com.search.model.ResultHistory;

import java.io.*;
import java.sql.*;
import java.util.List;

/**
 * Reads the file with name <searchword>.txt
 */
public class DeserializeJDBC {

    private List<FileObject> searchList;


    public ResultHistory deserializeResults(String searchWord, PrintWriter out) {

        //Abhi, read the object from the file which stores that particular keyword

        Connection connection = JDBCConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {

            connection = JDBCConnector.getConnection();
            preparedStatement = connection.prepareStatement("SELECT resultList FROM file_search.search_history WHERE searchWord = ?");
            preparedStatement.setString(1, searchWord);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                byte[] st = (byte[]) rs.getObject(1);
                ByteArrayInputStream baip = new ByteArrayInputStream(st);
                ObjectInputStream ois = new ObjectInputStream(baip);
                searchList = (List<FileObject>) ois.readObject();
            }

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        ResultHistory resultHistory = new ResultHistory();
        resultHistory.setSearchKeyword(searchWord);
        resultHistory.setResultsForKeyword(searchList);
        //Now, print the object
        return resultHistory;

    }
}





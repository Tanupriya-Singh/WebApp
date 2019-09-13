package com.search.utils;

import com.search.constants.Constants;
import com.search.database.JDBCConnector;
import com.search.model.FileObject;
import com.search.model.ResultHistory;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SerializeJDBC {

    private ResultHistory resultHistory = new ResultHistory();

    public void serializeBlobObjects(String searchKeyword, List<FileObject> searchResults){
        Connection connection = JDBCConnector.getConnection();
        ObjectOutputStream oos = null;
        PreparedStatement preparedStatement = null;
        try {
            /*
            Open the streams here
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(searchResults);
            byte[] resultAsBytes = baos.toByteArray();
            preparedStatement = connection.prepareStatement("INSERT INTO file_search.search_history (searchWord, resultList) VALUES(?,?)");
            preparedStatement.setString(1, searchKeyword);
            ByteArrayInputStream bais = new ByteArrayInputStream(resultAsBytes);
            preparedStatement.setBinaryStream(2, bais, resultAsBytes.length);
            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
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

    }
}


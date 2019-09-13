package com.search.utils;

import com.search.constants.Constants;
import com.search.model.ResultHistory;

import java.io.*;

/**
 * Reads the file with name <searchword>.txt
 */
public class DeserializeHistory {

    private ResultHistory resultHistory;
    private ObjectInputStream out;

    public DeserializeHistory(String searchWord) {
        try {
            FileInputStream fileIn = new FileInputStream(Constants.directoryWithSerializedResults + searchWord + ".txt");
            out = new ObjectInputStream(fileIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultHistory deserializeResults() {

        //Abhi, read the object from the file which stores that particular keyword

        try {
            resultHistory = (ResultHistory) out.readObject();
            System.out.println("Inside deserializer!");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Now, print the object
        String searchWord = resultHistory.getSearchKeyword();
        System.out.println("Deserializing results for:" + searchWord + "\n");
        return resultHistory;

    }
}





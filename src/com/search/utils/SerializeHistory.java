package com.search.utils;

import com.search.constants.Constants;
import com.search.model.FileObject;
import com.search.model.ResultHistory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 What happens here is that
 1. A file is created with the name of whatever is searched. e.g., I am.txt
 2. This keyword is appended to the @Constants.fileWithAllResults. This file has already been created in the SearchService.txt
 */

public class SerializeHistory {

    private ResultHistory resultHistory = new ResultHistory();
    private File fileWithAllResults;
    private File fileCreatedEveryResult;

    public SerializeHistory(String searchKeyword){
        //Initialize
        fileWithAllResults = new File(Constants.fileWithAllResults);
        fileCreatedEveryResult = new File(Constants.directoryWithSerializedResults + searchKeyword + ".txt");
    }

    public void storeHistoryResults(String searchKeyword, List<FileObject> searchResults){

        /*
        Get all the contents of the file
//         */
        String stringWithAllResults = "";
        try {
            stringWithAllResults = Files.readString(Paths.get(fileWithAllResults.getAbsolutePath()), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //First store the keyword in @Constants.fileWithAllResults text file
        try {
            FileWriter fileWriter = new FileWriter(fileWithAllResults);
            fileWriter.write( stringWithAllResults + searchKeyword+",");
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //Now, create the file with that search word
        try {
            FileOutputStream fileOut = new FileOutputStream(fileCreatedEveryResult);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            //Set the result history
            resultHistory.setSearchKeyword(searchKeyword);
            resultHistory.setResultsForKeyword(searchResults);
            out.writeObject(resultHistory);
            out.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

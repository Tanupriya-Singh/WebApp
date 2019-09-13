package com.search.service;

import com.search.constants.Constants;
import com.search.exception.BadFormatException;
import com.search.model.FileObject;
import com.search.model.ResultHistory;
import com.search.utils.DeserializeHistory;
import com.search.utils.SerializeHistory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchService {

    //To check for no results
    private boolean flag;

    //To store the results
    private List<FileObject> matchResults;

    private int resultCounter;

    private PrintWriter out;

    //Constructor
    public SearchService(PrintWriter out) {

        flag = false;
        resultCounter = 0;
        matchResults = new ArrayList<>();
        this.out = out;
    }

    /**
     * To search a keyword
     * 1. Get list of files in folder
     * 1.1 If you get a file, search
     * 1.2 Else, get a list again
     * <p>
     * 2. Perform the search in the file
     * 2.2 Store the results in the resultHistory object;
     * 2.3 Then, make the
     */

    public void directoryCrawler(File directory, String searchWord) {

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

    //Step 2 - Perform the search in a file

    private List<FileObject> performSearch(File file, String searchWord) {

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
        return matchResults;
    }

    /*
     Called after the deserializer gives back a result object.
     */
    public void displayHistoryResults(ResultHistory resultHistory) {
        //Displaying the results

        List<FileObject> fileObjects = resultHistory.getResultsForKeyword();
        if (out == null)
            System.out.println(Constants.resultDisplayFormatting);
        else
            out.println(Constants.resultDisplayFormatting);

        int resultCounter = 0;

        for (FileObject fileObject : fileObjects) {
            resultCounter++;
            //Add the results for further operations
            matchResults.add(fileObject);
            if (out == null)
                System.out.println(resultCounter + "\t\t\t" + fileObject.getLineID() + "\t\t\t" + fileObject.getAbsolutePath());
            else
                out.println(resultCounter + "     " + fileObject.getLineID() + "     " + fileObject.getAbsolutePath());
        }
    }
    /*
    Displaying the file
     */

    public void displayFile(int enteredResultNumber) {

        FileObject fileObject = matchResults.get(enteredResultNumber - 1);
        String pathOfFile = fileObject.getAbsolutePath();
        File file = new File(pathOfFile);

        //Get the name of the file
        String name = file.getName();
        if (out == null)
            System.out.print("\nDisplaying " + name + Constants.linesForFormatting);
        else
            out.print("\nDisplaying " + name + Constants.linesForFormatting);

        /*
        I have to display the five lines before and after the particular line
         */
        int lineWithResult = fileObject.getLineID();
        Scanner sc = new Scanner(System.in);

        int howManyLinesToDisplay = readNumberOfLinesToDisplay();

        if (out == null)
            System.out.println("Line you asked for:" + lineWithResult);
        else
            out.println("Line you asked for:" + lineWithResult);

        int lowLimit = Math.max((lineWithResult - howManyLinesToDisplay-1), 0);
        lowLimit += 1;

        if (out == null)
            System.out.println("LowLimit:" + lowLimit);
        else
            out.println("LowLimit:" + lowLimit);

        int highLimit = lineWithResult + (howManyLinesToDisplay - 1);
        highLimit += 1;

        if (out == null)
            System.out.println("UpperLimit:" + highLimit);
        else
            out.println("UpperLimit:" + highLimit);

        int counter = 0;
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                if (counter >= lowLimit && counter <= highLimit) {
                    if (out == null)
                        System.out.println(fileScanner.nextLine());
                    else
                        out.println(fileScanner.nextLine());
                } else {
                    fileScanner.nextLine();
                }
                counter++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readNumberOfLinesToDisplay() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of lines you want to see below and above the line id");
        int howManyLinesToDisplay = sc.nextInt();
        while (true) {
            try {
                if (howManyLinesToDisplay <= 0 || howManyLinesToDisplay >= 20)
                    throw new BadFormatException("Enter a number Between 1 and 20");
                else
                    break;
            } catch (BadFormatException message) {
                System.out.println(message);
            }
        }
        return howManyLinesToDisplay;
    }

    //Edit file
    public void appendToFile(String enteredStringToAppend, int enteredResultNumber) {

        try {
            // Open given file in append mode.
            FileObject fileObject = matchResults.get(enteredResultNumber - 1);
            String fileName = fileObject.getAbsolutePath();

            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(enteredStringToAppend + "\n");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Return the result
    public String selectedPathNumber(int enteredResultNumber) {
        FileObject fileObject = matchResults.get(enteredResultNumber - 1);
        return fileObject.getAbsolutePath();
    }

    //To check if no result
    private boolean checkNoResult() {
        return flag;
    }

    private void serializeTheResults(String searchWord) {
        SerializeHistory serializeHistory = new SerializeHistory(searchWord);
        serializeHistory.storeHistoryResults(searchWord, matchResults);
    }


    public void checkIfSerialized(String searchWord) {

        boolean resultSerialized = false;
        String stringWithAllResults = readFileWithAllResults();
        String[] arrSplit = stringWithAllResults.split(",");
        for (String s : arrSplit) {
            if (s.equals(searchWord)) {
                resultSerialized = true;
                if (out == null)
                    System.out.println("Keyword history found! Calling deserializer!");
                else
                    out.println("Keyword history found! Calling deserializer!");
            }
        }

        if (resultSerialized == false) {
            /*
             * Call the search class to crawl through the directory
             */
            File directory = new File(Constants.fileDirectory);
            directoryCrawler(directory, searchWord);

            if (!checkNoResult()) {
                if (out == null) {
                    System.out.println(Constants.noResultMessage);
                    System.out.println(Constants.exitMessage);
                } else {
                    out.println(Constants.noResultMessage);
                    out.println(Constants.exitMessage);
                }
                return;
            }
            serializeTheResults(searchWord);
        } else {
            //System.out.println("Calling deserializer from Main");
            DeserializeHistory deserializeHistory = new DeserializeHistory(searchWord);
            ResultHistory resultHistory = deserializeHistory.deserializeResults();
            displayHistoryResults(resultHistory);
        }

        //System.out.println("\n");
    }

    public String readFileWithAllResults() {

        File file;
        Scanner scanner = null;
        try {
            file = new File(Constants.fileWithAllResults);
            /*
             * Saved my life
             */
            file.createNewFile();
            scanner = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
         String stringWithAllResults = "";

        while (scanner.hasNextLine())
            stringWithAllResults = scanner.nextLine();
        return stringWithAllResults;

    }

    public boolean checkBadFormatInput(String searchWord) {
        boolean statusOfInput = true;
        try {
            statusOfInput = (searchWord != null) && (!searchWord.equals("")) && (searchWord.matches(Constants.inputStringValidation));
            if (!statusOfInput) {
                throw new BadFormatException(Constants.badFormatExceptionMessage);
            }
        } catch (BadFormatException message) {
            System.out.println("\n" + message.getClass().getName() + ": " + message.getMessage());
            System.out.print(Constants.exitMessage);

        }
        return statusOfInput;
    }

}
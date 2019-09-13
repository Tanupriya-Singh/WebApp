package com.search.view;

import com.search.constants.Constants;
import com.search.exception.NoOptionException;
import com.search.service.SearchService;

import java.util.Scanner;

public class SearchMain {

    /**
     * @param args 1. Reads the keyword
     *             2. Asks the service class to crawl through the directory
     *             2.1 A flag is present to determine whether there is no result at all
     *             2.2 Else, all results are displayed.
     *             3. Iteratively, User is asked if she wants to see the the results in detail
     *             3.1 User is asked if she wants to append to the file
     *             3.2 @SearchService is called to make changes and display file again
     *             4. The loop keeps running until the user selects "n". If so, wish the customer and exit.
     *             5. In case of any other option, throw an exception
     */

    public static void main(String args[]) {

        SearchService searchService = new SearchService(null);
        //The search word
        String searchWord;
        /*
         * Read the keyword to search here
         */

//        JDBCHelper jdbcHelper = new JDBCHelper();
//        jdbcHelper.readDB("tanupriya","tanupriya", null);

        System.out.print(Constants.enterKeywordMessage);
        Scanner sc = new Scanner(System.in);
        searchWord = sc.nextLine();

        /*
        Check if there are bad format characters
         */


        boolean badFormat = searchService.checkBadFormatInput(searchWord);
        if (badFormat == false)
            return;
        /*
        Check if the keyword has already been searched
        1. If the file doesn't exist, create it.
        2. Read the file and if the keyword exists, display the file.
        3. Else, serialize the file.
         */

        searchService.checkIfSerialized(searchWord);


        /*
         * Now, let the user see the contents of a file
         */
        //while (true) {
        System.out.print(Constants.wantToSeeDetailMessage);

        //Now read the input
        String choice = sc.next();
        if ((Constants.yesOption).equals(choice)) {
            //Ask the user to provide more info
            System.out.print(Constants.enterResultNumberMessage);
            int enteredResultNumber = Integer.parseInt(sc.next());
            //Display the file
            searchService.displayFile(enteredResultNumber);

                /*
                Ask the user if she wants to append to the file
                */

            System.out.print(Constants.wantToAppendMessage);

            String newChoice = sc.next();

            //Append to the file
            if ((Constants.yesOption).equals(newChoice)) {
                //Check if the lock exists
                //FileLockTest fileLockTest = new FileLockTest();
                    /*
                    Ask the string and lock the file
                     */
                String filePath = searchService.selectedPathNumber(enteredResultNumber);
                //fileLockTest.setLock(filePath);
                System.out.print(Constants.enterAppendMessage);
                sc.nextLine();
                String enteredStringToAppend = sc.nextLine();
                searchService.appendToFile(enteredStringToAppend, enteredResultNumber);
                //Now display the file
                searchService.displayFile(enteredResultNumber);
                //fileLockTest.removeLock();


            } else if ((Constants.noOption).equals(newChoice)) {
                System.out.println(Constants.exitMessage);
                //break;
            } else {
                noOptionDetected();
            }
        } else if ((Constants.noOption).equals(choice)) {
            System.out.println(Constants.exitMessage);
            //break;
        } else {
            noOptionDetected();
        }
        //}
    }

    private static void noOptionDetected() {
        try {
            throw new NoOptionException(Constants.wrongOptionExceptionMessage);
        } catch (NoOptionException message) {
            System.out.println("\n" + message.getClass().getName() + ": " + message.getMessage());
        }
    }

}
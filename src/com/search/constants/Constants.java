package com.search.constants;

public class Constants {

    public static final String inputStringValidation =  "[a-zA-Z0-9 ]*$";
    public static final String regexPattern = ".*";
    public static final String fileDirectory = "/Users//ttanupri/Desktop/FileFinder";
    public static final String wrongOptionExceptionMessage = "OPTION DOES NOT EXIST!!";
    public static final String exitMessage = "\nHAVE A NICE DAY!!";
    public static final String yesOption = "yes";
    public static final String noOption = "no";
    public static final String noResultMessage = "NO RESULTS!!";
    public static final String enterKeywordMessage = "Enter the keyword:";
    public static final String linesForFormatting = "---------------------------------------------\n";
    public static final String wantToSeeDetailMessage = "\nNAVIGATION" + Constants.linesForFormatting+
            "Want to see the results in detail?\n" +
            "Enter yes/no:";
    public static final String wantToAppendMessage = "\nNAVIGATION"  + Constants.linesForFormatting+
            "Want to append to the displayed file?\n" +
            "Enter yes/no:";
    public static final String enterResultNumberMessage = "\nEnter the result number to see the contents of the file:";
    public static final String enterAppendMessage = "\nEnter the string to be appended:";

    public static final String resultDisplayFormatting = "RESULT NO \t LINE NO \t ABSOLUTE PATH \n" + Constants.linesForFormatting;
    public static final String badFormatExceptionMessage = "BAD FORMAT INPUT";
    public static final String fileWithAllResults = "/Users/ttanupri/Desktop/SerializedFiles/fileWithAllResults.txt";
    public static final String directoryWithSerializedResults = "/Users/ttanupri/Desktop/SerializedFiles/allResultsSerialized/";
    public static final String inputOptionsValidation = "[0-9]*$";
    public static final String sessionUsername = "username";
    public static final String sessionPassword = "password";
}
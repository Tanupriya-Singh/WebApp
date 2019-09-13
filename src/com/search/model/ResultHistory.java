package com.search.model;
import java.io.Serializable;
import java.util.List;

/**
 * This is the object that has to be serialized into different files
 * One object of this kind has a keyword and an arrayList of type object
 * The object consists of the lineNO and pathname
 */

public class ResultHistory implements Serializable {

    private String searchKeyword;
    private List<FileObject> resultsForKeyword;

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<FileObject> getResultsForKeyword() {
        return resultsForKeyword;
    }

    public void setResultsForKeyword(List<FileObject> resultsForKeyword) {
        this.resultsForKeyword = resultsForKeyword;
    }
}

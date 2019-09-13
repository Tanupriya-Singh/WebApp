package com.search.model;

import java.io.Serializable;

/**
 * This object consists of a lineID and pathname
 */

public class FileObject implements Serializable {
    private int lineID;
    private String absolutePath;

    public FileObject(int lineID, String path) {
        this.lineID = lineID;
        this.absolutePath = path;

    }

    public int getLineID() {
        return lineID;
    }

    public void setLineID(int lineID) {
        this.lineID = lineID;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}

package com.mounica.studytree.api.response;

/**
 * Created by ankur on 7/5/16.
 */
public class ImageUploadResponse {
    private String message;
    private boolean error;
    private String file_path;

    public String getMessage() {
        return message;
    }

    public boolean getError() {
        return error;
    }

    public String getFilePath() {
        return file_path;
    }
}

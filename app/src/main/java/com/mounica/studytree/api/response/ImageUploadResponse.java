package com.mounica.studytree.api.response;

/**
 * Created by ankur on 7/5/16.
 */
public class ImageUploadResponse {
    private String message;
    private String error;
    private String file_path;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getFilePath() {
        return file_path;
    }
}

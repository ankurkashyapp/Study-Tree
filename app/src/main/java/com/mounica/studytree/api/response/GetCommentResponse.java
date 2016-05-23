package com.mounica.studytree.api.response;

import java.util.List;

/**
 * Created by ankur on 23/5/16.
 */
public class GetCommentResponse {
    private String message;
    private List<CommentsResponse> comments;

    public String getMessage() {
        return message;
    }

    public List<CommentsResponse> getComments() {
        return comments;
    }
}

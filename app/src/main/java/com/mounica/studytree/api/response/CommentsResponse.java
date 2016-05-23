package com.mounica.studytree.api.response;

/**
 * Created by ankur on 23/5/16.
 */
public class CommentsResponse {
    private String id;
    private String feed_id;
    private String user_id;
    private String comment;
    private UserResponse user;

    public UserResponse getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }
}

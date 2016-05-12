package com.mounica.studytree.api.response;

import java.util.List;

/**
 * Created by ankur on 5/5/16.
 */
public class UserCreatedResponse {

    private String message;
    private UserResponse user;
    private List<SubjectsResponse> subjects;

    public String getMessage() {
        return message;
    }

    public UserResponse getUser() {
        return user;
    }

    public List<SubjectsResponse> getSubjects() {
        return subjects;
    }
}

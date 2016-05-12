package com.mounica.studytree.api.response;

import java.util.List;

/**
 * Created by ankur on 12/5/16.
 */
public class TeacherListResponse {

    private String message;
    private List<UserResponse> teachers;

    public String getMessage() {
        return message;
    }

    public List<UserResponse> getTeachers() {
        return teachers;
    }
}

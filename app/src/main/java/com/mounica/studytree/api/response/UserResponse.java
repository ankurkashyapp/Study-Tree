package com.mounica.studytree.api.response;

/**
 * Created by ankur on 5/5/16.
 */
public class UserResponse {

    private String user_id;
    private String reg_no;
    private String name;
    private String age;
    private String email;
    private String contact;
    private String password;
    private String image_path;

    public String getUser_id() {
        return user_id;
    }

    public String getReg_no() {
        return reg_no;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getPassword() {
        return password;
    }

    public String getImage_path() {
        return image_path;
    }
}

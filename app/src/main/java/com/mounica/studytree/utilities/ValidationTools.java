package com.mounica.studytree.utilities;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ankur on 5/5/16.
 */
public class ValidationTools {

    public static boolean isValidEmail(String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern= Pattern.compile(EMAIL_PATTERN);
        Matcher matcher=  emailPattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password){
        if(password.trim().length()<6){
            return false;
        }
        return true;
    }

    public static boolean isValidContact(String contact) {
        if (contact.trim().length() < 10) {
            return false;
        }
        return true;
    }

    public static boolean isValidRegNo(String regNo) {
        if (regNo.trim().length() != 8) {
            return false;
        }
        return true;
    }

    public static boolean isValidName(String name) {
        if (name.trim().length() <= 1) {
            return false;
        }
        return true;
    }

    public static boolean isValidAge(String age) {
        if (age.trim().length() == 0 || age.trim().length() > 2) {
            return false;
        }
        return true;
    }

    public static boolean isValidImageTitle(String title) {
        if (title.trim().length() <8 || title.trim().length() > 30) {
            return false;
        }
        return true;
    }

    public static boolean isValidImageDescription(String description) {
        if (description.trim().length() < 10 || description.trim().length() > 150) {
            return false;
        }
        return true;
    }

    public static boolean isValidImageFile(File imageFile) {
        if (imageFile == null)
            return false;
        return true;
    }

    public static boolean isValidSelectedDropDownItem(int position) {
        if (position == 0)
            return false;
        return true;
    }
}

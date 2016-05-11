package com.mounica.studytree.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.MessageResponse;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ankur on 5/5/16.
 */
public class User {

    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_REG_NO = "userRegNo";
    public static final String USER_IMAGE_LINK = "userImageLink";

    //public static final String USER_DEFAULT_IMAGE_LINK = "http://192.168.1.5/ankur/uploads/icon-user-default.png";
    public static final String USER_DEFAULT_IMAGE_LINK = "http://mink.netne.net/uploads/icon-user-default.png";

    public static void createUser(Context context, int regNo, String name, int age, String email, String contact, String password, final UserCreated callback) {
        RestClient.get().createUser(regNo, name, age, email, contact, password, new Callback<UserCreatedResponse>() {
            @Override
            public void success(UserCreatedResponse userCreatedResponse, Response response) {
                String message = userCreatedResponse.getMessage();
                if (!message.equals("Success"))
                    callback.onUserNotCreated(message);
                else
                    callback.onUserCreated(userCreatedResponse.getUser());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onUserNotCreated("Something went wrong. Please try again later.");
            }
        });
    }

    public static void loginUser(Context context, int regNo, String password, final UserLogin callback) {
        RestClient.get().userLogin(regNo, password, new Callback<UserCreatedResponse>() {
            @Override
            public void success(UserCreatedResponse userCreatedResponse, Response response) {

                String message = userCreatedResponse.getMessage();
                if (!message.equals("Success"))
                    callback.onUserLoginFailed(message);
                else
                    callback.onUserLoginSuccess(userCreatedResponse.getUser());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onUserLoginFailed("Something went wrong. Please try again later.");
            }
        });
    }

    public static void logout(Context context, final UserLogout callback) {
        SharedPreferences credentials = context.getSharedPreferences(User.USER_CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = credentials.edit();
        editor.putInt(User.USER_ID, -1);
        editor.putString(User.USER_REG_NO, null);
        editor.putString(User.USER_NAME, null);
        editor.putString(User.USER_IMAGE_LINK, null);
        editor.commit();
        callback.onLogoutSuccess();
    }

    public static int getLoggedInUserId(Context c) {
        SharedPreferences credentials = c.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getInt(USER_ID, -1);
    }

    public static String getLoggedInUserRegNo(Context c) {
        SharedPreferences credentials = c.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_REG_NO, null);
    }

    public static String getLoggedInUserName(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_NAME, null);
    }

    public static String getLoggedInUserImageLink(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_IMAGE_LINK, USER_DEFAULT_IMAGE_LINK);
    }

    public interface UserCreated {
        void onUserCreated(UserResponse userResponse);
        void onUserNotCreated(String message);
    }

    public interface UserLogin {
        void onUserLoginSuccess(UserResponse userResponse);
        void onUserLoginFailed(String message);
    }

    public interface UserLogout {
        void onLogoutSuccess();
        void onLogoutFailed();
    }

}

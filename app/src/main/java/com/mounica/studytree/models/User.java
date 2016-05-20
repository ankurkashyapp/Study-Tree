package com.mounica.studytree.models;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.ImageUploadResponse;
import com.mounica.studytree.api.response.MessageResponse;
import com.mounica.studytree.api.response.TeacherListResponse;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by ankur on 5/5/16.
 */
public class User{

    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_REG_NO = "userRegNo";
    public static final String USER_IMAGE_LINK = "userImageLink";
    public static final String USER_AGE = "userAge";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_CONTACT = "userContact";
    public static final String USER_PASSWORD = "userPassword";

    //public static final String USER_DEFAULT_IMAGE_LINK = "http://192.168.1.5/ankur/uploads/icon-user-default.png";
    public static final String USER_DEFAULT_IMAGE_LINK = "http://mink.netne.net/uploads/icon-user-default.png";
    public static final String IMAGE_ROOT_PATH = "http://mink.netne.net/uploads/";

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

    public static void updateProfilePic(Context context, File image, final UserProfilePicUpdate callback) {

        TypedFile typedImage = new TypedFile("application/octet-stream", image);
        int userId = User.getLoggedInUserId(context);
        RestClient.get().updateProfilePic(typedImage, userId, new Callback<ImageUploadResponse>() {
            @Override
            public void success(ImageUploadResponse imageUploadResponse, Response response) {
                if (imageUploadResponse.getError())
                    callback.onUpdatedFailure(imageUploadResponse.getMessage());
                else
                    callback.onUpdatedSuccess(imageUploadResponse.getFilePath());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onUpdatedFailure("Something went wrong. Please try again later");
            }
        });
    }

    public static void updateSubjects(Context context, ArrayList<Integer> subjectIds, final UserSubjectsUpdate callback) {
        RestClient.get().updateSubjects(User.getLoggedInUserId(context), subjectIds, new Callback<MessageResponse>() {
            @Override
            public void success(MessageResponse messageResponse, Response response) {
                callback.onUpdated(messageResponse.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onUpdated("Something went wrong. Please try again later");
            }
        });
    }

    public static void getTeacherList(Context context, int subjectId, final TeacherListLoad callback) {
        RestClient.get().getTeacherList(subjectId, new Callback<TeacherListResponse>() {
            @Override
            public void success(TeacherListResponse teacherListResponse, Response response) {
                String message = teacherListResponse.getMessage();
                if (!message.equals("Success"))
                    callback.onTeachersLoadFailure(message);
                else
                    callback.onTeachersLoaded(teacherListResponse.getTeachers());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onTeachersLoadFailure("Something went wrong. Please try again later");
            }
        });
    }

    public static void getUserById(Context context, int userId, final UserById callback) {
        RestClient.get().getUserById(userId, new Callback<UserCreatedResponse>() {
            @Override
            public void success(UserCreatedResponse userCreatedResponse, Response response) {
                callback.onUserLoadedSuccess(userCreatedResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onUserLoadedFailure("Something went wrong. Please try again later");
            }
        });
    }

    public static void logout(Context context, final UserLogout callback) {
        SharedPreferences credentials = context.getSharedPreferences(User.USER_CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = credentials.edit();
        editor.putInt(USER_ID, -1);
        editor.putString(USER_REG_NO, null);
        editor.putString(USER_NAME, null);
        editor.putString(USER_AGE, null);
        editor.putString(USER_EMAIL, null);
        editor.putString(USER_CONTACT, null);
        editor.putString(USER_PASSWORD, null);
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

    public static String getLoggedInUserAge(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_AGE, null);
    }

    public static String getLoggedInUserEmail(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_EMAIL, null);
    }

    public static String getLoggedInUserContact(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_CONTACT, null);
    }

    public static String getLoggedInUserPassword(Context context) {
        SharedPreferences credentials = context.getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE);
        return credentials.getString(USER_PASSWORD, null);
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

    public interface UserProfilePicUpdate {
        void onUpdatedSuccess(String path);
        void onUpdatedFailure(String message);
    }

    public interface UserSubjectsUpdate {
        void onUpdated(String message);
    }

    public interface TeacherListLoad {
        void onTeachersLoaded(List<UserResponse> teachers);
        void onTeachersLoadFailure(String message);
    }

    public interface UserById {
        void onUserLoadedSuccess(UserCreatedResponse userCreatedResponse);
        void onUserLoadedFailure(String message);
    }

}
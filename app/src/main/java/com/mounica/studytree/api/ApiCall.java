package com.mounica.studytree.api;

import com.mounica.studytree.api.response.FeedResponse;
import com.mounica.studytree.api.response.ImageUploadResponse;
import com.mounica.studytree.api.response.MessageResponse;
import com.mounica.studytree.api.response.Products;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by ankur on 2/5/16.
 */
public interface ApiCall {

    @GET("/newjson.php")
    void getAllProducts(Callback<List<Products>> callback);

    @GET("/create_user.php")
    void createUser(@Query("reg_no") int regNo, @Query("name") String name, @Query("age") int age, @Query("email") String email, @Query("contact") String contact, @Query("password") String password, Callback<UserCreatedResponse> callback);

    @GET("/user_login.php")
    void userLogin(@Query("reg_no") int regNo, @Query("password") String password, Callback<UserCreatedResponse> callback);

    @Multipart
    @POST("/image_upload.php")
    void uploadImage(@Part("image") TypedFile image, @Part("user_id") int userId, @Part("title") String title, @Part("description") String description, @Part("subject") int subject, @Part("category") int category, Callback<ImageUploadResponse> callback);

    @GET("/show_feed.php")
    void showFeed(Callback<List<FeedResponse>> callback);
}

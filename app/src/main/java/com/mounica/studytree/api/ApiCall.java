package com.mounica.studytree.api;

import com.mounica.studytree.api.response.FeedResponse;
import com.mounica.studytree.api.response.GetCommentResponse;
import com.mounica.studytree.api.response.ImageUploadResponse;
import com.mounica.studytree.api.response.MessageResponse;
import com.mounica.studytree.api.response.Products;
import com.mounica.studytree.api.response.TeacherListResponse;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
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

    @Multipart
    @POST("/update_profile_pic.php")
    void updateProfilePic(@Part("image") TypedFile image, @Part("user_id") int userId, Callback<ImageUploadResponse> callback);

    @FormUrlEncoded
    @POST("/update_subjects.php")
    void updateSubjects(@Field("user_id") int userId, @Field("subject_ids[]") ArrayList<Integer> subjectIds, Callback<MessageResponse> callback);

    @GET("/show_teachers.php")
    void getTeacherList(@Query("subject_id") int subjectId, Callback<TeacherListResponse> callback);

    @GET("/get_user.php")
    void getUserById(@Query("user_id") int userId, Callback<UserCreatedResponse> callback);

    @FormUrlEncoded
    @POST("/post_comment.php")
    void postComment(@Field("feed_id") int feedId, @Field("user_id") int userId, @Field("comment") String comment, Callback<GetCommentResponse> callback);

    @GET("/get_comments.php")
    void getComments(@Query("feed_id") int feedId, Callback<GetCommentResponse> callback);
}

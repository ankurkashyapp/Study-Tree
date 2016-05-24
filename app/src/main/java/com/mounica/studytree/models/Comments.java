package com.mounica.studytree.models;

import android.content.Context;
import android.util.Log;

import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.CommentsResponse;
import com.mounica.studytree.api.response.GetCommentResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ankur on 23/5/16.
 */
public class Comments {

    public static void postComments(Context context, int feedId, int userId, String comment, final CommentsLoadListener callback) {
        Log.e("Comments", comment);
        RestClient.get().postComment(feedId, userId, comment, new Callback<GetCommentResponse>() {
            @Override
            public void success(GetCommentResponse getCommentResponse, Response response) {
                String message = getCommentResponse.getMessage();
                if (!message.equals("Success"))
                    callback.onCommentsNotLoaded(message);
                else
                    callback.onCommentsLoaded(getCommentResponse.getComments());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onCommentsNotLoaded("Something went wrong. Please try again later");
            }
        });
    }

    public static void getComments(Context context, int feedId, final CommentsLoadListener callback) {
        RestClient.get().getComments(feedId, new Callback<GetCommentResponse>() {
            @Override
            public void success(GetCommentResponse getCommentResponse, Response response) {
                String message = getCommentResponse.getMessage();
                if (!message.equals("Success"))
                    callback.onCommentsNotLoaded(message);
                else
                    callback.onCommentsLoaded(getCommentResponse.getComments());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onCommentsNotLoaded("Something went wrong. Please try again later");
            }
        });
    }

    public interface CommentsLoadListener {
        void onCommentsLoaded(List<CommentsResponse> commentsResponse);
        void onCommentsNotLoaded(String message);
    }

}

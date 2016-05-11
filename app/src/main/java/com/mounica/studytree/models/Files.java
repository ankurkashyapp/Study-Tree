package com.mounica.studytree.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.ImageUploadResponse;
import com.mounica.studytree.api.response.MessageResponse;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by ankur on 6/5/16.
 */
public class Files {


    public static void uploadImage(final Context context, File image, String title, String description, int subject, int category, final ImageUpload callback) {
        TypedFile typedImage = new TypedFile("application/octet-stream", image);
        int userId = User.getLoggedInUserId(context);

        RestClient.get().uploadImage(typedImage, userId, title, description, subject , category, new Callback<ImageUploadResponse>() {
            @Override
            public void success(ImageUploadResponse imageUploadResponse, Response response) {
                Toast.makeText(context, imageUploadResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onImageUploaded(imageUploadResponse.getFilePath());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("Files", "ImageUpload: "+error.getMessage());
            }
        });
    }

    public interface ImageUpload {
        void onImageUploaded(String path);
    }
}

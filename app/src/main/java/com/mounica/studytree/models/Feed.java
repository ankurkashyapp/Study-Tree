package com.mounica.studytree.models;

import android.content.Context;
import android.widget.Toast;

import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.FeedResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ankur on 10/5/16.
 */
public class Feed {

    public static void getFeed(final Context context, final FeedLoaded callback) {

        Toast.makeText(context, "Getting Feeds", Toast.LENGTH_SHORT).show();

        RestClient.get().showFeed(new Callback<List<FeedResponse>>() {
            @Override
            public void success(List<FeedResponse> feedResponses, Response response) {
                if (feedResponses.size()  >0)
                    callback.onFeedFound(feedResponses);
                else
                    callback.onFeedEmpty();
                Toast.makeText(context, "Feeds Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFeedEmpty();
                Toast.makeText(context, "Feeds not Loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface FeedLoaded {
        void onFeedFound(List<FeedResponse> feedResponse);
        void onFeedEmpty();
    }
}

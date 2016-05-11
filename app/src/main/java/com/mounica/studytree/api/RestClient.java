package com.mounica.studytree.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.mounica.studytree.api.response.UserResponse;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

/**
 * This class is a singleton and is used to configure retorfit.
 * @author
 */

public class RestClient {

    private static ApiCall REST_CLIENT=null;
    //private static String ROOT = "http://192.168.1.5/ankur";
    private static String ROOT = "http://mink.netne.net";

    static {
        setupRestClient();
    }

    private RestClient() {}

    public static ApiCall get() {
        return REST_CLIENT;
    }

    public static void setupRestClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(40, TimeUnit.SECONDS);
        client.setConnectTimeout(40, TimeUnit.SECONDS);

        /*Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();*/



        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();


        if(REST_CLIENT==null) {
            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(ROOT)
                    .setLogLevel(RestAdapter.LogLevel.FULL);

            RestAdapter restAdapter2 = builder.build();
            REST_CLIENT = restAdapter2.create(ApiCall.class);
        }
    }
}
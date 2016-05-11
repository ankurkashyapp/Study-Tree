package com.mounica.studytree.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mounica.studytree.R;
import com.mounica.studytree.adapters.DrawerViewAdapter;
import com.mounica.studytree.adapters.FeedAdapter;
import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.FeedResponse;
import com.mounica.studytree.api.response.Products;
import com.mounica.studytree.models.Feed;
import com.mounica.studytree.models.Files;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DrawerViewAdapter.ProfileEditListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SuperRecyclerView feedsView;
    private FloatingActionButton imageUpload;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        recyclerView = (RecyclerView)findViewById(R.id.drawer_view);
        feedsView = (SuperRecyclerView)findViewById(R.id.feeds);
        imageUpload = (FloatingActionButton)findViewById(R.id.upload_image);
        logout = (Button)findViewById(R.id.user_logout);

        imageUpload.setOnClickListener(this);
        logout.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupDrawerToggle();
        setupDrawerRecyclerView();
        setupFeedRecyclerView();
    }

    private void setupDrawerRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DrawerViewAdapter adapter = new DrawerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFeedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedsView.setLayoutManager(layoutManager);

        Feed.getFeed(this, new Feed.FeedLoaded() {
            @Override
            public void onFeedFound(List<FeedResponse> feedResponse) {
                FeedAdapter feedAdapter = new FeedAdapter(getApplicationContext(), feedResponse);
                feedsView.setAdapter(feedAdapter);
            }

            @Override
            public void onFeedEmpty() {
                Toast.makeText(MainActivity.this, "No Feed Found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void setupDrawerToggle(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
    }

    @Override
    public void onClick(View view) {
        if (view == imageUpload) {
            startActivity(new Intent(this, UploadImageActivity.class));
        }
        else if (view == logout) {
            logoutUser();
        }
    }

    private void logoutUser() {
        User.logout(this, new User.UserLogout() {
            @Override
            public void onLogoutSuccess() {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                finish();
            }

            @Override
            public void onLogoutFailed() {

            }
        });
    }


    @Override
    public void onEditProfileClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(new Intent(this, ProfileActivity.class));
    }
}

package com.mounica.studytree.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mounica.studytree.R;
import com.mounica.studytree.adapters.DrawerViewAdapter;
import com.mounica.studytree.adapters.FeedAdapter;
import com.mounica.studytree.api.response.FeedResponse;
import com.mounica.studytree.models.Feed;
import com.mounica.studytree.models.User;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DrawerViewAdapter.ProfileEditListener,
        DrawerViewAdapter.MenuItemListener, FeedAdapter.DownloadListener, FeedAdapter.CommentClickListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SuperRecyclerView feedsView;
    private FloatingActionButton imageUpload;
    private Button logout;

    private ProgressDialog progressDialog;

    private List<FeedResponse> feedResponse;

    private String imageUrl, imageName;

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
        DrawerViewAdapter adapter = new DrawerViewAdapter(this, this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFeedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedsView.setLayoutManager(layoutManager);

        feedsView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillFeedData();
            }
        });

        fillFeedData();
    }

    private void fillFeedData() {
        Feed.getFeed(this, new Feed.FeedLoaded() {
            @Override
            public void onFeedFound(List<FeedResponse> feedResponse) {
                MainActivity.this.feedResponse = feedResponse;
                FeedAdapter feedAdapter = new FeedAdapter(getApplicationContext(), feedResponse, MainActivity.this, MainActivity.this);
                feedsView.setAdapter(feedAdapter);
            }

            @Override
            public void onFeedEmpty() {
                Toast.makeText(MainActivity.this, "No Feed Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setupDrawerToggle() {
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
                startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
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

    @Override
    public void onMenuItemClicked(View view) {
        int clickedPosition = recyclerView.getChildLayoutPosition(view);

        if (clickedPosition == 2) {
            startActivity(new Intent(this, TeachersSubjectsActivity.class));
        }
    }

    @Override
    public void onCommentClicked(View itemView) {
        int clickedPosition = feedsView.getRecyclerView().getChildLayoutPosition(itemView);
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.FEED_ID, feedResponse.get(clickedPosition).getId());
        startActivity(intent);
    }

    @Override
    public void onDownloadClicked(View itemView) {
        int clickedPosition = feedsView.getRecyclerView().getChildLayoutPosition(itemView);
        imageName = feedResponse.get(clickedPosition).getImage();
        imageUrl = User.IMAGE_ROOT_PATH + feedResponse.get(clickedPosition).getImage();
        new DownloadTask().execute();
    }

    public void DownloadFromUrl() {
        File SDCardRoot = Environment.getExternalStorageDirectory();
        int count;

        try {

            File mydir = new File(Environment.getExternalStorageDirectory() + "/StudyTree/");
            if(!mydir.exists())
                mydir.mkdirs();
            else
                Log.e("error", "dir. already exists");

            String fileName = imageName.lastIndexOf(":") == -1 ? imageName : "StudyTree_" + imageName.substring(imageName.lastIndexOf(":")+1);
            File file = new File(SDCardRoot, "StudyTree/"+fileName);
            Log.e("MainActivity", "ImageUrl: "+imageUrl);
            URL url = new URL(imageUrl);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading. Please wait...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }



    private class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DownloadFromUrl();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            Toast.makeText(MainActivity.this, "Saved in StudyTree folder", Toast.LENGTH_SHORT).show();
        }
    }
}
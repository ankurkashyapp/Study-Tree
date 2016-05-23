package com.mounica.studytree.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mounica.studytree.R;
import com.mounica.studytree.adapters.CommentsAdapter;
import com.mounica.studytree.api.response.CommentsResponse;
import com.mounica.studytree.models.Comments;
import com.mounica.studytree.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankur on 23/5/16.
 */
public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FEED_ID = "feedId";
    private Toolbar toolbar;
    private SuperRecyclerView commentsView;
    private EditText createComment;
    private Button submitComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initViews();
        loadComments();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        commentsView = (SuperRecyclerView)findViewById(R.id.comments_list);
        createComment = (EditText)findViewById(R.id.comment_create);
        submitComment = (Button)findViewById(R.id.submit_comment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsView.setLayoutManager(layoutManager);

        commentsView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadComments();
            }
        });

        submitComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submitComment) {
            createComment.setText(null);
            postComment();
        }
    }

    private void loadComments() {
        int feedId = Integer.parseInt(getIntent().getStringExtra(FEED_ID));
        Comments.getComments(this, feedId, new Comments.CommentsLoadListener() {
            @Override
            public void onCommentsLoaded(List<CommentsResponse> commentsResponse) {
                CommentsAdapter commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentsResponse);
                commentsView.setAdapter(commentsAdapter);
            }

            @Override
            public void onCommentsNotLoaded(String message) {
                Toast.makeText(CommentsActivity.this, message, Toast.LENGTH_SHORT).show();
                List<CommentsResponse> commentsResponse = new ArrayList<CommentsResponse>();
                CommentsAdapter commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentsResponse);
                commentsView.setAdapter(commentsAdapter);
            }
        });
    }

    private void postComment() {
        int feedId = Integer.parseInt(getIntent().getStringExtra(FEED_ID));
        int userId = User.getLoggedInUserId(this);
        String comment = createComment.getText().toString();

        Comments.postComments(this, feedId, userId, comment, new Comments.CommentsLoadListener() {
            @Override
            public void onCommentsLoaded(List<CommentsResponse> commentsResponse) {
                CommentsAdapter commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentsResponse);
                commentsView.setAdapter(commentsAdapter);
            }

            @Override
            public void onCommentsNotLoaded(String message) {
                Toast.makeText(CommentsActivity.this, message, Toast.LENGTH_SHORT).show();
                List<CommentsResponse> commentsResponse = new ArrayList<CommentsResponse>();
                CommentsAdapter commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentsResponse);
                commentsView.setAdapter(commentsAdapter);
            }
        });
    }

}

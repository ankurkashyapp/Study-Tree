package com.mounica.studytree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.mounica.studytree.R;
import com.mounica.studytree.api.response.FeedResponse;
import com.mounica.studytree.api.response.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ankur on 10/5/16.
 */
public class FeedAdapter extends BaseSwipeAdapter<BaseSwipeAdapter.BaseSwipeableViewHolder> {

    public static final String imageRootPath = "http://mink.netne.net/uploads/";

    private Context context;
    private List<FeedResponse> feedResponse;

    private DownloadListener downloadListener;
    private CommentClickListener commentClickListener;

    public FeedAdapter(Context context, List<FeedResponse> feedResponse, DownloadListener downloadListener, CommentClickListener commentClickListener) {
        this.context = context;
        this.feedResponse = feedResponse;
        this.downloadListener = downloadListener;
        this.commentClickListener = commentClickListener;
    }

    @Override
    public BaseSwipeableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View feedImage = LayoutInflater.from(context).inflate(R.layout.item_view_feed, parent, false);
        return new FeedImageHolder(feedImage, downloadListener, commentClickListener);
    }

    @Override
    public void onBindViewHolder(BaseSwipeableViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);

        FeedResponse feed = feedResponse.get(position);
        UserResponse user = feed.getUser();

        FeedImageHolder feedImageHolder = (FeedImageHolder)holder;

        Picasso.with(context).load(imageRootPath + feed.getImage()).into(feedImageHolder.feedImage);
        feedImageHolder.feedTitle.setText(feed.getTitle());
        feedImageHolder.feedDescription.setText(feed.getDescription());
        feedImageHolder.feedUserName.setText(user.getName());
        feedImageHolder.feedUserRegNo.setText(user.getReg_no());
        Picasso.with(context).load(imageRootPath+user.getImage_path()).into(feedImageHolder.feedUserImage);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return feedResponse.size();
    }

    public class FeedImageHolder extends BaseSwipeableViewHolder {

        public ImageView feedImage;
        public TextView feedTitle;
        public TextView feedDescription;
        public TextView feedUserName;
        public TextView feedUserRegNo;
        public ImageView feedUserImage;

        public RelativeLayout feedDownloadFile;
        public RelativeLayout feedComment;

        public FeedImageHolder(final View itemView, final DownloadListener downloadListener, final CommentClickListener commentClickListener) {
            super(itemView);
            feedImage = (ImageView)itemView.findViewById(R.id.feed_image);
            feedTitle = (TextView)itemView.findViewById(R.id.feed_image_title);
            feedDescription = (TextView)itemView.findViewById(R.id.feed_image_description);
            feedUserName = (TextView)itemView.findViewById(R.id.feed_user_name);
            feedUserRegNo = (TextView)itemView.findViewById(R.id.feed_user_reg_no);
            feedUserImage = (ImageView) itemView.findViewById(R.id.feed_user_image);
            feedComment = (RelativeLayout)itemView.findViewById(R.id.feed_comment);
            feedDownloadFile = (RelativeLayout)itemView.findViewById(R.id.feed_download_file);


            feedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentClickListener.onCommentClicked(itemView);
                }
            });
            feedDownloadFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadListener.onDownloadClicked(itemView);
                }
            });
        }

    }

    public interface DownloadListener {
        void onDownloadClicked(View itemView);
    }

    public interface CommentClickListener {
        void onCommentClicked(View itemView);
    }
}

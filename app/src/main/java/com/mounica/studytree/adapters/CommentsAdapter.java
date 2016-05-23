package com.mounica.studytree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.mounica.studytree.R;
import com.mounica.studytree.api.response.CommentsResponse;
import com.mounica.studytree.api.response.GetCommentResponse;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ankur on 23/5/16.
 */
public class CommentsAdapter extends BaseSwipeAdapter<BaseSwipeAdapter.BaseSwipeableViewHolder> {

    private Context context;
    private List<CommentsResponse> comments;

    public CommentsAdapter(Context context, List<CommentsResponse> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public BaseSwipeableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseSwipeableViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);
        CommentsResponse comment = comments.get(position);
        UserResponse user = comment.getUser();

        CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
        Picasso.with(context).load(User.IMAGE_ROOT_PATH + user.getImage_path()).into(commentViewHolder.commentorImage);
        commentViewHolder.commentorName.setText(user.getName());
        commentViewHolder.comment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private class CommentViewHolder extends BaseSwipeableViewHolder {

        public ImageView commentorImage;
        public TextView commentorName;
        public TextView comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentorImage = (ImageView)itemView.findViewById(R.id.commentor_image);
            commentorName = (TextView)itemView.findViewById(R.id.commentor_name);
            comment = (TextView)itemView.findViewById(R.id.comment);
        }
    }
}

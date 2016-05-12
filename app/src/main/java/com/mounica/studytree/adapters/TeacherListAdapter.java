package com.mounica.studytree.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.mounica.studytree.R;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ankur on 12/5/16.
 */
public class TeacherListAdapter extends BaseSwipeAdapter<BaseSwipeAdapter.BaseSwipeableViewHolder> {

    private Context context;
    private List<UserResponse> userResponse;
    private ViewProfileListener viewProfileListener;

    public TeacherListAdapter(Context context, List<UserResponse> userResponse, ViewProfileListener viewProfileListener) {
        this.context = context;
        this.userResponse = userResponse;
        this.viewProfileListener = viewProfileListener;
    }

    @Override
    public BaseSwipeableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_teachers, parent, false);
        return new SubjectTeacherHolder(view, viewProfileListener);
    }

    @Override
    public void onBindViewHolder(BaseSwipeableViewHolder holder, int position) {
        UserResponse user = userResponse.get(position);
        SubjectTeacherHolder teacherHolder = (SubjectTeacherHolder)holder;

        Picasso.with(context).load(User.IMAGE_ROOT_PATH + user.getImage_path()).into(teacherHolder.teacherImage);
        teacherHolder.teacherName.setText(user.getName());
        teacherHolder.teacherRegNo.setText(user.getReg_no());

    }

    @Override
    public int getItemCount() {
        return userResponse.size();
    }

    private class SubjectTeacherHolder extends BaseSwipeableViewHolder {

        public ImageView teacherImage;
        public TextView teacherName;
        public TextView teacherRegNo;
        public Button teacherViewProfile;

        public SubjectTeacherHolder(final View itemView, final ViewProfileListener viewProfileListener) {
            super(itemView);
            teacherImage = (ImageView)itemView.findViewById(R.id.teacher_image);
            teacherName = (TextView)itemView.findViewById(R.id.teacher_name);
            teacherRegNo = (TextView)itemView.findViewById(R.id.teacher_reg_no);
            teacherViewProfile = (Button)itemView.findViewById(R.id.teacher_view_profile);

            teacherViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewProfileListener.onViewProfileClicked(itemView);
                }
            });
        }
    }

    public interface ViewProfileListener {
        void onViewProfileClicked(View itemView);
    }
}

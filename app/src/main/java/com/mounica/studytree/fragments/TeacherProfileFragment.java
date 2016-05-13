package com.mounica.studytree.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.SubjectsResponse;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ankur on 12/5/16.
 */
public class TeacherProfileFragment extends Fragment {

    public static final String USER_ID = "userId";

    private int userId;

    private ProgressBar progressBar;

    private ImageView teacherImage;
    private TextView teacherName;
    private TextView teacherRegNo;
    private TextView teacherAge;
    private TextView teacherEmail;
    private TextView teacherContact;

    private LinearLayout linearLayout;

    private UserCreatedResponse teacherAllDetails;
    private UserResponse user;
    private List<SubjectsResponse> subjectsList;

    public static TeacherProfileFragment newInstance(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID, userId);

        TeacherProfileFragment teacherProfileFragment = new TeacherProfileFragment();
        teacherProfileFragment.setArguments(bundle);
        return teacherProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);
        initViews(view);
        loadUserData();
        return view;
    }

    private void initViews(View view) {
        userId = getArguments().getInt(USER_ID, 1);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        teacherImage = (ImageView)view.findViewById(R.id.teacher_image_front);
        teacherName = (TextView)view.findViewById(R.id.teacher_name);
        teacherRegNo = (TextView)view.findViewById(R.id.teacher_reg_no);
        teacherAge = (TextView)view.findViewById(R.id.teacher_age);
        teacherEmail = (TextView)view.findViewById(R.id.teacher_email);
        teacherContact = (TextView)view.findViewById(R.id.teacher_contact);

        linearLayout = (LinearLayout)view.findViewById(R.id.subject_group);


    }

    private void loadUserData() {
        User.getUserById(getActivity(), userId, new User.UserById() {
            @Override
            public void onUserLoadedSuccess(UserCreatedResponse userCreatedResponse) {
                teacherAllDetails = userCreatedResponse;
                user = userCreatedResponse.getUser();
                subjectsList = userCreatedResponse.getSubjects();
                fillUserData();
                hideProgressBar();
            }

            @Override
            public void onUserLoadedFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });
    }

    private void fillUserData() {
        Picasso.with(getActivity())
                .load(User.IMAGE_ROOT_PATH + user.getImage_path())
                .into(teacherImage);

        teacherName.setText(user.getName());
        teacherRegNo.setText(user.getReg_no());
        teacherAge.setText(user.getAge());
        teacherEmail.setText(user.getEmail());
        teacherContact.setText(user.getContact());
        fillSubject();
    }

    private void fillSubject() {
        int i, j=0;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(20, 0, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        layoutParams.gravity = Gravity.CENTER;
        for (i=0; i<Math.floor(subjectsList.size()/2); i++) {
            LinearLayout linearLayout2 = new LinearLayout(getActivity());
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout2.setLayoutParams(layoutParams);
            TextView textView = new TextView(getActivity());
            textView.setText(subjectsList.get(j).getSubject_name());
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_transparent_light));
            textView.setPadding(15, 5, 15, 5);
            j++;
            TextView textView2 = new TextView(getActivity());
            textView2.setText(subjectsList.get(j).getSubject_name());
            textView2.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            textView2.setGravity(Gravity.CENTER);
            textView2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_transparent_light));
            textView2.setPadding(15, 5, 15, 5);

            textView2.setLayoutParams(layoutParams2);
            linearLayout2.addView(textView);

            linearLayout2.addView(textView2);

            linearLayout.addView(linearLayout2);
            j++;
        }

        if (subjectsList.size() % 2  != 0) {
            TextView textView3 = new TextView(getActivity());
            textView3.setText(subjectsList.get(j).getSubject_name());
            textView3.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            textView3.setGravity(Gravity.CENTER);
            textView3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_transparent_light));
            textView3.setPadding(15, 5, 15, 5);
            textView3.setLayoutParams(layoutParams);
            linearLayout.addView(textView3);
        }
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}

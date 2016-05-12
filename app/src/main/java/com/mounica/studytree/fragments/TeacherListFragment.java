package com.mounica.studytree.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mounica.studytree.R;
import com.mounica.studytree.activity.TeacherProfileView;
import com.mounica.studytree.adapters.TeacherListAdapter;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;

import java.util.List;

/**
 * Created by ankur on 12/5/16.
 */
public class TeacherListFragment extends Fragment implements TeacherListAdapter.ViewProfileListener{

    public static final String SUBJECT_ID = "subjectId";

    private int subjectId;

    private SuperRecyclerView superRecyclerView;

    private List<UserResponse> teachersList;

    public static TeacherListFragment newInstance(int subjectId) {
        Bundle bundle = new Bundle();
        bundle.putInt(SUBJECT_ID, subjectId);
        TeacherListFragment teacherListFragment = new TeacherListFragment();
        teacherListFragment.setArguments(bundle);
        return teacherListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        initViews(view);
        loadTeachers();
        return view;
    }

    private void initViews(View view) {
        subjectId = getArguments().getInt(SUBJECT_ID, 1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        superRecyclerView = (SuperRecyclerView)view.findViewById(R.id.super_recycler_teachers);
        superRecyclerView.setLayoutManager(layoutManager);
        superRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTeachers();
            }
        });

    }

    private void loadTeachers() {
        User.getTeacherList(getActivity(), subjectId, new User.TeacherListLoad() {
            @Override
            public void onTeachersLoaded(List<UserResponse> teachers) {
                teachersList = teachers;
                TeacherListAdapter teacherListAdapter = new TeacherListAdapter(getActivity(), teachersList, TeacherListFragment.this);
                superRecyclerView.setAdapter(teacherListAdapter);
            }

            @Override
            public void onTeachersLoadFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewProfileClicked(View itemView) {
        int clickedPosition = superRecyclerView.getRecyclerView().getChildLayoutPosition(itemView);
        int userId = Integer.parseInt(teachersList.get(clickedPosition).getUser_id());
        Intent intent = new Intent(getActivity(), TeacherProfileView.class);
        intent.putExtra(TeacherProfileView.USER_ID, userId);
        startActivity(intent);
    }
}

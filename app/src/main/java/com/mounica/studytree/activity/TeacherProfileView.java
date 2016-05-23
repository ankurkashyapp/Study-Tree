package com.mounica.studytree.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mounica.studytree.R;
import com.mounica.studytree.fragments.ProfileEditFragment;
import com.mounica.studytree.fragments.SubjectsTeachFragment;
import com.mounica.studytree.fragments.TeacherAppointmentFragment;
import com.mounica.studytree.fragments.TeacherProfileFragment;

/**
 * Created by ankur on 12/5/16.
 */
public class TeacherProfileView extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public static final String USER_ID = "userId";

    private int userId;

    private Toolbar toolbar;
    private TabLayout profileTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile_view);
        initViews();
    }

    private void initViews() {
        userId = getIntent().getIntExtra(USER_ID, 1);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        profileTabs = (TabLayout)findViewById(R.id.profile_tabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupTabs();
    }

    private void setupTabs() {
        profileTabs.addTab(profileTabs.newTab().setText("Teacher Profile"), true);
        profileTabs.addTab(profileTabs.newTab().setText("Take Appointment"));

        profileTabs.getTabAt(0).select();
        replaceFragment(TeacherProfileFragment.newInstance(userId));
        profileTabs.setOnTabSelectedListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_profile_tabs, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            replaceFragment(TeacherProfileFragment.newInstance(userId));
        }
        else if (tab.getPosition() == 1) {
            replaceFragment(TeacherAppointmentFragment.newInstance(userId));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}

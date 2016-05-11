package com.mounica.studytree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mounica.studytree.R;
import com.mounica.studytree.fragments.LoginFragment;
import com.mounica.studytree.fragments.ProfileEditFragment;
import com.mounica.studytree.fragments.RegisterFragment;
import com.mounica.studytree.fragments.SubjectsTeachFragment;
import com.mounica.studytree.models.User;

/**
 * Created by ankur on 11/5/16.
 */
public class ProfileActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private Toolbar toolbar;
    private TabLayout profileTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        profileTabs = (TabLayout)findViewById(R.id.profile_tabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupTabs();
    }

    private void setupTabs() {
        profileTabs.addTab(profileTabs.newTab().setText("Your Profile"), true);
        profileTabs.addTab(profileTabs.newTab().setText("Subjects you teach"));

        profileTabs.getTabAt(0).select();
        replaceFragment(ProfileEditFragment.newInstance());
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
            replaceFragment(ProfileEditFragment.newInstance());
        }
        else if (tab.getPosition() == 1) {
            replaceFragment(SubjectsTeachFragment.newInstance());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}

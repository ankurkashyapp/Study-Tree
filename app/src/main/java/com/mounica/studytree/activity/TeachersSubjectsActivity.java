package com.mounica.studytree.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mounica.studytree.R;
import com.mounica.studytree.adapters.ViewPagerAdapter;
import com.mounica.studytree.fragments.TeacherListFragment;

/**
 * Created by ankur on 12/5/16.
 */
public class TeachersSubjectsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_subjects);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(TeacherListFragment.newInstance(1), "C/C++");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(2), "Java");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(3), "Operating System");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(4), "Data Structure");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(5), "Algorithm");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(6), "Database");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(7), "Maths");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(8), "Physics");
        pagerAdapter.addFragment(TeacherListFragment.newInstance(9), "Chemistry");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

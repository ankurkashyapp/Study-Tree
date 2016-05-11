package com.mounica.studytree.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mounica.studytree.R;
import com.mounica.studytree.fragments.LoginFragment;
import com.mounica.studytree.fragments.RegisterFragment;

/**
 * Created by ankur on 5/5/16.
 */
public class UserLoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public static final String TAB_POSITION = "tabPosition";

    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        setupTabs();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Login"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Register"));

        int tabPosition = getIntent().getIntExtra(TAB_POSITION, 0);
        tabLayout.getTabAt(tabPosition).select();

        if (tabPosition == 0){
            replaceFragment(LoginFragment.newInstance());
        }
        else {
            replaceFragment(RegisterFragment.newInstance());
        }
        tabLayout.setOnTabSelectedListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_login_register_fragment, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            replaceFragment(loginFragment);
        }
        else if (tab.getPosition() == 1) {
            RegisterFragment registerFragment = RegisterFragment.newInstance();
            replaceFragment(registerFragment);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}

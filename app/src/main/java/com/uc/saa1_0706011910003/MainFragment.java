package com.uc.saa1_0706011910003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.saa1_0706011910003.fragment.AccountFragment;
import com.uc.saa1_0706011910003.fragment.CourseFragment;
import com.uc.saa1_0706011910003.fragment.ScheduleFragment;

public class MainFragment extends AppCompatActivity {

    Toolbar toolbar;
//    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    String action="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        toolbar = findViewById(R.id.toolbar_main_frag);
        toolbar.setTitle(R.string.menu_home);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        action = intent.getStringExtra("edit");

        if(action.equalsIgnoreCase("sch_frag")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, new ScheduleFragment());
            transaction.commit();
        } else if(action.equalsIgnoreCase("edit_acc")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, new AccountFragment());
            transaction.commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_navbar_frag);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.schedule_frag_menu:
                        toolbar.setTitle(R.string.menu_schedule);
                        setSupportActionBar(toolbar);
                        fragment = new ScheduleFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.course_frag_menu:
                        toolbar.setTitle(R.string.menu_courses);
                        setSupportActionBar(toolbar);
                        fragment = new CourseFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.setting_frag_menu:
                        toolbar.setTitle(R.string.menu_settings);
                        setSupportActionBar(toolbar);
                        fragment = new AccountFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.schedule_frag_menu);

    }


}

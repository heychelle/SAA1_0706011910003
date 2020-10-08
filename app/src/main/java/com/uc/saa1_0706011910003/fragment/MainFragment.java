package com.uc.saa1_0706011910003.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uc.saa1_0706011910003.R;

public class MainFragment extends AppCompatActivity {

    Toolbar toolbar;
//    Fragment fragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);


        toolbar = findViewById(R.id.toolbar_main_frag);
        toolbar.setTitle(R.string.menu_home);
        setSupportActionBar(toolbar);

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

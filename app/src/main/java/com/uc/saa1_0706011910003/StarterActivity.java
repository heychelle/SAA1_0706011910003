package com.uc.saa1_0706011910003;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class StarterActivity extends AppCompatActivity {

    ImageView button_add_student, button_add_lecturer, button_add_course, button_login_student;
    Intent intent;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        firebaseAuth = FirebaseAuth.getInstance();
        button_add_student = findViewById(R.id.button_add_student);
        button_add_lecturer = findViewById(R.id.button_add_lecturer);
        button_add_course = findViewById(R.id.button_add_course);
        button_login_student = findViewById(R.id.button_login_student);

        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(StarterActivity.this,"Welcome Back", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StarterActivity.this, MainFragment.class);
            intent.putExtra("edit", "edit_acc");
            startActivity(intent);
            finish();
        }

        button_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                intent = new Intent (StarterActivity.this, StudentRegister.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_add_lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                intent = new Intent (StarterActivity.this, AddLecturer.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                intent = new Intent (StarterActivity.this, AddCourse.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_login_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                intent = new Intent (StarterActivity.this, StudentLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to close the apps!", Toast.LENGTH_SHORT).show();
    }
}

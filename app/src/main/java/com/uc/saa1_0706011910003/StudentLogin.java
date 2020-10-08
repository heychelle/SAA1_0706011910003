package com.uc.saa1_0706011910003;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class StudentLogin extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    TextInputLayout input_email, input_password;
    String email, password;
    Button button_login_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        toolbar = findViewById(R.id.toolbar_student_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        button_login_student = findViewById(R.id.button_login_student);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogin.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        email = input_email.getEditText().getText().toString().trim();
        password = input_password.getEditText().getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty() ) {
            button_login_student.setEnabled(true);
        } else {
            button_login_student.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

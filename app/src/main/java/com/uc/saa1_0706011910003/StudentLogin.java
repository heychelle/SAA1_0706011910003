package com.uc.saa1_0706011910003;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uc.saa1_0706011910003.fragment.ScheduleFragment;
import com.uc.saa1_0706011910003.model.Student;

public class StudentLogin extends AppCompatActivity implements TextWatcher {

    Toolbar toolbar;
    TextInputLayout input_email, input_password;
    String email, password;
    Button button_login_student;
    FirebaseAuth firebaseAuth;
    Intent intent;
    Dialog dialog;
    SharedPreferences userPref;
    SharedPreferences.Editor userEditor;
    DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        dbUsers = FirebaseDatabase.getInstance().getReference("student");


        dialog = Glovar.loadingDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar_student_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        button_login_student = findViewById(R.id.button_login_student);

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userEditor = userPref.edit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogin.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_login_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                email = input_email.getEditText().getText().toString().trim();
                password = input_password.getEditText().getText().toString().trim();
                //sign in
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.cancel();
                        if(task.isSuccessful()){
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                String utoken = task.getResult();
                                dbUsers.child(firebaseAuth.getCurrentUser().getUid()).child("token").setValue(utoken);
                                userEditor.putString("utoken", utoken);
                                userEditor.commit();
                                }
                            });
                            Toast.makeText(StudentLogin.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                            intent = new Intent(StudentLogin.this, MainFragment.class);
                            intent.putExtra("edit", "sch_frag");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(StudentLogin.this,"Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

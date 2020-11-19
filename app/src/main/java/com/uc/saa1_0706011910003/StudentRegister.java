package com.uc.saa1_0706011910003;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.adapter.StudentAdapter;
import com.uc.saa1_0706011910003.fragment.AccountFragment;
import com.uc.saa1_0706011910003.model.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity implements TextWatcher {

    TextInputLayout input_email, input_password, input_name, input_nim, input_age, input_address;
    String uid="", email="", password="", name="", nim="", age="", gender="male", address="", token="",action="";
    Button button_register;
    Toolbar toolbar;
    Dialog dialog;
    RadioButton radio_button;
    RadioGroup radio_group;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reg);

        dialog = Glovar.loadingDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //path
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        toolbar = findViewById(R.id.toolbar_student_register);
        setSupportActionBar(toolbar);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);
        input_nim = findViewById(R.id.input_nim);

        radio_group = findViewById(R.id.radio_gender_student);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radio_button = findViewById(i);
                gender = radio_button.getText().toString();
            }
        });

        input_age = findViewById(R.id.input_age);
        input_address = findViewById(R.id.input_address);
        button_register = findViewById(R.id.button_register);

        //implements text watcher
        input_email.getEditText().addTextChangedListener(this);
        input_password.getEditText().addTextChangedListener(this);
        input_name.getEditText().addTextChangedListener(this);
        input_nim.getEditText().addTextChangedListener(this);
        input_age.getEditText().addTextChangedListener(this);
        input_address.getEditText().addTextChangedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRegister.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("add")){
            toolbar.setTitle("Student Register");
            button_register.setText("Register");
            button_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = input_email.getEditText().getText().toString().trim();
                    password = input_password.getEditText().getText().toString().trim();
                    name = input_name.getEditText().getText().toString().trim();
                    nim = input_nim.getEditText().getText().toString().trim();
                    age = input_age.getEditText().getText().toString().trim();
                    address = input_address.getEditText().getText().toString().trim();
                    addStudent(email,password,name,nim,gender,age,address,token);
                }
            });
        }else {
            toolbar.setTitle("Edit Student");
            button_register.setText("Edit");
            input_email.getEditText().setEnabled(false);
            input_password.getEditText().setEnabled(false);

            student = intent.getParcelableExtra("edit_data_stud");
            input_email.getEditText().setText(student.getEmail());
            input_password.getEditText().setText(student.getPassword());
            input_name.getEditText().setText(student.getName());
            input_nim.getEditText().setText(student.getNim());
            input_age.getEditText().setText(student.getAge());
            input_address.getEditText().setText(student.getAddress());

            if (student.getGender().equalsIgnoreCase("male")) {
                radio_group.check(R.id.radio_male);
            } else {
                radio_group.check(R.id.radio_female);
            }
            button_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    email = input_email.getEditText().getText().toString().trim();
                    password = input_password.getEditText().getText().toString().trim();
                    name = input_name.getEditText().getText().toString().trim();
                    nim = input_nim.getEditText().getText().toString().trim();
                    age = input_age.getEditText().getText().toString().trim();
                    address = input_address.getEditText().getText().toString().trim();

                    Map<String, Object> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("name", name);
                    params.put("nim", nim);
                    params.put("gender", gender);
                    params.put("age", age);
                    params.put("address", address);
                    mDatabase.child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            Toast.makeText(StudentRegister.this, "Student Data Updated Successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(StudentRegister.this, StudentData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    //add data student
    public void addStudent(String memail, String mpassword, String mname, String mnim, String mgender, String mage, String maddress, String mtoken){
        getFormValue();
        dialog.show();
        //klo kita buat email,password dia auto sign in sehingga kita hrs sign out
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.cancel();
                    uid = mAuth.getCurrentUser().getUid();
                    Student student = new Student(uid,email,password,name,nim,gender,age,address,"-");
                    mDatabase.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mAuth.signOut();
                            Toast.makeText(StudentRegister.this, "Student Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent (StudentRegister.this, StudentData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthInvalidCredentialsException malFormed){
                        Toast.makeText(StudentRegister.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthUserCollisionException existEmail){
                        Toast.makeText(StudentRegister.this, "Email Already Registered", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(StudentRegister.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent(this, StarterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(intent, options.toBundle());
                finish();
                return true;
            }
            case R.id.student_list: {
                Log.d("genshin", "masuk");
                Intent intent = new Intent(StudentRegister.this, StudentData.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getFormValue(){
        email = input_email.getEditText().getText().toString().trim();
        password = input_password.getEditText().getText().toString().trim();
        name = input_name.getEditText().getText().toString().trim();
        nim = input_nim.getEditText().getText().toString().trim();
        age = input_age.getEditText().getText().toString().trim();
        address = input_address.getEditText().getText().toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = input_email.getEditText().getText().toString().trim();
            password = input_password.getEditText().getText().toString().trim();
            name = input_name.getEditText().getText().toString().trim();
            nim = input_nim.getEditText().getText().toString().trim();
            age = input_age.getEditText().getText().toString().trim();
            address = input_address.getEditText().getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty() ) {
                button_register.setEnabled(true);
            } else {
                button_register.setEnabled(false);
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent, options.toBundle());
        finish();
    }
}

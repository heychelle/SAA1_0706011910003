package com.uc.saa1_0706011910003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.TextView;
import android.widget.Toast;

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
import com.uc.saa1_0706011910003.fragment.AccountFragment;
import com.uc.saa1_0706011910003.model.Student;

import java.util.HashMap;
import java.util.Map;

public class EditAccount extends AppCompatActivity implements TextWatcher {


    TextInputLayout input_name, input_nim, input_age, input_address;
    TextView input_email;
    String uid="", email="", password="", name="", nim="", age="", gender="male", address="", action="";
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
        setContentView(R.layout.activity_edit_account);

        dialog = Glovar.loadingDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        toolbar = findViewById(R.id.toolbar_edit_acc);
        setSupportActionBar(toolbar);

        input_email = findViewById(R.id.email_acc);
        input_name = findViewById(R.id.name_acc);
        input_nim = findViewById(R.id.nim_acc);

        radio_group = findViewById(R.id.rg_acc);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radio_button = findViewById(i);
                gender = radio_button.getText().toString();
            }
        });

        input_age = findViewById(R.id.age_acc);
        input_address = findViewById(R.id.address_acc);
        button_register = findViewById(R.id.button_edit_acc);

        //implements text watcher
        input_name.getEditText().addTextChangedListener(this);
        input_nim.getEditText().addTextChangedListener(this);
        input_age.getEditText().addTextChangedListener(this);
        input_address.getEditText().addTextChangedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccount.this, MainFragment.class);
                intent.putExtra("edit", "edit_acc");
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("edit_profile")){
            toolbar.setTitle("Edit Student");
            button_register.setText("Edit");

            student = intent.getParcelableExtra("edit_data_profile");
            input_email.setText(student.getEmail());
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
                    email = input_email.getText().toString().trim();
                    name = input_name.getEditText().getText().toString().trim();
                    nim = input_nim.getEditText().getText().toString().trim();
                    age = input_age.getEditText().getText().toString().trim();
                    address = input_address.getEditText().getText().toString().trim();

                    Map<String, Object> params = new HashMap<>();
                    params.put("email", email);
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
                            Toast.makeText(EditAccount.this, "Account Data Updated Successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(EditAccount.this, MainFragment.class);
                            intent.putExtra("edit", "edit_acc");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditAccount.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });

        } else {

        }
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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        name = input_name.getEditText().getText().toString().trim();
        nim = input_nim.getEditText().getText().toString().trim();
        age = input_age.getEditText().getText().toString().trim();
        address = input_address.getEditText().getText().toString().trim();

        if (!name.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty() ) {
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

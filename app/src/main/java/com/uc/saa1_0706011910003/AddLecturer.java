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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.model.Lecturer;

import java.util.HashMap;
import java.util.Map;

public class AddLecturer extends AppCompatActivity implements TextWatcher {

    TextInputLayout input_name_lecturer, input_expertise;
    RadioButton radio_button;
    RadioGroup radio_gender;
    Button button_add_lecturer;
    Dialog dialog;
    Toolbar toolbar;
    Intent intent;
    Lecturer lecturer;
    String name = "", expertise = "", gender = "male", action = "";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = Glovar.loadingDialog(this);
        radio_button = findViewById(R.id.radio_male);
        toolbar = findViewById(R.id.toolbar_add_lecturer);
        setSupportActionBar(toolbar);
        input_name_lecturer = findViewById(R.id.input_name_lecturer);
        radio_gender = findViewById(R.id.radio_gender);
        radio_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radio_button = findViewById(i);
                gender = radio_button.getText().toString();
            }
        });
        input_expertise = findViewById(R.id.input_expertise);
        input_name_lecturer.getEditText().addTextChangedListener(this);
        input_expertise.getEditText().addTextChangedListener(this);
        button_add_lecturer = findViewById(R.id.button_add_lecturer);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLecturer.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //set radio button by default
        radio_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radio_button = findViewById(i);
                gender = radio_button.getText().toString();
            }
        });

        button_add_lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name_lecturer.getEditText().getText().toString().trim();
                expertise = input_expertise.getEditText().getText().toString().trim();
                AddLecturerActivity(name, gender, expertise);
            }
        });

        intent = getIntent();
        action = intent.getStringExtra("action");

        if(action.equalsIgnoreCase("add"))
        {
            getSupportActionBar().setTitle(R.string.add_lecturer);
            button_add_lecturer.setText(R.string.add_lecturer);
            button_add_lecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = input_name_lecturer.getEditText().getText().toString().trim();
                    expertise = input_expertise.getEditText().getText().toString().trim();
                    AddLecturerActivity(name, gender, expertise);
                }
            });
        }else
        { //saat activity dari lecturer detail & mau mengupdate data
            getSupportActionBar().setTitle(R.string.edit_lecturer);
            lecturer = intent.getParcelableExtra("edit_data_lect");
            input_name_lecturer.getEditText().setText(lecturer.getName());
            input_expertise.getEditText().setText(lecturer.getExpertise());
            if (lecturer.getGender().equalsIgnoreCase("male")) {
                radio_gender.check(R.id.radio_female);
            } else {
                radio_gender.check(R.id.radio_female);
            }
            button_add_lecturer.setText(R.string.edit_lecturer);
            button_add_lecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    name = input_name_lecturer.getEditText().getText().toString().trim();
                    expertise = input_expertise.getEditText().getText().toString().trim();
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expertise", expertise);
                    params.put("gender", gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(AddLecturer.this, LecturerData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    //add lecturer
    public void AddLecturerActivity(String mnama, String mgender, String mexpertise) {
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, mnama, mgender, mexpertise);
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                intent = new Intent(AddLecturer.this, StarterActivity.class);
                startActivity(intent);
                dialog.cancel();
                Toast.makeText(AddLecturer.this, "Add Lecturer Successfully", Toast.LENGTH_SHORT).show();
                input_name_lecturer.getEditText().setText("");
                input_expertise.getEditText().setText("");
                radio_gender.check(R.id.radio_male);

                Log.d("masuk", "hua cry");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tidakmasuk", "hua cry");

                Toast.makeText(AddLecturer.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        name = input_name_lecturer.getEditText().getText().toString().trim();
        expertise = input_expertise.getEditText().getText().toString().trim();
        gender = radio_button.getText().toString();

        if (!name.isEmpty() && !expertise.isEmpty() && !gender.isEmpty()) {
            button_add_lecturer.setEnabled(true);
        } else {
            button_add_lecturer.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu pojok kanan
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //lecturer_list ini icon yang ada di package menu layout lecturer_menu
        if (id == android.R.id.home) {
            Intent intent;
            intent = new Intent(this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        } else if (id == R.id.lecturer_list) {
            Intent intent;
            intent = new Intent(this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent, options.toBundle());
        finish();
    }

}

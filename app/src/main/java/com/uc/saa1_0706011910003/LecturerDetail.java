package com.uc.saa1_0706011910003;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.model.Lecturer;

import java.util.ArrayList;

public class LecturerDetail extends AppCompatActivity {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Intent intent;
    ImageView button_delete,button_edit;
    TextView text_name, text_gender, text_expertise;
    Lecturer lecturer;
    Toolbar toolbar;
    DatabaseReference dbLecturer;
    ArrayList<Lecturer> listLecturer = new ArrayList<>();
    int pos = 0;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_detail);
        toolbar = findViewById(R.id.toolbar_lect_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        text_name = findViewById(R.id.name_lect_detail);
        text_gender = findViewById(R.id.gender_lect_detail);
        text_expertise = findViewById(R.id.expertise_lect_detail);
        button_edit = findViewById(R.id.edit_lect_data);
        button_delete = findViewById(R.id.delete_lect_data);
        dialog = Glovar.loadingDialog(LecturerDetail.this);

        intent = getIntent();
        pos = intent.getIntExtra("position",0);
        lecturer = intent.getParcelableExtra("data_lecturer");

        text_name.setText(lecturer.getName());
        text_gender.setText(lecturer.getGender());
        text_expertise.setText(lecturer.getExpertise());

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(LecturerDetail.this)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.logo2)
                        .setMessage("Are you sure to delete "+lecturer.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        dbLecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Intent in = new Intent(LecturerDetail.this, LecturerData.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                Toast.makeText(LecturerDetail.this, "Delete success!", Toast.LENGTH_SHORT).show();
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                                                startActivity(in, options.toBundle());
                                                finish();
                                                dialogInterface.cancel();
                                            }
                                        });

                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });


        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(LecturerDetail.this, AddLecturer.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_lect", lecturer);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                startActivity(in, options.toBundle());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerDetail.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerDetail.this, LecturerData.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}

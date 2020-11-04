package com.uc.saa1_0706011910003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.saa1_0706011910003.adapter.LecturerAdapter;
import com.uc.saa1_0706011910003.adapter.StudentAdapter;
import com.uc.saa1_0706011910003.model.Student;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar toolbar;
    DatabaseReference dbStudent;
    ArrayList<Student> listStudent = new ArrayList<>();
    RecyclerView rv_stud_data;
    Student student;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);
        toolbar = findViewById(R.id.toolbar_stud_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //path
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        rv_stud_data = findViewById(R.id.rv_stud_data);
        fetchStudentData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StudentData.this, StudentRegister.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //ambil data
    public void fetchStudentData(){
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                    rv_stud_data.setAdapter(null);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //show data
    public void showStudentData(final ArrayList<Student> list){
        rv_stud_data.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        rv_stud_data.setAdapter(studentAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            intent = new Intent(StudentData.this, StudentRegister.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }else{
            intent = new Intent(StudentData.this, StudentRegister.class);
            intent.putExtra("action", "edit_stud");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(StudentData.this, StudentRegister.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
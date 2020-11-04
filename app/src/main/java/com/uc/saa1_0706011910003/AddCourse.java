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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.saa1_0706011910003.model.Course;
import com.uc.saa1_0706011910003.model.Lecturer;
import com.uc.saa1_0706011910003.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddCourse extends AppCompatActivity implements TextWatcher{

    Toolbar toolbar;
    Spinner spinner_day, spinner_start, spinner_end, spinner_lecturer;
    String id="", subject="", day="", time1="", time2="", lecturer="", action="";
    TextInputLayout input_course_subject;
    Button button_add_course;
    Course course;
    Dialog dialog;
    DatabaseReference mDatabase;
    List<String> lecturer_array;
    ArrayAdapter<CharSequence> adapterend;
    ArrayList<Lecturer> lecturerArrayList;
    DatabaseReference dbStudent;
    DatabaseReference dbCourse;
    DatabaseReference dbCourses;
    DatabaseReference dbLect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //path di firebase
        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        dbLect = FirebaseDatabase.getInstance().getReference("lecturer");

        dialog = Glovar.loadingDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar_add_course);
        input_course_subject = findViewById(R.id.input_course_subject);
        spinner_day = findViewById(R.id.spinner_day);
        spinner_start = findViewById(R.id.spinner_time1);
        spinner_end = findViewById(R.id.spinner_time2);
        spinner_lecturer = findViewById(R.id.spinner_lect);
        button_add_course = findViewById(R.id.button_add_course);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourse.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //set spinner
        spinner_day = findViewById(R.id.spinner_day);
        ArrayAdapter<CharSequence> adapterdays = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterdays);

        spinner_lecturer = findViewById(R.id.spinner_lect);
        final ArrayAdapter<CharSequence> adapterlecturers = ArrayAdapter.createFromResource(this, R.array.lecturers, android.R.layout.simple_spinner_item);
        adapterlecturers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lecturer.setAdapter(adapterlecturers);

        spinner_start = findViewById(R.id.spinner_time1);
        ArrayAdapter<CharSequence> adaptertimes1 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adaptertimes1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adaptertimes1);

        spinner_end = findViewById(R.id.spinner_time2);
//        ArrayAdapter<CharSequence> adaptertimes2 =  ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
//        adaptertimes2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 adapterend = null;
                 setSpinner_end(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //akses lecturer utk dapetin namenya yg di set ke spinner
        lecturer_array = new ArrayList<>();
        mDatabase.child("lecturer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    String firebase_lecturer = childSnapshot.child("name").getValue(String.class);
                    lecturer_array.add(firebase_lecturer);
                }
                ArrayAdapter<String> adapterlecturers = new ArrayAdapter<>(AddCourse.this, android.R.layout.simple_spinner_item,lecturer_array);
                adapterlecturers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_lecturer.setAdapter(adapterlecturers);
                if (action.equalsIgnoreCase("edit_data_course")){
                    int lectIndex = adapterlecturers.getPosition(course.getLecturer());
                    spinner_lecturer.setSelection(lectIndex);
                    Log.d("lecturer", String.valueOf(lectIndex)+course.getLecturer());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //klo masuk dr starter
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.add_course);
            button_add_course.setText("Add");
            button_add_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subject = input_course_subject.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    time1 = spinner_start.getSelectedItem().toString();
                    time2 = spinner_end.getSelectedItem().toString();
                    lecturer = spinner_lecturer.getSelectedItem().toString();
                    addCourse(subject,day,time1,time2,lecturer);
                }
            });
        }
        //masuk dari button edit di adapter
        else {
            getSupportActionBar().setTitle(R.string.edit_course);
            button_add_course.setText("Edit");
            course = intent.getParcelableExtra("edit_data_course");

            //set text pada spinner dan subject
            input_course_subject.getEditText().setText(course.getSubject());

            int dayIndex = adapterdays.getPosition(course.getDay());
            spinner_day.setSelection(dayIndex);

            int startIndex = adaptertimes1.getPosition(course.getStart());
            spinner_start.setSelection(startIndex);

            setSpinner_end(startIndex);
            int endIndex = adapterend.getPosition(course.getEnd());
            spinner_end.setSelection(endIndex);

            button_add_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    subject = input_course_subject.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    time1 = spinner_start.getSelectedItem().toString();
                    time2 = spinner_end.getSelectedItem().toString();
                    lecturer = spinner_lecturer.getSelectedItem().toString();

                    Map<String, Object> params = new HashMap<>();
                    params.put("subject", subject);
                    params.put("day", day);
                    params.put("start", time1);
                    params.put("end", time2);
                    params.put("lecturer", lecturer);

                    dbCourse.child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        //                    mDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            checkCourse(course.getId());
                            Intent intent;
                            Toast.makeText(AddCourse.this, "Course Data Updated Successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(AddCourse.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addCourse(String msubject, String mday, String mtime1, String mtime2, String mlecturer){
            String mid = mDatabase.child("course").push().getKey();
            Course course = new Course(mid, msubject, mday, mtime1, mtime2, mlecturer);
            mDatabase.child("course").child(mid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dialog.cancel();
                    Toast.makeText(AddCourse.this, "Add Course Successfully", Toast.LENGTH_SHORT).show();
                    //ketika button di click, set smua back to awal
                    input_course_subject.getEditText().setText("");
                    spinner_day.setSelection(0);
                    spinner_start.setSelection(0);
                    spinner_end.setSelection(0);
                    spinner_lecturer.setSelection(0);

                    Log.d("masuk", "hua cry");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("tidakmasuk", "hua cry");

                    Toast.makeText(AddCourse.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        subject = input_course_subject.getEditText().getText().toString().trim();
        day = spinner_day.getSelectedItem().toString();
        time1 = spinner_start.getSelectedItem().toString();
        time2 = spinner_end.getSelectedItem().toString();
        lecturer = spinner_lecturer.getSelectedItem().toString();

        if (!subject.isEmpty()) {
            button_add_course.setEnabled(true);
        } else {
            button_add_course.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu pojok kanan
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent;
            intent = new Intent(this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        } else if (id == R.id.course_list) {
            Intent intent;
            intent = new Intent(AddCourse.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddCourse.this, StarterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    //isi spinner time
    public void setSpinner_end(int position){
        if(position==0){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
        }else if(position==1){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
        }else if(position==2){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
        }else if(position==3){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
        }else if(position==4){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
        }else if(position==5){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
        }else if(position==6){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
        }else if(position==7){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
        }else if(position==8){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
        }else if(position==9){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
        }else if(position==10){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
        }else if(position==11){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
        }else if(position==12){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
        }else if(position==13){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
        }else if(position==14){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
        }else if(position==15){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
        }else if(position==16){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
        }else if(position==17){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
        }else if(position==18){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
        }

        adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_end.setAdapter(adapterend);
    }
    public void checkCourse(final String check) {
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //baca dengan loop id nya
                for (DataSnapshot stud : snapshot.getChildren()) {
                    dbCourses = dbStudent.child(stud.getValue(Student.class).getUid()).child("courses");
                    dbCourses.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot cr : snapshot.getChildren()) {
                                cr.getValue(Course.class).getId();
                                if (check.equals(cr.getValue(Course.class).getId())) {
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("subject", subject);
                                    params.put("day", day);
                                    params.put("start", time1);
                                    params.put("end", time2);
                                    params.put("lecturer", lecturer);
                                    dbCourses.child(cr.getValue(Course.class).getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

package com.uc.saa1_0706011910003;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;


public class AddCourse extends AppCompatActivity implements TextWatcher{

    Toolbar toolbar;
    Spinner spinner_day, spinner_start, spinner_end, spinner_lecturer;
    TextInputLayout input_course_subject;
    String course, day, time1, time2, lecturer;
    Button button_add_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        toolbar = findViewById(R.id.toolbar_add_course);
        input_course_subject = findViewById(R.id.input_course_subject);
        spinner_day = findViewById(R.id.spinner_day);
        spinner_start = findViewById(R.id.spinner_time1);
        spinner_end = findViewById(R.id.spinner_time2);
        spinner_lecturer = findViewById(R.id.spinner_lect);
        button_add_course = findViewById(R.id.button_add_course);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourse.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spinner_day = findViewById(R.id.spinner_day);
        ArrayAdapter<CharSequence> adapterdays = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterdays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterdays);

        spinner_lecturer = findViewById(R.id.spinner_lect);
        ArrayAdapter<CharSequence> adapterlecturers = ArrayAdapter.createFromResource(this, R.array.lecturers, android.R.layout.simple_spinner_item);
        adapterlecturers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lecturer.setAdapter(adapterlecturers);

        spinner_start = findViewById(R.id.spinner_time1);
        ArrayAdapter<CharSequence> adaptertimes1 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adaptertimes1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adaptertimes1);

        spinner_end = findViewById(R.id.spinner_time2);

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapterend = null;
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddCourse.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        course = input_course_subject.getEditText().getText().toString().trim();
        day = spinner_day.getSelectedItem().toString();
        time1 = spinner_start.getSelectedItem().toString();
        time2 = spinner_end.getSelectedItem().toString();
        lecturer = spinner_lecturer.getSelectedItem().toString();

        if (!course.isEmpty() && !day.isEmpty() && !time1.isEmpty() && !time2.isEmpty() && !lecturer.isEmpty()) {
            button_add_course.setEnabled(true);
        } else {
            button_add_course.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

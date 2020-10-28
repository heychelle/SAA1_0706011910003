package com.uc.saa1_0706011910003.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.StarterActivity;
import com.uc.saa1_0706011910003.adapter.CourseAdapter;
import com.uc.saa1_0706011910003.adapter.ScheduleAdapter;
import com.uc.saa1_0706011910003.model.Course;
import com.uc.saa1_0706011910003.model.Student;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    ImageView no_data_sch;
    RecyclerView recyclerView;
    Adapter ScheduleAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Course course;
    DatabaseReference dbCourse;
    ImageView imageView;
    ArrayList<Course> listCourse = new ArrayList<>();

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_schedule_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        if (listCourse.isEmpty()){
//            imageView.setVisibility(View.VISIBLE);
//        }else{
//            imageView.setVisibility(View.INVISIBLE);
//        }


        dbCourse = FirebaseDatabase.getInstance().getReference("course");
//        no_data_sch = (ImageView) getView().findViewById(R.id.image_no_sch);
        recyclerView = getView().findViewById(R.id.rv_schedule_frag);

//        no_data_sch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Input Your Schedule First!", Toast.LENGTH_SHORT).show();
//            }
//        });
        fetchCourseData();
    }
    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                    recyclerView.setAdapter(null);
                }
                showCourseData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourseData(final ArrayList<Course> list){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleAdapter ScheduleAdapter = new ScheduleAdapter(getActivity());
        ScheduleAdapter.setListCourse(list);
        recyclerView.setAdapter(ScheduleAdapter);

    }
}

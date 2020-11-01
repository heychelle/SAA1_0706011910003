package com.uc.saa1_0706011910003.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.saa1_0706011910003.CourseData;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.adapter.CourseAdapter;
import com.uc.saa1_0706011910003.adapter.CourseFragmentAdapter;
import com.uc.saa1_0706011910003.model.Course;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    ImageView no_data_cour;
    Course course;
    RecyclerView recyclerView;
    Adapter ScheduleAdapter;
    DatabaseReference dbCourse;
    ImageView imageView;
    ArrayList<Course> listCourse = new ArrayList<>();


    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        no_data_cour = (ImageView) getView().findViewById(R.id.image_no_course);
        recyclerView = getView().findViewById(R.id.rv_frag_course);

        no_data_cour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Input Your Course First!", Toast.LENGTH_SHORT).show();
            }
        });
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
                if (listCourse.isEmpty()){
                    no_data_cour.setVisibility(View.VISIBLE);
                }else{
                    no_data_cour.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showCourseData(final ArrayList<Course> list){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CourseFragmentAdapter CourseFragmentAdapter = new CourseFragmentAdapter(getActivity());
        CourseFragmentAdapter.setListCourse(list);
        recyclerView.setAdapter(CourseFragmentAdapter);

        final Observer<Course> crAddObserver = new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                FirebaseDatabase.getInstance().getReference().child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").child(course.getId()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Add Course Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Add Course Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        CourseFragmentAdapter.getCourseAdd().observe(this , crAddObserver);

    }
}
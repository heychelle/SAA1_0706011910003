package com.uc.saa1_0706011910003.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.AddCourse;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.model.Course;

import java.util.ArrayList;

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseFragmentAdapter.CardViewViewHolder> {

    private Context context;
    DatabaseReference dbCourse;
    Dialog dialog;
    int pos = 0;
    private ArrayList<Course> listCourse;
    private ArrayList<Course> getListCourse() {
        return listCourse;
    }
    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }
    public CourseFragmentAdapter(Context context) {
        this.context = context;
    }
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @NonNull
    @Override
    public CourseFragmentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_course_fragment_adapter, parent, false);
        return new CourseFragmentAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseFragmentAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.crSubject.setText(course.getSubject());
        holder.crDay.setText(course.getDay());
        holder.crStart.setText(course.getStart());
        holder.crEnd.setText(course.getEnd());
        holder.crLecturer.setText(course.getLecturer());


        holder.button_enroll.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
//                Intent in = new Intent(context, AddCourse.class);
//                in.putExtra("action", "edit_stud");
//                in.putExtra("edit_data_stud", course);
//                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(in);
//                finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {

            TextView crSubject, crDay, crStart, crEnd, crLecturer;
            ImageView button_enroll;

            CardViewViewHolder(View itemView) {
                super(itemView);
                crSubject = itemView.findViewById(R.id.course_subject_frag);
                crDay = itemView.findViewById(R.id.course_day_frag);
                crStart = itemView.findViewById(R.id.course_start_frag);
                crEnd = itemView.findViewById(R.id.course_end_frag);
                crLecturer = itemView.findViewById(R.id.course_lect_frag);

                dbCourse = FirebaseDatabase.getInstance().getReference("course");

                dialog = Glovar.loadingDialog(context);

                button_enroll = itemView.findViewById(R.id.button_enroll);

            }

    }
}
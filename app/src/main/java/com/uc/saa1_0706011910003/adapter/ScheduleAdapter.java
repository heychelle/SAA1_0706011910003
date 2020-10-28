package com.uc.saa1_0706011910003.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.AddCourse;
import com.uc.saa1_0706011910003.CourseData;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.model.Course;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CardViewViewHolder> {

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
    public ScheduleAdapter(Context context) {
        this.context = context;
    }

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @NonNull
    @Override
    public ScheduleAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_schedule_adapter, parent, false);
        return new ScheduleAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ScheduleAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.crSubject.setText(course.getSubject());
        holder.crDay.setText(course.getDay());
        holder.crStart.setText(course.getStart());
        holder.crEnd.setText(course.getEnd());
        holder.crLecturer.setText(course.getLecturer());


        holder.button_delete.setOnClickListener(new View.OnClickListener(){

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

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView crSubject, crDay, crStart, crEnd, crLecturer;
        ImageView button_delete;

        CardViewViewHolder(View itemView) {
            super(itemView);
            crSubject = itemView.findViewById(R.id.course_subject);
            crDay = itemView.findViewById(R.id.days_course);
            crStart = itemView.findViewById(R.id.time_start_course);
            crEnd = itemView.findViewById(R.id.time_end_course);
            crLecturer = itemView.findViewById(R.id.lect_course);

            dbCourse = FirebaseDatabase.getInstance().getReference("course");

            dialog = Glovar.loadingDialog(context);

            button_delete = itemView.findViewById(R.id.delete_course);

        }

    }

}

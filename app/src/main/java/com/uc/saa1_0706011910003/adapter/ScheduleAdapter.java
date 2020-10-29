package com.uc.saa1_0706011910003.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.AddCourse;
import com.uc.saa1_0706011910003.CourseData;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.LecturerData;
import com.uc.saa1_0706011910003.LecturerDetail;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.model.Course;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CardViewViewHolder> {

    private Context context;
    DatabaseReference dbStudent;
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
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.logo2)
                        .setMessage("Are you sure to delete "+course.getSubject()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dbStudent.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(context, "Course has been removed", Toast.LENGTH_SHORT).show();
                                            dialogInterface.cancel();
                                            Log.d("id course", course.getId());

                                            }
                                        });
                                        dialog.cancel();
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

            dbStudent = FirebaseDatabase.getInstance().getReference("student");

            dialog = Glovar.loadingDialog(context);

            button_delete = itemView.findViewById(R.id.delete_course);

        }

    }

}

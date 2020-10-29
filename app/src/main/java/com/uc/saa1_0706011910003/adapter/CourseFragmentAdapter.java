package com.uc.saa1_0706011910003.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.saa1_0706011910003.AddCourse;
import com.uc.saa1_0706011910003.CourseData;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.StudentData;
import com.uc.saa1_0706011910003.model.Course;

import java.util.ArrayList;

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseFragmentAdapter.CardViewViewHolder> {

    private Context context;
    Dialog dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbCourse;
    DatabaseReference dbStudent;
    Course course;
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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

//        ArrayList<Course> finalListCourse = listCourse;

        holder.crSubject.setText(course.getSubject());
        holder.crDay.setText(course.getDay());
        holder.crStart.setText(course.getStart());
        holder.crEnd.setText(course.getEnd());
        holder.crLecturer.setText(course.getLecturer());



        holder.button_enroll.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.logo2)
                        .setMessage("Are you sure to you want to take "+course.getSubject()+" ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        CheckTime(course);
//                                        dbCourse.child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
//                                            @Override
//                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                                Intent in = new Intent(context, CourseData.class);
//                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
////                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context);
////                                                context.startActivity(in, options.toBundle());
//                                                context.startActivity(in);
//                                                ((Activity)context).finish();
//                                                dialogInterface.cancel();
//                                            }
//                                        });

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
    MutableLiveData<Course> courseAdd = new MutableLiveData<>();

    public MutableLiveData<Course> getCourseAdd(){
        return courseAdd;
    }

    boolean conflict = false;

    public void CheckTime(final Course choose){

        final int  courseStart = Integer.parseInt(choose.getStart().replace(":",""));
        final int courseEnd = Integer.parseInt(choose.getEnd().replace(":",""));

        FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        conflict = false;
                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            Course course = childSnapshot.getValue(Course.class);
                            int crStart = Integer.parseInt(course.getStart().replace(":",""));
                            int crEnd = Integer.parseInt(course.getEnd().replace(":",""));

                            if (!choose.getDay().equalsIgnoreCase(course.getDay()) || crStart>=courseStart || crEnd<=courseEnd){
                                conflict = false;
                                Log.d("CheckFalse","False");
                            }else{
                                conflict = true;
                                Log.d("CheckTrue","True");
                                break;
                            }
                        }

                        if (conflict){
                            new AlertDialog.Builder(context)
                                    .setTitle("Warning")
                                    .setIcon(R.drawable.logo2)
                                    .setMessage("You cannot take  "+course.getSubject()+", check again your schedule!")
                                    .setCancelable(false)
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                            dialog.show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.cancel();

                                                }
                                            }, 2000);
                                        }
                                    })
                                    .create()
                                    .show();

                        } else {
                            courseAdd.setValue(choose);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
//                dbStudent = FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses");

                dbCourse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            course = childSnapshot.getValue(Course.class);
                            listCourse.add(course);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog = Glovar.loadingDialog(context);

                button_enroll = itemView.findViewById(R.id.button_enroll);

            }

    }
}
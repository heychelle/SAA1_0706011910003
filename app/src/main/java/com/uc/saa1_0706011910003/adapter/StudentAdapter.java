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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.AddLecturer;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.LecturerData;
import com.uc.saa1_0706011910003.LecturerDetail;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.StarterActivity;
import com.uc.saa1_0706011910003.StudentData;
import com.uc.saa1_0706011910003.StudentRegister;
import com.uc.saa1_0706011910003.model.Lecturer;
import com.uc.saa1_0706011910003.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder>{

    private Context context;
    DatabaseReference dbStudent;
    Dialog dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    int pos = 0;
    private ArrayList<Student> listStudent;
    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }
    public StudentAdapter(Context context) {
        this.context = context;
    }

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);
        holder.studEmail.setText(student.getEmail());
        holder.studName.setText(student.getName());
        holder.studNim.setText(student.getNim());
        holder.studGender.setText(student.getGender());
        holder.studAge.setText(student.getAge());
        holder.studAddress.setText(student.getAddress());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        intent = getIntent();
//        pos = intent.getIntExtra("position",0);
//        student = intent.getParcelableExtra("data_student");

        holder.button_edit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(context, StudentRegister.class);
                in.putExtra("action", "edit_stud");
                in.putExtra("edit_data_stud", student);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(in);
//                finish();
            }
        });
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.diamond2)
                        .setMessage("Are you sure to delete "+student.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();

                                        String uid = student.getUid();
                                        Log.d("cobadong", uid);

                                        firebaseAuth.signInWithEmailAndPassword(student.getEmail(),student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                firebaseUser.delete();

                                                dbStudent.child(student.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Intent in = new Intent(context, StudentData.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                        context.startActivity(in);
                                                        ((Activity)context).finish();
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                            }
                                        });
//                                        dbStudent.child(student.getUid()).removeValue(new DatabaseReference.CompletionListener() {
//                                            @Override
//                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//
//                                                Intent in = new Intent(context, StudentData.class);
//                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
//                                                context.startActivity(in);
////                                                finish();
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


    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView studEmail, studName, studNim, studGender, studAge, studAddress;
        ImageView button_edit, button_delete;

        CardViewViewHolder(View itemView) {
            super(itemView);
            studEmail = itemView.findViewById(R.id.stud_email_card);
            studName = itemView.findViewById(R.id.stud_name_card);
            studNim = itemView.findViewById(R.id.stud_nim_card);
            studGender = itemView.findViewById(R.id.stud_gender_card);
            studAge = itemView.findViewById(R.id.stud_age_card);
            studAddress = itemView.findViewById(R.id.stud_address_card);

            dbStudent = FirebaseDatabase.getInstance().getReference("student");

            dialog = Glovar.loadingDialog(context);

            button_edit = itemView.findViewById(R.id.button_edit_student);
            button_delete = itemView.findViewById(R.id.button_delete_student);
        }

    }

}

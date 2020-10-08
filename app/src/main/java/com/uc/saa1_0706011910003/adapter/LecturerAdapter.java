package com.uc.saa1_0706011910003.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.model.Lecturer;

import java.util.ArrayList;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.CardViewViewHolder>{

    private Context context;
    private ArrayList<Lecturer> listLecturer;
    private ArrayList<Lecturer> getListLecturer() {
        return listLecturer;
    }
    public void setListLecturer(ArrayList<Lecturer> listLecturer) {
        this.listLecturer = listLecturer;
    }
    public LecturerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LecturerAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_adapter, parent, false);
        return new LecturerAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final LecturerAdapter.CardViewViewHolder holder, int position) {
        final Lecturer lecturer = getListLecturer().get(position);
        holder.lectName.setText(lecturer.getName());
        holder.lectGender.setText(lecturer.getGender());
        holder.lectExpertise.setText(lecturer.getExpertise());
    }

    @Override
    public int getItemCount() {
        return getListLecturer().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lectName, lectGender, lectExpertise;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lectName = itemView.findViewById(R.id.lect_name_adapter);
            lectGender = itemView.findViewById(R.id.lect_gender_adapter);
            lectExpertise = itemView.findViewById(R.id.lect_expertise_adapter);

        }
    }
}

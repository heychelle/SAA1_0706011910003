package com.uc.saa1_0706011910003.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.saa1_0706011910003.Glovar;
import com.uc.saa1_0706011910003.LecturerDetail;
import com.uc.saa1_0706011910003.R;
import com.uc.saa1_0706011910003.StarterActivity;
import com.uc.saa1_0706011910003.model.Student;

public class AccountFragment extends Fragment {

    Button signout;
    Dialog dialog;
    Student student;
    TextView text_fname, text_email, text_nim, text_gender, text_age, text_address;
    DatabaseReference dbStudent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbStudent = FirebaseDatabase.getInstance().getReference("student");

//        dialog = Glovar.loadingDialog(AccountFragment.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        text_fname.setText(student.getName());
        text_email.setText(student.getEmail());
        text_nim.setText(student.getNim());
        text_gender.setText(student.getGender());
        text_age.setText(student.getAge());
        text_address.setText(student.getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_account_fragment, container, false);
    }

//    public void logout (View view){
//        FirebaseAuth.getInsance()signOut();
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView text_fname = (TextView) getView().findViewById(R.id.name_frag_stud);
        TextView text_email = (TextView) getView().findViewById(R.id.email_frag_stud);
        TextView text_nim = (TextView) getView().findViewById(R.id.nim_frag_stud);
        TextView text_gender = (TextView) getView().findViewById(R.id.gender_frag_stud);
        TextView text_age = (TextView) getView().findViewById(R.id.age_frag_stud);
        TextView text_address = (TextView) getView().findViewById(R.id.address_frag_stud);

        signout = view.findViewById(R.id.button_signout_frag);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StarterActivity.class);
                startActivity(intent);
            }
        });
    }
}

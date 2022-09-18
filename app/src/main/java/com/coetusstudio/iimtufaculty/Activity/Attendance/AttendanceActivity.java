package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.databinding.ActivityAttendanceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceActivity extends AppCompatActivity {

    ActivityAttendanceBinding binding;
    String facultyName, facultySemester, facultySection, facultySubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseDatabase.getInstance().getReference().child("Faculty Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    try {
                        facultyName = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultyName").getValue(String.class).toString();
                        facultySemester = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySemester").getValue(String.class).toString();
                        facultySection = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();
                        facultySubject = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySubject").getValue(String.class).toString();


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AttendanceActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, Select_Attendance.class);
                startActivity(intent);
            }
        });


        binding.btnViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, Select_ViewAttendance_Date.class);
                intent.putExtra("textViewAttendance","View Attendance");
                intent.putExtra("section", facultySection);
                intent.putExtra("subject", facultySubject);
                startActivity(intent);
            }
        });

        binding.btnUpdateAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, Select_ViewAttendance_Date.class);
                intent.putExtra("textViewAttendance","Update Attendance");
                intent.putExtra("section", facultySection);
                intent.putExtra("subject", facultySubject);
                startActivity(intent);
            }
        });

        binding.btnDeleteAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, Delete_Attendance.class);
                intent.putExtra("section", facultySection);
                intent.putExtra("subject", facultySubject);
                startActivity(intent);
            }
        });

        binding.btnDownloadAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, DownloadAttendanceBySubject.class);
                startActivity(intent);
            }
        });
    }
}
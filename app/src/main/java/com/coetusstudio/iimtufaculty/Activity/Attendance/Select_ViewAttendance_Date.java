package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.databinding.ActivitySelectViewAttendanceDateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Select_ViewAttendance_Date extends AppCompatActivity {

    ActivitySelectViewAttendanceDateBinding binding;
    String facultySubject, facultySection, attendanceDate, textValue;
    DatabaseReference dbSubjectRef, dbDateRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectViewAttendanceDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        dbDateRef = FirebaseDatabase.getInstance().getReference().child("AttendenRecordSheet");
        dbSubjectRef=FirebaseDatabase.getInstance().getReference().child("Faculty Data");
        dbSubjectRef.keepSynced(true);

        Intent intent=getIntent();
        textValue=intent.getStringExtra("textViewAttendance");
        facultySection =intent.getStringExtra("section");
        facultySubject = intent.getStringExtra("subject");

        binding.textViewAttendance.setText(textValue);
        binding.btnViewAttendanceByDate.setText(textValue);

        binding.btnCollectiveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), View_Collective_Attendance.class));
            }
        });


                //Spinner for Date And Time
                final List<String> listSubject=new ArrayList<String>();
                listSubject.add("Select Date (Optional)");

                ArrayAdapter<String> subjectArrayAdapter=new ArrayAdapter<String>(Select_ViewAttendance_Date.this,android.R.layout.simple_spinner_item,listSubject);
                subjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerAttendanceDate.setAdapter(subjectArrayAdapter);

                binding.spinnerAttendanceDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        attendanceDate=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                dbDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dsp :dataSnapshot.child(facultySection).child(facultySubject).getChildren()){

                            //String selectedDate = dsp.getKey();

                            listSubject.add(dsp.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        binding.btnViewAttendanceByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendanceDate.equals("Select Date (Optional)")){
                    Toast.makeText(getApplicationContext(), "Please! Select the Date", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Select_ViewAttendance_Date.this, View_Attendace_ByDate.class);
                    intent.putExtra("facultySubject", facultySubject);
                    intent.putExtra("facultySection", facultySection);
                    intent.putExtra("attendanceDate", attendanceDate);
                    startActivity(intent);
                }
            }
        });

        binding.btnCollectiveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Select_ViewAttendance_Date.this, View_Collective_Attendance.class);
                intent.putExtra("facultySubject",facultySubject);
                intent.putExtra("facultySection",facultySection);
                startActivity(intent);
            }
        });
    }
}
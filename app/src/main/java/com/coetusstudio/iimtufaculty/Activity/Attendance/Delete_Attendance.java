package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.databinding.ActivityDeleteAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Delete_Attendance extends AppCompatActivity {

    ActivityDeleteAttendanceBinding binding;
    String facultySubject, facultySection, attendanceDate;
    DatabaseReference dbDateRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Delete");
        mDialog.setMessage("Attendance Deleting...");
        mDialog.setCanceledOnTouchOutside(false);

        Intent intent=getIntent();
        facultySection =intent.getStringExtra("section");
        facultySubject = intent.getStringExtra("subject");

        dbDateRef = FirebaseDatabase.getInstance().getReference().child("AttendenRecordSheet");
        dbDateRef.keepSynced(true);


                //Spinner for Date And Time
                final List<String> listSubject=new ArrayList<String>();
                listSubject.add("Select Date");

                ArrayAdapter<String> subjectArrayAdapter=new ArrayAdapter<String>(Delete_Attendance.this,android.R.layout.simple_spinner_item,listSubject);
                subjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerAttendanceDelete.setAdapter(subjectArrayAdapter);

                binding.spinnerAttendanceDelete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                            listSubject.add(dsp.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        binding.btnDeleteAttendanceByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
                        dbDateRef.child(facultySection).child(facultySubject).child(attendanceDate).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Attendance Record Delete Successfully", Toast.LENGTH_SHORT).show();
                                    binding.spinnerAttendanceDelete.getEmptyView();
                                }
                            }
                        });
                    }
                });

    }
}
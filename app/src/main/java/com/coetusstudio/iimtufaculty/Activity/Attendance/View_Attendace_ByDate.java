package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivitySelectViewAttendanceDateBinding;
import com.coetusstudio.iimtufaculty.databinding.ActivityViewAttendaceByDateBinding;
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

public class View_Attendace_ByDate extends AppCompatActivity {

    ActivityViewAttendaceByDateBinding binding;
    String facultySubject, facultySection, attendanceDate;
    DatabaseReference dbAttendanceRecordRef;
    ArrayList attendance= new ArrayList<>();
    int count=0;
    ProgressDialog mDialog;
    String mrollno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAttendaceByDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Loading...");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        attendance.clear();

        Intent intent=getIntent();
        facultySubject=intent.getStringExtra("facultySubject");
        facultySection=intent.getStringExtra("facultySection");
        attendanceDate = intent.getStringExtra("attendanceDate");

        dbAttendanceRecordRef= FirebaseDatabase.getInstance().getReference().child("AttendenRecordSheet").child(facultySection).child(facultySubject).child(attendanceDate);
        dbAttendanceRecordRef.keepSynced(true);

        binding.facultySubjectAttendance.setText(facultySubject);
        binding.facultySectionAttendance.setText(facultySection);
        binding.attendanceDate.setText(attendanceDate);

        attendance.add("Student Roll Number"+"                   "+"Attendance");
        dbAttendanceRecordRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dspp :dataSnapshot.getChildren()){
                    String rollnumber,avalue;
                    rollnumber=dspp.getKey();
                    avalue=dspp.child("atvalue").getValue().toString();

                    attendance.add(rollnumber+"                                           "+avalue);

                    count=count+1;
                }
                attendance.add("Total Number Of Student=  "+count);
                listshow(attendance);//this is a function created by me
                mDialog.dismiss();
                count=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void listshow(ArrayList attendancelist) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, attendancelist);
        binding.listviewbydate.setAdapter(adapter);

        binding.listviewbydate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String dataupatevar = parent.getItemAtPosition(position).toString();
                updateattendence(dataupatevar);
                return false;
            }
        });
    }

    public void updateattendence(String dataupdate){

        mrollno=dataupdate.substring(0,11);
        mrollno=mrollno.trim();
        String atvalue=dataupdate.substring(14);
        atvalue=atvalue.trim();
        String atvaltrim=atvalue.substring(0,1);
        final String atvaluemax=atvalue.substring(2,3);


        final AlertDialog.Builder myDialog=new AlertDialog.Builder(View_Attendace_ByDate.this);
        LayoutInflater inflater=LayoutInflater.from(View_Attendace_ByDate.this);
        View mview=inflater.inflate(R.layout.update_attendance,null);
        final AlertDialog dialog=myDialog.create();
        dialog.setView(mview);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView enrollmentno=mview.findViewById(R.id.rollno_update_tv);
        final EditText atstatus=mview.findViewById(R.id.attendance_vale_update);
        Button updatebtn=mview.findViewById(R.id.update_btn);
        Button cancelbtn=mview.findViewById(R.id.cancel_btn);

        enrollmentno.setText(mrollno.trim());
        atstatus.setText(atvaltrim);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String getatvalue=atstatus.getText().toString();
                String updates;
                updates=(getatvalue+"/"+atvaluemax);
                if(getatvalue.isEmpty()){
                    atstatus.setError("Can't leave Empty");
                    return;
                }
                else if((Integer.valueOf(getatvalue))>(Integer.valueOf(atvaluemax))){
                    Toast.makeText(getApplicationContext(),"Value can't be more than "+atvaluemax,Toast.LENGTH_SHORT).show();
                }
                else {

                    mDialog.show();
                    dialog.dismiss();
                    updatte(updates);
                }

            }
        });



    }

    public void updatte(String atval){

        dbAttendanceRecordRef.child(mrollno).child("atvalue").setValue(atval).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Activity.Students.StudentdetailsActivity;
import com.coetusstudio.iimtufaculty.Adapter.StudentAdapter;
import com.coetusstudio.iimtufaculty.Model.AddFaculty;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivitySelectAttendanceBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Select_Attendance extends AppCompatActivity {

    ActivitySelectAttendanceBinding binding;
    String uid;
    TextView dateet;
    DatePickerDialog datePickerDialog;
    String time;
    String datetimestamp;
    String finaldatetime;
    Button attenmid;
    String itemfacsublist;
    Spinner SpinnerAttendenceType;
    String itemattenvalue,ListAttendanceType;
    DatabaseReference dbsubjectref;
    String item_subject, facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");

        dbsubjectref=FirebaseDatabase.getInstance().getReference().child("Faculty Data");
        dbsubjectref.keepSynced(true);

        FirebaseDatabase.getInstance().getReference().child("Faculty Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    try {
                        facultySection = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();
                        item_subject = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySubject").getValue(String.class).toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Select_Attendance.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        attenmid=findViewById(R.id.takeatenmid_btn);
        SpinnerAttendenceType=findViewById(R.id.spinnerattentype);

        dateet=findViewById(R.id.date_et);

        dateet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Select_Attendance.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                datetimestamp=(dayOfMonth+ "-" + (monthOfYear + 1)  + "-" + year);
                                dateet.setText(datetimestamp);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        time = currentTime.format(calForTime.getTime());

        // spinner attendance type
        final List<String> lstfacsub=new ArrayList<String>();
        lstfacsub.add("Select Present Student Only");
        lstfacsub.add("Select Absent Student Only");

        ArrayAdapter<String> AttendanceTypeSubArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstfacsub);
        AttendanceTypeSubArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAttendenceType.setAdapter(AttendanceTypeSubArrayAdapter);

        SpinnerAttendenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListAttendanceType=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        attenmid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timestamppre;

                timestamppre=(datetimestamp+" ("+time+")");
                finaldatetime=timestamppre.toUpperCase();
                calltonextpage(facultySection,item_subject,finaldatetime);

            }
        });


    }

    public void calltonextpage(String facultySection, String item_subject, String finaldatetime){
        if (itemfacsublist=="Select Subject"){
            Toast.makeText(getApplicationContext(),"Please Select Valid Subject",Toast.LENGTH_SHORT).show();
        }
        else if (datetimestamp==null){
            dateet.setError("Please Select Date");
        }
        else if (itemattenvalue=="Select Attendance Weightage"){
            Toast.makeText(getApplicationContext(),"Please Select Attendance Weightage",Toast.LENGTH_SHORT).show();

        }
        else {
                    Intent intent=new Intent(getApplicationContext(), Attendance_TakenBy_Subject.class);
                    intent.putExtra("subid",item_subject);
                    intent.putExtra("timestamp",finaldatetime);
                    //intent.putExtra("attenvalue",itemattenvalue);
                    intent.putExtra("AttendanceType",ListAttendanceType);
                    intent.putExtra("facultySection",facultySection);
                    startActivity(intent);
                    finish();

        }
    }
}
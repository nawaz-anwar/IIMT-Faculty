package com.coetusstudio.iimtufaculty.Activity.Marks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Activity.Home.MainActivity;
import com.coetusstudio.iimtufaculty.Model.AddFaculty;
import com.coetusstudio.iimtufaculty.Model.SessionalMarks;
import com.coetusstudio.iimtufaculty.Model.SessionalTitle;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.databinding.ActivitySessionalMarksBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sessional_Marks extends AppCompatActivity {

    ActivitySessionalMarksBinding binding;
    DatabaseReference reference;
    DatabaseReference dbsubjectref, dbrollref, dbsessionalref, dbnameref;
    String item_subject1, item_subject2, item_subject3, item_subject4, item_subject5, studentRollNumber, sessionalTitle, studentName, facultySection;
    String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySessionalMarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        section = intent.getStringExtra("section");

        reference = FirebaseDatabase.getInstance().getReference().child("SessionalMarks");
        dbsubjectref=FirebaseDatabase.getInstance().getReference().child("Faculty Data");
        dbrollref = FirebaseDatabase.getInstance().getReference().child("Student Data");
        dbnameref = FirebaseDatabase.getInstance().getReference().child("Student Data");
        dbsessionalref= FirebaseDatabase.getInstance().getReference("Sessional");;

        binding.btnSessionalMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtSessionalSub1.toString().isEmpty()) {
                    binding.edtSessionalSub1.setError("Empty");
                    binding.edtSessionalSub1.requestFocus();
                } else {
                    uploadMarks();
                }
            }
        });


        //Spinner for Semester
        final List<String> listDivision=new ArrayList<String>();
        listDivision.add("Result Type");

        ArrayAdapter<String> divisionArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listDivision);
        divisionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSessionalTitle.setAdapter(divisionArrayAdapter);

        binding.spinnerSessionalTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sessionalTitle=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbsessionalref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    SessionalTitle br = dsp.getValue(SessionalTitle.class);

                    listDivision.add(br.getSessionalTitle());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Sessional_Marks.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
        




        //Spinner for StudentRollNumber
        final List<String> listStudentRollNumber=new ArrayList<String>();
        listStudentRollNumber.add("Select Roll Number");

        ArrayAdapter<String> rollNumberArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listStudentRollNumber);
        rollNumberArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentRollNumber.setAdapter(rollNumberArrayAdapter);

        binding.spinnerStudentRollNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentRollNumber=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbrollref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    StudentDetails br = dsp.getValue(StudentDetails.class);

                    listStudentRollNumber.add(br.getStudentRollNumber());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Subject1
        final List<String> listSubject1=new ArrayList<String>();
        listSubject1.add("Select Subject");

        ArrayAdapter<String> subject1ArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSubject1);
        subject1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentSub1.setAdapter(subject1ArrayAdapter);

        binding.spinnerStudentSub1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_subject1=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsubjectref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    AddFaculty br = dsp.getValue(AddFaculty.class);

                    listSubject1.add(br.getFacultySubject());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Subject2
        final List<String> listSubject2=new ArrayList<String>();
        listSubject2.add("Select Subject");

        ArrayAdapter<String> subject2ArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSubject2);
        subject2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentSub2.setAdapter(subject2ArrayAdapter);

        binding.spinnerStudentSub2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_subject2=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsubjectref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    AddFaculty br = dsp.getValue(AddFaculty.class);

                    listSubject2.add(br.getFacultySubject());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Subject3
        final List<String> listSubject3=new ArrayList<String>();
        listSubject3.add("Select Subject");

        ArrayAdapter<String> subject3ArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSubject3);
        subject3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentSub3.setAdapter(subject3ArrayAdapter);

        binding.spinnerStudentSub3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_subject3=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsubjectref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    AddFaculty br = dsp.getValue(AddFaculty.class);

                    listSubject3.add(br.getFacultySubject());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Subject4
        final List<String> listSubject4=new ArrayList<String>();
        listSubject4.add("Select Subject");

        ArrayAdapter<String> subject4ArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSubject4);
        subject4ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentSub4.setAdapter(subject4ArrayAdapter);

        binding.spinnerStudentSub4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_subject4=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsubjectref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    AddFaculty br = dsp.getValue(AddFaculty.class);

                    listSubject4.add(br.getFacultySubject());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Subject5
        final List<String> listSubject5=new ArrayList<String>();
        listSubject5.add("Select Subject");

        ArrayAdapter<String> subject5ArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSubject5);
        subject5ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStudentSub5.setAdapter(subject5ArrayAdapter);

        binding.spinnerStudentSub5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_subject5=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsubjectref.child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    AddFaculty br = dsp.getValue(AddFaculty.class);

                    listSubject5.add(br.getFacultySubject());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadMarks() {

        String sessionalMaxMarks = binding.edtSessionalTotalMarks.getText().toString();
        String sessionalSub1 = binding.edtSessionalSub1.getText().toString();
        String sessionalSub2 = binding.edtSessionalSub2.getText().toString();
        String sessionalSub3 = binding.edtSessionalSub3.getText().toString();
        String sessionalSub4 = binding.edtSessionalSub4.getText().toString();
        String sessionalSub5 = binding.edtSessionalSub5.getText().toString();


        SessionalMarks sessionalMarks = new SessionalMarks(sessionalTitle, studentRollNumber, sessionalMaxMarks, item_subject1, sessionalSub1, item_subject2, sessionalSub2, item_subject3, sessionalSub3, item_subject4, sessionalSub4, item_subject5, sessionalSub5, section);

        reference.child(section).child(studentRollNumber).push().setValue(sessionalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Sessional_Marks.this, "Marks Uploaded Successfully", Toast.LENGTH_SHORT).show();
                binding.edtSessionalSub1.setText("");
                binding.edtSessionalSub2.setText("");
                binding.edtSessionalSub3.setText("");
                binding.edtSessionalSub4.setText("");
                binding.edtSessionalSub5.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sessional_Marks.this, "Please, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
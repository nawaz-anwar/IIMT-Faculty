package com.coetusstudio.iimtufaculty.Activity.Faculty;

import static android.view.View.INVISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Model.AddFaculty;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityFacultyDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyDetails extends AppCompatActivity {

    ActivityFacultyDetailsBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacultyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFacultyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacultyDetails.this.finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty Data");

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AddFaculty addFaculty = snapshot.getValue(AddFaculty.class);

                binding.facultyName.setText(addFaculty.getFacultyName());
                binding.facultyEmail.setText(addFaculty.getFacultyEmail());
                binding.facultyID.setText(addFaculty.getFacultyId());
                binding.facultySubject.setText(addFaculty.getFacultySubject());
                binding.facultySubjectCode.setText(addFaculty.getFacultySubjectCode());
                binding.facultyBranch.setText(addFaculty.getFacultyBranch());
                binding.facultySemester.setText(addFaculty.getFacultySemester());
                binding.facultySection.setText(addFaculty.getFacultySection());
                String password = addFaculty.getFacultyPassword();

                String url = snapshot.child("facultyImage").getValue().toString();
                Glide.with(getApplicationContext()).load(url).error(R.drawable.manimg).into(binding.facultyImage);

                binding.facultyPasswordVisibility.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.facultyPassword.setText(password);
                        binding.facultyPasswordVisibility.setVisibility(INVISIBLE);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyDetails.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

         */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String image, name, email, rollNo, addNo, enrollNo, department, semester, section, grade, password;

                    try {
                        name = dsp.child(auth.getCurrentUser().getUid()).child("facultyName").getValue(String.class);
                        binding.facultyName.setText(name.toString());
                        email = dsp.child(auth.getCurrentUser().getUid()).child("facultyEmail").getValue(String.class).toString();
                        binding.facultyEmail.setText(email);
                        rollNo = dsp.child(auth.getCurrentUser().getUid()).child("facultyId").getValue(String.class).toString();
                        binding.facultyID.setText(rollNo);
                        addNo = dsp.child(auth.getCurrentUser().getUid()).child("facultySubject").getValue(String.class).toString();
                        binding.facultySubject.setText(addNo);
                        enrollNo = dsp.child(auth.getCurrentUser().getUid()).child("facultySubjectCode").getValue(String.class).toString();
                        binding.facultySubjectCode.setText(enrollNo);
                        department = dsp.child(auth.getCurrentUser().getUid()).child("facultyBranch").getValue(String.class).toString();
                        binding.facultyBranch.setText(department);
                        semester = dsp.child(auth.getCurrentUser().getUid()).child("facultySemester").getValue(String.class).toString();
                        binding.facultySemester.setText(semester);
                        section = dsp.child(auth.getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();
                        binding.facultySection.setText(section);
                        password = dsp.child(auth.getCurrentUser().getUid()).child("facultyPassword").getValue(String.class).toString();
                        image = dsp.child(auth.getCurrentUser().getUid()).child("facultyImage").getValue(String.class).toString();
                        Glide.with(getApplicationContext()).load(image).error(R.drawable.manimg).into(binding.facultyImage);

                        binding.facultyPasswordVisibility.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.facultyPassword.setText(password);
                                binding.facultyPasswordVisibility.setVisibility(INVISIBLE);
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                }
                /*
                StudentDetails studentDetails = snapshot.getValue(StudentDetails.class);

                binding.studentName.setText(studentDetails.getStudentName());
                binding.studentEmailId.setText(studentDetails.getStudentEmail());
                binding.studentRollNumber.setText(studentDetails.getStudentRollNumber());
                binding.studentAdmissionNumber.setText(studentDetails.getStudentAdmissionNumber());
                binding.studentEnroolmentNumber.setText(studentDetails.getStudentEnrollmentNumber());
                binding.studentGrade.setText(studentDetails.getStudentGrade());
                binding.studentBranch.setText(studentDetails.getStudentBranch());
                binding.studentSemester.setText(studentDetails.getStudentSemester());
                binding.studentSection.setText(studentDetails.getStudentSection());



                 */

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyDetails.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
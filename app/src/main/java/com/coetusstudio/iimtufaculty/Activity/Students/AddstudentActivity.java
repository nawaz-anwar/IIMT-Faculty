package com.coetusstudio.iimtufaculty.Activity.Students;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.coetusstudio.iimtufaculty.Model.Branch;
import com.coetusstudio.iimtufaculty.Model.Section;
import com.coetusstudio.iimtufaculty.Model.Semester;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.databinding.ActivityAddstudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddstudentActivity extends AppCompatActivity {


    ActivityAddstudentBinding binding;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Uri selectedImage;
    DatabaseReference dbbranchref,dbsemesterref,dbsection,dbDeleteAttendance;
    String itemFaculty,item_batchname,itemAdmissionYear,item_branch,item_division,item_classType;
    HashMap<String,String> hashMapBranch=new HashMap<>();
    HashMap<String,String> hashMapDivision=new HashMap<>();
    HashMap<String,String> hashMapClassType=new HashMap<>();
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddstudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        /*
        FirebaseDatabase.getInstance().getReference().child(String.valueOf(auth.getCurrentUser())).child("facultyName"));

        db=FirebaseDatabase.getInstance().getReference().child("IIMTU").child("Faculty").child(currentUser.getUid()).child("facultyName");
        String facName = db.toString();

         */

        progressDialog = new ProgressDialog(AddstudentActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're adding new Student");

        dbbranchref=FirebaseDatabase.getInstance().getReference("Branch");
        dbsemesterref=FirebaseDatabase.getInstance().getReference("Semester");
        dbsection=FirebaseDatabase.getInstance().getReference("Section");
        dbDeleteAttendance=FirebaseDatabase.getInstance().getReference("DeleteAttendance");

        binding.imageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        //Spinner for Branch
        final List<String> listBranch=new ArrayList<String>();
        listBranch.add("Select Branch");

        ArrayAdapter<String> branchArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listBranch);
        branchArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBranchStudent.setAdapter(branchArrayAdapter);

        binding.spinnerBranchStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_branch=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbbranchref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    Branch br = dsp.getValue(Branch.class);

                    listBranch.add(br.getBranch());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Spinner for Semester
        final List<String> listDivision=new ArrayList<String>();
        listDivision.add("Select Semester");

        ArrayAdapter<String> divisionArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listDivision);
        divisionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSemesterStudent.setAdapter(divisionArrayAdapter);

        binding.spinnerSemesterStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_division=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbsemesterref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    Semester di = dsp.getValue(Semester.class);

                    listDivision.add(di.getSemester());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Spinner for Section
        final List<String> listClassType=new ArrayList<String>();
        listClassType.add("Select Section");

        ArrayAdapter<String> ClassTypeArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listClassType);
        ClassTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSectionStudent.setAdapter(ClassTypeArrayAdapter);

        binding.spinnerSectionStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_classType=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbsection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){

                    Section di = dsp.getValue(Section.class);

                    listClassType.add(di.getSection());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        binding.btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.emailStudent.getEditText().getText().toString(), binding.passwordStudent.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(selectedImage != null) {
                            StorageReference reference = storage.getReference().child("Student Profiles").child(auth.getUid());
                            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();

                                                StudentDetails studentDetails = new StudentDetails(imageUrl,binding.nameStudent.getEditText().getText().toString(), binding.emailStudent.getEditText().getText().toString(),
                                                        binding.admissionNumberStudent.getEditText().getText().toString(), binding.enrollmentNumberStudent.getEditText().getText().toString(), binding.rollNumberStudent.getEditText().getText().toString(),
                                                        item_branch , item_division, item_classType, binding.gradeStudent.getEditText().getText().toString(), binding.passwordStudent.getEditText().getText().toString());


                                                database.getReference().child("Student Data").child(studentDetails.getStudentSection()).child(FirebaseAuth.getInstance().getUid()).setValue(studentDetails);
                                            }
                                        });
                                    }
                                }
                            });
                        }

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if(data.getData() != null) {
                binding.imageStudent.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
}
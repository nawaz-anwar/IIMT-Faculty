package com.coetusstudio.iimtufaculty.Activity.Queries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Activity.Attendance.Select_Attendance;
import com.coetusstudio.iimtufaculty.Adapter.QueriesAdapter;
import com.coetusstudio.iimtufaculty.Model.Queries;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityQueriesBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QueriesActivity extends AppCompatActivity {

    ActivityQueriesBinding binding;
    RecyclerView recviewQueries;
    QueriesAdapter queriesAdapter;
    String studentId;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String facultyName, facultyImage;
    DatabaseReference reference, dbnameref, dbrollref, dbfacultyref, dbfacultyuid;
    String studentName, studentRollNumber, facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Resolve Queries");
        dbrollref = FirebaseDatabase.getInstance().getReference().child("Student Data");
        dbfacultyuid = FirebaseDatabase.getInstance().getReference().child("Faculty Data");
        dbfacultyuid.keepSynced(true);

        FirebaseDatabase.getInstance().getReference().child("Faculty Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    try {
                        facultySection = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();
                        facultyName = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultyName").getValue(String.class).toString();
                        facultyImage = dsp.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facultyImage").getValue(String.class).toString();


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

                //Spinner for StudentRollNumber
                final List<String> listStudentRollNumber=new ArrayList<String>();
                listStudentRollNumber.add("Select Roll Number");

                ArrayAdapter<String> rollNumberArrayAdapter=new ArrayAdapter<String>(QueriesActivity.this,android.R.layout.simple_spinner_item,listStudentRollNumber);
                rollNumberArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerQueriesRollNumber.setAdapter(rollNumberArrayAdapter);

                binding.spinnerQueriesRollNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        studentRollNumber=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                dbrollref.child(facultySection).addListenerForSingleValueEvent(new ValueEventListener() {
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

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QueriesActivity.this);
                recviewQueries=(RecyclerView)findViewById(R.id.rcQueries);
                recviewQueries.setLayoutManager(linearLayoutManager);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                FirebaseRecyclerOptions<Queries> options =
                        new FirebaseRecyclerOptions.Builder<Queries>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Queries").child(facultySection).child(facultyName), Queries.class)
                                .build();
                queriesAdapter=new QueriesAdapter(options);
                recviewQueries.setAdapter(queriesAdapter);
                queriesAdapter.startListening();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QueriesActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.queriesTitle.getEditText().getText().toString().isEmpty()) {
                    binding.queriesTitle.setError("Empty");
                    binding.queriesTitle.requestFocus();
                } else {

                    String queriesTitle = binding.queriesTitle.getEditText().getText().toString();
                    Queries queries = new Queries(studentRollNumber, facultyName, queriesTitle, facultyImage);

                    reference.child(facultySection).child(studentRollNumber).push().setValue(queries).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(QueriesActivity.this, "Queries Sent...", Toast.LENGTH_SHORT).show();

                            binding.queriesTitle.getEditText().setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QueriesActivity.this, "Please, try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        queriesAdapter.stopListening();
    }

}
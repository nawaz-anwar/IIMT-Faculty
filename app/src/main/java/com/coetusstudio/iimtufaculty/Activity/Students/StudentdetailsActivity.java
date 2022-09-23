package com.coetusstudio.iimtufaculty.Activity.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Activity.Home.MainActivity;
import com.coetusstudio.iimtufaculty.Adapter.NotesAdapter;
import com.coetusstudio.iimtufaculty.Adapter.StudentAdapter;
import com.coetusstudio.iimtufaculty.Model.PdfData;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityNotesBinding;
import com.coetusstudio.iimtufaculty.databinding.ActivityStudentdetailsBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentdetailsActivity extends AppCompatActivity {

    ActivityStudentdetailsBinding binding;
    RecyclerView recviewStudent;
    StudentAdapter studentAdapter;
    FirebaseAuth auth;
    String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentdetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Search Roll Number");

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("Faculty Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {

                    try {
                        section = dsp.child(auth.getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();

                        recviewStudent=(RecyclerView)findViewById(R.id.rcstudents);
                        recviewStudent.setLayoutManager(new LinearLayoutManager(StudentdetailsActivity.this));

                        FirebaseRecyclerOptions<StudentDetails> options =
                                new FirebaseRecyclerOptions.Builder<StudentDetails>()
                                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Student Data").child(section), StudentDetails.class)
                                        .build();

                        studentAdapter=new StudentAdapter(options);
                        recviewStudent.setAdapter(studentAdapter);
                        studentAdapter.startListening();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentdetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        studentAdapter.stopListening();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {

                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<StudentDetails> options =
                new FirebaseRecyclerOptions.Builder<StudentDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Student Data").child(section).orderByChild("studentRollNumber").startAt(s).endAt(s+"\uf8ff"), StudentDetails.class)
                        .build();

        studentAdapter=new StudentAdapter(options);
        studentAdapter.startListening();
        recviewStudent.setAdapter(studentAdapter);

    }
}
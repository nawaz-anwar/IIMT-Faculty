package com.coetusstudio.iimtufaculty.Activity.Lecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.coetusstudio.iimtufaculty.Adapter.LectureAdapter;
import com.coetusstudio.iimtufaculty.Model.Lecture;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityRecentlectureBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RecentlectureActivity extends AppCompatActivity {

    ActivityRecentlectureBinding binding;
    RecyclerView recviewLecture;
    LectureAdapter lectureAdapter;
    String facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentlectureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Search Lecture");

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecentlectureActivity.this);
        recviewLecture=(RecyclerView)findViewById(R.id.rcLecture);
        recviewLecture.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        FirebaseRecyclerOptions<Lecture> options =
                new FirebaseRecyclerOptions.Builder<Lecture>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Lecture").child(facultySection), Lecture.class)
                        .build();

        lectureAdapter=new LectureAdapter(options);
        recviewLecture.setAdapter(lectureAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lectureAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lectureAdapter.stopListening();
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
        FirebaseRecyclerOptions<Lecture> options =
                new FirebaseRecyclerOptions.Builder<Lecture>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Lecture").child(facultySection).orderByChild("lectureName").startAt(s).endAt(s+"\uf8ff"), Lecture.class)
                        .build();

        lectureAdapter=new LectureAdapter(options);
        lectureAdapter.startListening();
        recviewLecture.setAdapter(lectureAdapter);

    }
}
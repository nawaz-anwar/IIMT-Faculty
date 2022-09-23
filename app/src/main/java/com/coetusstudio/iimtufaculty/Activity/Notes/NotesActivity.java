package com.coetusstudio.iimtufaculty.Activity.Notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.coetusstudio.iimtufaculty.Activity.Home.MainActivity;
import com.coetusstudio.iimtufaculty.Adapter.NotesAdapter;
import com.coetusstudio.iimtufaculty.Model.PdfData;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityNotesBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class NotesActivity extends AppCompatActivity {

    ActivityNotesBinding binding;
    FloatingActionButton fb;
    RecyclerView recview;
    NotesAdapter adapter;
    String facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Search PDF");

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");

        fb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), UploadpdfActivity.class);
                intent1.putExtra("section", facultySection);
                startActivity(intent1);
            }
        });



        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<PdfData> options =
                new FirebaseRecyclerOptions.Builder<PdfData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notes").child(facultySection), PdfData.class)
                        .build();

        adapter=new NotesAdapter(options);
        recview.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
        FirebaseRecyclerOptions<PdfData> options =
                new FirebaseRecyclerOptions.Builder<PdfData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notes").child(facultySection).orderByChild("filename").startAt(s).endAt(s+"\uf8ff"), PdfData.class)
                        .build();

        adapter=new NotesAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
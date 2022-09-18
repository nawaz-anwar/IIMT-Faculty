package com.coetusstudio.iimtufaculty.Activity.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.coetusstudio.iimtufaculty.Adapter.NoticeAdapter;
import com.coetusstudio.iimtufaculty.Adapter.NoticeHodAdapter;
import com.coetusstudio.iimtufaculty.Model.NoticeData;
import com.coetusstudio.iimtufaculty.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RecentHodNotification extends AppCompatActivity {

    RecyclerView recviewNotice;
    NoticeHodAdapter noticeAdapter;
    String facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_hod_notification);

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecentHodNotification.this);
        recviewNotice=(RecyclerView)findViewById(R.id.rcHodNotice);
        recviewNotice.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        FirebaseRecyclerOptions<NoticeData> options =
                new FirebaseRecyclerOptions.Builder<NoticeData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notice").child(facultySection).child("Only Faculty"), NoticeData.class)
                        .build();

        noticeAdapter=new NoticeHodAdapter(options);
        recviewNotice.setAdapter(noticeAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noticeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noticeAdapter.stopListening();
    }

}
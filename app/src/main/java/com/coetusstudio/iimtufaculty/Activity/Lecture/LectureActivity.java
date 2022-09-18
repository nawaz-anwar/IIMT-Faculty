package com.coetusstudio.iimtufaculty.Activity.Lecture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Model.Lecture;
import com.coetusstudio.iimtufaculty.databinding.ActivityLectureBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LectureActivity extends AppCompatActivity {


    ActivityLectureBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String facultyImage, facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLectureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");
        facultyImage = intent.getStringExtra("image");

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Lecture");

        binding.btnRecentLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LectureActivity.this, RecentlectureActivity.class);
                intent.putExtra("section", facultySection);
                startActivity(intent);
            }
        });

        binding.btnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.lectureLink.getEditText().getText().toString().isEmpty()) {
                    binding.lectureLink.setError("Empty");
                    binding.lectureLink.requestFocus();
                } else {
                    sendlink();
                }
            }
        });

        binding.btnMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse("https://meet.google.com/");
                Intent webMeet = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webMeet);
            }
        });
        binding.btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage1 = Uri.parse("https://zoom.us/wc/meetingId/join?prefer=1");
                Intent webZoom = new Intent(Intent.ACTION_VIEW, webpage1);
                startActivity(webZoom);
            }
        });
        binding.btnMsTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage3 = Uri.parse("https://teams.live.com/");
                Intent webMsTeam = new Intent(Intent.ACTION_VIEW, webpage3);
                startActivity(webMsTeam);
            }
        });
        binding.btnWebex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage3 = Uri.parse("https://www.webex.com/");
                Intent webWebex = new Intent(Intent.ACTION_VIEW, webpage3);
                startActivity(webWebex);
            }
        });

    }

    private void sendlink() {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        String lectureName = binding.lectureName.getEditText().getText().toString();
        String lectureLink = binding.lectureLink.getEditText().getText().toString();
        String lectureTiming = binding.lectureTiming.getEditText().getText().toString();
        Lecture lecture = new Lecture(lectureName,lectureTiming,lectureLink,date,time, facultyImage, facultySection);

        reference.child(facultySection).push().setValue(lecture).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LectureActivity.this, "Link sent to the student", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LectureActivity.this, "Please, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
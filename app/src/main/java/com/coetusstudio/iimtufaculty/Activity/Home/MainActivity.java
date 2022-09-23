package com.coetusstudio.iimtufaculty.Activity.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Activity.Attendance.AttendanceActivity;
import com.coetusstudio.iimtufaculty.Activity.Faculty.FacultyDetails;
import com.coetusstudio.iimtufaculty.Activity.Lecture.LectureActivity;
import com.coetusstudio.iimtufaculty.Activity.Students.AddstudentActivity;
import com.coetusstudio.iimtufaculty.Activity.Form.CreateformActivity;
import com.coetusstudio.iimtufaculty.Activity.Notes.NotesActivity;
import com.coetusstudio.iimtufaculty.Activity.Queries.QueriesActivity;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.Activity.Notification.SendnotificationActivity;
import com.coetusstudio.iimtufaculty.Activity.Marks.Sessional_Marks;
import com.coetusstudio.iimtufaculty.Activity.Students.StudentdetailsActivity;
import com.coetusstudio.iimtufaculty.VerifyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.coetusstudio.iimtufaculty.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView sendNotification, addNewStudent, studentDetails, attendance, lecture, queries, notes, uploadMarks;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser currentUser ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton emailFeedback;
    Toolbar toolbar;
    String facultySection, facultySubject, facultyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        sendNotification = findViewById(R.id.sendNotification);
        addNewStudent = findViewById(R.id.addNewStudent);
        attendance = findViewById(R.id.attendance);
        lecture = findViewById(R.id.lecture);
        studentDetails = findViewById(R.id.studentDetails);
        notes = findViewById(R.id.notes);
        uploadMarks = findViewById(R.id.uploadMarks);
        queries = findViewById(R.id.queries);

        sendNotification.setOnClickListener(this);
        addNewStudent.setOnClickListener(this);
        attendance.setOnClickListener(this);
        lecture.setOnClickListener(this);
        studentDetails.setOnClickListener(this);
        notes.setOnClickListener(this);
        uploadMarks.setOnClickListener(this);
        queries.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        emailFeedback = findViewById(R.id.emailFeedback);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle("IIMTU Faculty");

        updateNavHeader();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.profile) {
                    Intent intent = new Intent(MainActivity.this, FacultyDetails.class);
                    startActivity(intent);
                } else if (id == R.id.profileSendNotification) {
                    Intent intent = new Intent(MainActivity.this, SendnotificationActivity.class);
                    startActivity(intent);
                }else if (id == R.id.profileCreateForm) {
                        Intent intent = new Intent(MainActivity.this, CreateformActivity.class);
                        startActivity(intent);

                } else if (id == R.id.privacyPolicy) {
                    Uri webpage = Uri.parse("https://www.iimtu.com/privacy-policy");
                    Intent webMeet = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webMeet);
                } else if (id == R.id.aboutUs) {
                    Uri webpage = Uri.parse("https://www.iimtu.com/about-iimt/our-founder");
                    Intent webMeet = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webMeet);
                }else if (id == R.id.resetPassword) {
                    startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));

                } else if (id == R.id.logout) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        emailFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"feedback.coetusstudio@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IIMTU Faculty Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Type your query here...");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                startActivity(emailIntent);
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendNotification:
//                Intent intent = new Intent(MainActivity.this, SendnotificationActivity.class);
                startActivity(new Intent(MainActivity.this, SendnotificationActivity.class));
                break;
            case R.id.addNewStudent:
                Intent intent1 = new Intent(MainActivity.this, AddstudentActivity.class);
                startActivity(intent1);
                break;
            case R.id.attendance:
                Intent intent2 = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(intent2);
                break;
            case R.id.lecture:
                Intent intent3 = new Intent(MainActivity.this, LectureActivity.class);
                intent3.putExtra("section", facultySection);
                intent3.putExtra("image", facultyImage);
                startActivity(intent3);
                break;
            case R.id.studentDetails:
                Intent intent4 = new Intent(MainActivity.this, StudentdetailsActivity.class);
                startActivity(intent4);
                break;
            case R.id.notes:
                Intent intent5 = new Intent(MainActivity.this, NotesActivity.class);
                intent5.putExtra("section", facultySection);
                startActivity(intent5);
                break;
            case R.id.uploadMarks:
                Intent intent6 = new Intent(MainActivity.this, VerifyActivity.class);
                intent6.putExtra("section", facultySection);
                startActivity(intent6);
                break;
            case R.id.queries:
                Intent intent7 = new Intent(MainActivity.this, QueriesActivity.class);
                startActivity(intent7);
                break;
        }
    }
    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.facultyNameProfile);
        TextView navUserMail = headerView.findViewById(R.id.facultyEmailProfile);
        ImageView navUserPhot = headerView.findViewById(R.id.facultyImageProfile);

        FirebaseDatabase.getInstance().getReference().child("Faculty Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String image, name, email = null;

                    try {
                        name = dsp.child(auth.getCurrentUser().getUid()).child("facultyName").getValue(String.class).toString();
                        navUsername.setText(name);
                        email = dsp.child(auth.getCurrentUser().getUid()).child("facultyEmail").getValue(String.class).toString();
                        navUserMail.setText(email);
                        facultySection = dsp.child(auth.getCurrentUser().getUid()).child("facultySection").getValue(String.class).toString();
                        facultySubject = dsp.child(auth.getCurrentUser().getUid()).child("facultySubject").getValue(String.class).toString();
                        facultyImage = dsp.child(auth.getCurrentUser().getUid()).child("facultyImage").getValue(String.class).toString();

                        image = dsp.child(auth.getCurrentUser().getUid()).child("facultyImage").getValue(String.class).toString();
                        Glide.with(getApplicationContext()).load(image).error(R.drawable.manimg).into(navUserPhot);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
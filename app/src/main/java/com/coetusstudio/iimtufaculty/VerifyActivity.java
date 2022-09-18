package com.coetusstudio.iimtufaculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Activity.Marks.Sessional_Marks;
import com.coetusstudio.iimtufaculty.databinding.ActivityVerifyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyActivity extends AppCompatActivity {

    ActivityVerifyBinding binding;
    DatabaseReference databaseReference;
    String username, password, edtUsername, edtPassword, facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Credential");

        //edtUsername = binding.verificationUsername.getEditText().getText().toString();
        //edtPassword = binding.verificationPassword.getEditText().getText().toString();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                username = snapshot.child("username").getValue().toString();
                password = snapshot.child("password").getValue().toString();

                binding.btnVerifyPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.verificationUsername.getEditText().getText().toString().equals(username) && binding.verificationPassword.getEditText().getText().toString().equals(password)){
                            Toast.makeText(getApplicationContext(),"Account Verified", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Sessional_Marks.class);
                            intent.putExtra("section", facultySection);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(),"Incorrect Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
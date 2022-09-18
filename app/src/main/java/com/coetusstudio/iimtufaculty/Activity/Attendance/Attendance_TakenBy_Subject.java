package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coetusstudio.iimtufaculty.Model.Attendance;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Attendance_TakenBy_Subject extends AppCompatActivity {

    String subid;
    String timestamp;
    String attenvalue,AttendanceType, facultySection;
    boolean internetsts;

    int count1=0,count2=0,present=0;

    ArrayList<String> selectedItems;
    ArrayList<String> nonselectedItems;

    private ArrayAdapter adapter;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Usernames = new ArrayList<>();

    DatabaseReference AttendenRecordSheet;
    DatabaseReference stdSubdetail;

    Button submitbtn;
    ProgressDialog mDialog;

    private static int PICK_IMAGE=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_taken_by_subject);

        //receiving data from previous activity
        Intent intent=getIntent();
        subid=intent.getStringExtra("subid");
        timestamp=intent.getStringExtra("timestamp");
        attenvalue="1";
        AttendanceType = intent.getStringExtra("AttendanceType");
        facultySection = intent.getStringExtra("facultySection");


        submitbtn=findViewById(R.id.submitatten_btn);



        selectedItems = new ArrayList<String>();

        AttendenRecordSheet= FirebaseDatabase.getInstance().getReference().child("AttendenRecordSheet");
        stdSubdetail=FirebaseDatabase.getInstance().getReference().child("Student Data").child(facultySection);
        stdSubdetail.keepSynced(true);

        stdSubdetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){
                    StudentDetails studentDetails = dsp.getValue(StudentDetails.class);
                    Userlist.add(dsp.child("studentRollNumber").getValue().toString());
                    Collections.sort(Userlist);
                    //Usernames.add(dsp.child("studentName").getValue().toString());
                    count1=count1+1;

                }
                OnStart(Userlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                internetsts=isNetworkAvailable();

                if (internetsts!=true){
                    AlertDialog.Builder builder= new AlertDialog.Builder(Attendance_TakenBy_Subject.this);
                    builder.setMessage("Internet is not available");
                    builder.setTitle("Alert !");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                else {
                    assurance();
                }

            }
        });

    }

    public void OnStart(ArrayList<String> userlist){
        nonselectedItems=userlist;
        ListView ch1=findViewById(R.id.checkable_list);

        ch1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> sidarrayadapter=new ArrayAdapter<String>(this,R.layout.checkable_attendance,R.id.checkboxt,userlist);
        ch1.setAdapter(sidarrayadapter);

        ch1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem);
                else
                    selectedItems.add(selectedItem);

            }

        });

        ch1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = ((TextView) view).getText().toString();
                return false;
            }
        });
    }




    public void assurance(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation!");
        builder.setMessage("Do you want to Submit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startuploadattendence();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();  // Show the Alert Dialog box

    }

    public void startuploadattendence(){

        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Submitting Attendance");
        mDialog.setMessage("Please wait..");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();


        if (AttendanceType.equals("Select Present Student Only")){

            for (String item :selectedItems){
                nonselectedItems.remove(item);
                Attendance attendance=new Attendance((attenvalue+"/"+attenvalue));
                AttendenRecordSheet.child(facultySection).child(subid).child(timestamp).child(item).setValue(attendance);
                count2=count2+1;
                present=present+1;
            }

            for(String item :nonselectedItems){
                Attendance attendance=new Attendance(("0/"+attenvalue));
                AttendenRecordSheet.child(facultySection).child(subid).child(timestamp).child(item).setValue(attendance);
                count2=count2+1;
            }
        }


        else {

            for (String item : selectedItems) {
                nonselectedItems.remove(item);
                Attendance attendance = new Attendance(("0/" + attenvalue));
                AttendenRecordSheet.child(facultySection).child(subid).child(timestamp).child(item).setValue(attendance);
                count2 = count2 + 1;
            }

            for (String item : nonselectedItems) {
                Attendance attendance = new Attendance((attenvalue + "/" + attenvalue));
                AttendenRecordSheet.child(facultySection).child(subid).child(timestamp).child(item).setValue(attendance);
                count2 = count2 + 1;
                present = present + 1;

            }

        }


        mDialog.dismiss();

        if (count1==count2)
        {
            count1=count1-present;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submitted Successfully!");
            builder.setMessage("Present="+present+"  Absent="+count1+" Total Student="+count2);
            builder.setCancelable(false);

            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    dialog.cancel();
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            count1=0;
            count2=0;

            //under if statement
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submission Failed");
            builder.setMessage("Something went wrong !");
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Do you want to Exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();  // Show the Alert Dialog box
        super.onBackPressed();
    }
}
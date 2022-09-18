package com.coetusstudio.iimtufaculty.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coetusstudio.iimtufaculty.Model.Attendance;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.databinding.ActivityViewCollectiveAttendanceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Collective_Attendance extends AppCompatActivity {

    ActivityViewCollectiveAttendanceBinding binding;
    String subid,SubName;
    int tpresent = 0;
    int tattencount = 0;

    DatabaseReference dbAttendanceRecordRef;
    ProgressDialog mDialog;

    //variable deceleration
    ArrayList<Attendance> data = new ArrayList<>();
    RecyclerView recyclerView;
    RAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCollectiveAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        SubName=intent.getStringExtra("facultySubject");
        subid=intent.getStringExtra("facultySection");

        binding.textViewSubject.setText(SubName);

        //implementation of recycler view

        recyclerView = findViewById(R.id.recycler_collective_attendance_view);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RAdapter(data, getApplicationContext());

        dbAttendanceRecordRef= FirebaseDatabase.getInstance().getReference().child("AttendenRecordSheet").child(subid).child(SubName);
        dbAttendanceRecordRef.keepSynced(true);

        recyclerView.setAdapter(adapter);


        //ending of recycler view


        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Please Wait");
        mDialog.setMessage("Data is loading...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();




        //start quaring with student step 1 getting name and sid from here
        FirebaseDatabase.getInstance().getReference().child("Student Data").child(subid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id, name;
                    StudentDetails studentDetails = dsp.getValue(StudentDetails.class);
                    id=studentDetails.getStudentRollNumber();
                    name=studentDetails.getStudentName();
                    gotoattendencedatabase(id, name);
                    adapter.notifyDataSetChanged();


                }
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void gotoattendencedatabase(final String id, final String name) {

        dbAttendanceRecordRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String aval;
                    aval = dsp.child(id).child("atvalue").getValue(String.class);
                    try {
                        tpresent += Integer.valueOf(aval.substring(0, 1));
                        tattencount += Integer.valueOf(aval.substring(2, 3));
                    }
                    catch (Exception e){

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Alert !");
                        builder.setMessage("Something went wrong please contact Admin");
                        builder.setCancelable(false);
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }


                }
                int abs = tattencount - tpresent;
                float percent;
                percent = (((float) tpresent) / ((float) tattencount)) * 100;
                percent=Math.round(percent*1000)/1000;

                String attendence=(String.valueOf(tpresent)+"/"+String.valueOf(tattencount)) ;
                String absent=String.valueOf(abs);
                String percentage=String.valueOf(percent);

                Attendance details=new Attendance(id,name,attendence,absent,percentage);
                data.add(details);
                adapter.notifyDataSetChanged();
                tpresent = 0;
                tattencount = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class RAdapter extends RecyclerView.Adapter<RAdapter.RViewHolder>{
        ArrayList<Attendance> details;
        Context context;
        @NonNull
        @Override
        public RViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singlerowcollectiveattendance, viewGroup, false);
            RViewHolder holder = new RViewHolder(v);
            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RViewHolder v, int i) {
            View view = v.itemView;
            String percent = details.get(i).getPercent();
            if(Float.valueOf(percent)<=75){
                v.percenttv.setTextColor(Color.RED);
            }
            else if(Float.valueOf(percent)>75 && Float.valueOf(percent)<85 ){
                v.percenttv.setTextColor(Color.BLUE);
            }
            else if(Float.valueOf(percent)>=85){
                v.percenttv.setTextColor(Color.MAGENTA);
            }
            v.enrollmenttv.setText("Roll Number: "+details.get(i).getEnrollment());
            v.nametv.setText("Name: "+details.get(i).getName());
            v.absenttv.setText(details.get(i).getAbsent());
            v.percenttv.setText(percent+" %");
            v.attendancetv.setText(details.get(i).getAttendance());
        }

        @Override
        public int getItemCount() {
            return details.size();
        }

        RAdapter(ArrayList<Attendance> details, Context context){
            this.details = details;
            this.context  = context;
        }
        class RViewHolder extends RecyclerView.ViewHolder{
            TextView enrollmenttv, nametv, attendancetv, absenttv, percenttv;

            public RViewHolder(@NonNull View v) {
                super(v);
                enrollmenttv = v.findViewById(R.id.enno_input_tv);
                nametv = v.findViewById(R.id.attendanceStudentName);
                attendancetv = v.findViewById(R.id.present_input_tv);
                absenttv = v.findViewById(R.id.absent_input_tv);
                percenttv = v.findViewById(R.id.percentage_input_tv);
            }
        }
    }
}
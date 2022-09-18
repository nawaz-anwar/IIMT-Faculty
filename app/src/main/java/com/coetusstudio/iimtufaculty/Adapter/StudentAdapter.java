package com.coetusstudio.iimtufaculty.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Model.PdfData;
import com.coetusstudio.iimtufaculty.Model.StudentDetails;
import com.coetusstudio.iimtufaculty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends FirebaseRecyclerAdapter<StudentDetails,StudentAdapter.myviewholder>{
    String studentSection;

    public StudentAdapter(@NonNull FirebaseRecyclerOptions<StudentDetails> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StudentAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final StudentDetails StudentDetails)
    {


        holder.name.setText(StudentDetails.getStudentName());
        holder.email.setText(StudentDetails.getStudentEmail());
        holder.admission.setText(StudentDetails.getStudentAdmissionNumber());
        holder.enrollment.setText(StudentDetails.getStudentEnrollmentNumber());
        holder.roll.setText(StudentDetails.getStudentRollNumber());
        holder.grade.setText(StudentDetails.getStudentGrade());
        holder.branch.setText(StudentDetails.getStudentBranch());
        holder.semester.setText(StudentDetails.getStudentSemester());
        holder.studentSection.setText(StudentDetails.getStudentSection());
        Glide.with(holder.img.getContext()).load(StudentDetails.getStudentImage())
                .placeholder(R.drawable.manimg)
                .circleCrop()
                .error(R.drawable.manimg)
                .into(holder.img);
        studentSection = StudentDetails.getStudentSection();

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogstudent))
                        .setExpanded(true,1600)
                        .create();


                View myview=dialogPlus.getHolderView();
                final EditText name=myview.findViewById(R.id.updNameStudent);
                final EditText email=myview.findViewById(R.id.updEmailStudent);
                final EditText rollNumber=myview.findViewById(R.id.updRollNumberStudent);
                final EditText admissionNumber=myview.findViewById(R.id.updAdmissionNumberStudent);
                final EditText enrollmentNumber=myview.findViewById(R.id.updEnrollmentNumberStudent);
                final EditText branch=myview.findViewById(R.id.updBranchStudent);
                final EditText grade=myview.findViewById(R.id.updGradeStudent);
                Button submit=myview.findViewById(R.id.updBtnStudent);


                name.setText(StudentDetails.getStudentName());
                email.setText(StudentDetails.getStudentEmail());
                rollNumber.setText(StudentDetails.getStudentRollNumber());
                admissionNumber.setText(StudentDetails.getStudentAdmissionNumber());
                enrollmentNumber.setText(StudentDetails.getStudentEnrollmentNumber());
                branch.setText(StudentDetails.getStudentBranch());
                grade.setText(StudentDetails.getStudentGrade());
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("studentName",name.getText().toString());
                        map.put("studentEmail",email.getText().toString());
                        map.put("studentRollNumber",rollNumber.getText().toString());
                        map.put("studentAdmissionNumber",admissionNumber.getText().toString());
                        map.put("studentEnrollmentNumber",enrollmentNumber.getText().toString());
                        map.put("studentBranch",branch.getText().toString());
                        map.put("studentGrade",grade.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("Student Data").child(studentSection).child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(holder.img.getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.img.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure want to delete Student Data...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Student Data").child(studentSection).child(getRef(position).getKey())
                                .removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

    } // End of OnBindViewMethod

    @NonNull
    @Override
    public StudentAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowstudent,parent,false);
        return new StudentAdapter.myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        ImageView edit,delete;
        TextView name,branch,email,admission,enrollment,roll,semester, studentSection,grade;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.studentImage);
            name=itemView.findViewById(R.id.studentName);
            email=itemView.findViewById(R.id.studentEmail);
            admission=itemView.findViewById(R.id.studentAdmissionNumber);
            enrollment=itemView.findViewById(R.id.studentEnrollmentNumber);
            roll=itemView.findViewById(R.id.studentRollNumber);
            branch=itemView.findViewById(R.id.studentBranch);
            semester=itemView.findViewById(R.id.studentSemester);
            studentSection=itemView.findViewById(R.id.studentSection);
            grade=itemView.findViewById(R.id.studentGrade);

            edit=(ImageView)itemView.findViewById(R.id.studentEdit);
            delete=(ImageView)itemView.findViewById(R.id.studentDelete);
        }
    }
}
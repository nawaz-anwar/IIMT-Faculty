package com.coetusstudio.iimtufaculty.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coetusstudio.iimtufaculty.Model.Lecture;
import com.coetusstudio.iimtufaculty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class LectureAdapter extends FirebaseRecyclerAdapter<Lecture,LectureAdapter.myviewholder> {
    String section;
    public LectureAdapter(@NonNull FirebaseRecyclerOptions<Lecture> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final LectureAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Lecture Lecture)
    {


        holder.lectureName.setText(Lecture.getLectureName());
        holder.lectureTiming.setText(Lecture.getLectureTiming());
        holder.lectureLink.setText(Lecture.getLectureLink());
        holder.lectureDate.setText(Lecture.getLectureDate());
        holder.lectureTime.setText(Lecture.getLectureTime());
        String uriLink = Lecture.getLectureLink();
        section = Lecture.getFacultySection();

        holder.lectureDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.lectureName.getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure want to delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Lecture").child(section).child(getRef(position).getKey())
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

        holder.btnLectureJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, uriLink);
                intent.setType("text/plain");
                holder.lectureLink.getContext().startActivity(intent);
            }
        });

        holder.lectureLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, uriLink);
                intent.setType("text/plain");
                holder.lectureLink.getContext().startActivity(intent);
            }
        });


    } // End of OnBindViewMethod

    @NonNull
    @Override
    public LectureAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlelecture,parent,false);
        return new LectureAdapter.myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView lectureName, lectureTiming, lectureLink, lectureDate, lectureTime;
        Button btnLectureJoin;
        ImageView lectureDelete;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            lectureName=itemView.findViewById(R.id.lectureName);
            lectureTiming=itemView.findViewById(R.id.lectureTiming);
            lectureLink=itemView.findViewById(R.id.lectureLink);
            lectureDate=itemView.findViewById(R.id.lectureDate);
            lectureTime=itemView.findViewById(R.id.lectureTime);
            lectureDelete=itemView.findViewById(R.id.lectureDelete);
            btnLectureJoin=itemView.findViewById(R.id.btnLectureJoin);

        }
    }
}

package com.coetusstudio.iimtufaculty.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coetusstudio.iimtufaculty.Model.PdfData;
import com.coetusstudio.iimtufaculty.R;
import com.coetusstudio.iimtufaculty.ViewpdfActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class NotesAdapter extends FirebaseRecyclerAdapter<PdfData,NotesAdapter.myviewholder> {

    String section;

    public NotesAdapter(@NonNull FirebaseRecyclerOptions<PdfData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NotesAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final PdfData PdfData) {

        holder.header.setText(PdfData.getFilename());
        section = PdfData.getFacultySection();

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.img1.getContext(), ViewpdfActivity.class);
                intent.putExtra("filename",PdfData.getFilename());
                intent.putExtra("fileurl",PdfData.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.header.getContext(), ViewpdfActivity.class);
                intent.putExtra("filename",PdfData.getFilename());
                intent.putExtra("fileurl",PdfData.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.header.getContext().startActivity(intent);
            }
        });

        holder.notesDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.header.getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure want to delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Notes").child(section).child(getRef(position).getKey())
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

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowpdf,parent,false);
        return  new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1, notesDelete;
        TextView header;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);
            notesDelete=itemView.findViewById(R.id.notesDelete);
        }
    }
}

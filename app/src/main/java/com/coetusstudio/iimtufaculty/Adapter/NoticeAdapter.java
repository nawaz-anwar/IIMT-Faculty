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

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Model.NoticeData;
import com.coetusstudio.iimtufaculty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeAdapter extends FirebaseRecyclerAdapter<NoticeData,NoticeAdapter.myviewholder> {

    public NoticeAdapter(@NonNull FirebaseRecyclerOptions<NoticeData> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NoticeAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final NoticeData NoticeData)
    {


        holder.notificationTitle.setText(NoticeData.getTitle());
        holder.notificationDate.setText(NoticeData.getDate());
        holder.notificationTime.setText(NoticeData.getTime());
        Glide.with(holder.notificationImage.getContext()).load(NoticeData.getImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.notificationImage);

        holder.notificationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.notificationImage.getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure want to delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Notice").child("All Faculty And Students").child(getRef(position).getKey())
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

        holder.notificationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, NoticeData.getTitle());
                intent.setType("text/plain");
                holder.notificationTitle.getContext().startActivity(intent);
            }
        });


    } // End of OnBindViewMethod

    @NonNull
    @Override
    public NoticeAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlenotice,parent,false);
        return new NoticeAdapter.myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView notificationTitle, notificationDate, notificationTime;
        ImageView notificationImage, notificationDelete;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            notificationTitle=itemView.findViewById(R.id.notificationTitle);
            notificationImage=itemView.findViewById(R.id.notificationImage);
            notificationDate=itemView.findViewById(R.id.notificationDate);
            notificationTime=itemView.findViewById(R.id.notificationTime);
            notificationDelete=itemView.findViewById(R.id.notificationDelete);

        }
    }
}

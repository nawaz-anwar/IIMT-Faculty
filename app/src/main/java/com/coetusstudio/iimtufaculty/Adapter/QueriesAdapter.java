package com.coetusstudio.iimtufaculty.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coetusstudio.iimtufaculty.Model.Queries;
import com.coetusstudio.iimtufaculty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class QueriesAdapter extends FirebaseRecyclerAdapter<Queries,QueriesAdapter.myviewholder>{

    public QueriesAdapter(@NonNull FirebaseRecyclerOptions<Queries> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final QueriesAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Queries Queries)
    {


        holder.queriesName.setText(Queries.getQueriesName());
        holder.queriesRollNumber.setText(Queries.getQueriesRollNumber());
        holder.queriesTitle.setText(Queries.getQueriesTitle());
        Glide.with(holder.img.getContext()).load(Queries.getStudentImage())
                .placeholder(R.drawable.manimg)
                .circleCrop()
                .error(R.drawable.manimg)
                .into(holder.img);


    } // End of OnBindViewMethod

    @NonNull
    @Override
    public QueriesAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowqueries,parent,false);
        return new QueriesAdapter.myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView queriesName, queriesRollNumber, queriesTitle;
        CircleImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            queriesName=itemView.findViewById(R.id.queriesName);
            queriesRollNumber=itemView.findViewById(R.id.queriesRollNumber);
            queriesTitle=itemView.findViewById(R.id.queriesTitleAdapter);
            img=itemView.findViewById(R.id.queriesStudentImage);
            //delete=(ImageView)itemView.findViewById(R.id.queriesDelete);

        }
    }
}

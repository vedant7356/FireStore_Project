package com.example.firestore_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class mynewadapter extends RecyclerView.Adapter<mynewadapter.MyNewViewHolder>{

    Context context;

    ArrayList<evemodel> list;

    public mynewadapter(Context context, ArrayList<evemodel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerownew,parent,false);
        return new MyNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNewViewHolder holder, int position) {
        holder.joining_url.setMovementMethod(LinkMovementMethod.getInstance());
       evemodel mo=list.get(position);
        holder.title.setText(mo.getTitle());
        holder.details.setText(mo.getDetails_Of_Events());
        holder.joining_url.setText(mo.getJoining_url());
        holder.date.setText(mo.getDate());;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mo.getJoining_url()));
                context.startActivity(browserIntent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyNewViewHolder extends RecyclerView.ViewHolder{

        TextView title,details,joining_url,date;
        public MyNewViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title_event);
            details=(TextView)itemView.findViewById(R.id.details_event);
            joining_url=(TextView)itemView.findViewById(R.id.joining_url);
            date=(TextView)itemView.findViewById(R.id.date);
            joining_url.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
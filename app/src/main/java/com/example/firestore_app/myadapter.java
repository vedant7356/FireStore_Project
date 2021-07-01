package com.example.firestore_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder>{

    Context context;

    ArrayList<emplist> list;

    public myadapter(Context context, ArrayList<emplist> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        emplist mo=list.get(position);
        holder.name.setText(mo.getDisplayName());
        holder.email.setText(mo.getEmail());
        holder.usertypetxt.setText(mo.getUserType());
        Glide.with(holder.img.getContext()).load(mo.getImageUrl()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,email,usertypetxt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            usertypetxt=(TextView)itemView.findViewById(R.id.usertype);
        }
    }

}

package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;

import java.util.ArrayList;

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ViewHolder> {
    Context context;
    ArrayList<String> actors;
    public ActorsAdapter(Context context, ArrayList<String> actors){
        this.context = context;
        this.actors = actors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.actor_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(actors.get(position))
                .into(holder.actor_image);
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView actor_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actor_image = itemView.findViewById(R.id.actor_image);
        }
    }
}
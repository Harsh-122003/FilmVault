package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesapp.Domains.SliderItems;
import com.example.moviesapp.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SliderItems> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdapter(Context context, ArrayList<SliderItems> sliderItems, ViewPager2 viewPager2){
        this.context = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if(position == sliderItems.size()-2)
        {
            viewPager2.post(holder.runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.sliderImage);
        }

        void setImage(SliderItems sliderItems){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(),new RoundedCorners(60));

            Glide.with(context)
                    .load(sliderItems.getImage())
                    .apply(requestOptions)
                    .into(imageView);
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sliderItems.addAll(sliderItems);
                notifyDataSetChanged();
            }
        };
    }
}
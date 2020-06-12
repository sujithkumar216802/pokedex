package com.sujithkumar.pokedex.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.favouriterecyclerclicklistner;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.viewholder> {


    ArrayList<String> name, searchlist;
    ArrayList<Bitmap> images;
    favouriterecyclerclicklistner clicker;


    public FavouriteAdapter(ArrayList<String> n, ArrayList<Bitmap> x) {
        images = new ArrayList<>(x);
        name = new ArrayList<>(n);
        searchlist = new ArrayList<>(name);
    }

    public void setclicker(favouriterecyclerclicklistner clicker) {
        this.clicker = clicker;
    }

    @NonNull
    @Override
    public FavouriteAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewtemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.viewholder holder, int position) {

        holder.name.setText(name.get(position));
        holder.image.setImageBitmap(images.get(position));
        if(images.get(position)==null)
            holder.imagenotavailable.setVisibility(View.VISIBLE);
        ViewCompat.setTransitionName(holder.itemView, name.get(position));

        if (clicker != null)
            holder.itemView.setOnClickListener(v -> {
                if (clicker != null)
                    if (position != RecyclerView.NO_POSITION)
                        clicker.favouriteclick(position, holder.itemView);
            });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public void change(ArrayList<String> n, ArrayList<Bitmap> x) {
        images = new ArrayList<>(x);
        name = new ArrayList<>(n);
        searchlist = new ArrayList<>(name);
        notifyDataSetChanged();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,imagenotavailable;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            imagenotavailable = itemView.findViewById(R.id.notavailable);
        }
    }


}

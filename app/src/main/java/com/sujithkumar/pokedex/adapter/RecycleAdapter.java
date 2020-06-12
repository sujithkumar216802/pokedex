package com.sujithkumar.pokedex.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.recyclerclicklistner;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.viewholder> {

    ArrayList<NameandUrl> name;
    ArrayList<String> sprite;
    recyclerclicklistner clicker;


    public RecycleAdapter(ArrayList<NameandUrl> n, ArrayList<String> x) {
        sprite = new ArrayList<>(x);
        name = new ArrayList<>(n);
    }

    public void setclicker(recyclerclicklistner clicker) {
        this.clicker = clicker;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewtemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        holder.name.setText(name.get(position).getName());
        ViewCompat.setTransitionName(holder.itemView, name.get(position).getName());

        if (sprite.size() > position) {
            if (sprite.get(position) == null) {
                holder.imagenotavailable.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.INVISIBLE);
                holder.imageloading.setVisibility(View.GONE);
            } else {
                holder.image.setVisibility(View.VISIBLE);
                holder.imagenotavailable.setVisibility(View.GONE);
                Picasso.get().load(sprite.get(position)).into(holder.image);
                holder.imageloading.setVisibility(View.GONE);
            }
        } else {
            holder.imagenotavailable.setVisibility(View.GONE);
            holder.image.setVisibility(View.INVISIBLE);
            holder.imageloading.setVisibility(View.VISIBLE);
        }

        if (clicker != null)
            holder.itemView.setOnClickListener(v -> {
                if (clicker != null)
                    if (position != RecyclerView.NO_POSITION)
                        clicker.click(position + 1, holder.itemView, name.get(position));
            });
    }

    @Override
    public int getItemCount() {

        if (sprite.size() < 20)
            return name.size();
        return sprite.size();
    }

    public void change(ArrayList<NameandUrl> n, ArrayList<String> x) {
        sprite = new ArrayList<>(x);
        name = new ArrayList<>(n);
        Log.i("wlajehdiklhxwa;lsdjfcx;ASWkxaw", name.size() + "\n" + sprite.size());
        notifyDataSetChanged();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, imagenotavailable;
        ProgressBar imageloading;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            imageloading = itemView.findViewById(R.id.imageprogress);
            imagenotavailable = itemView.findViewById(R.id.notavailable);
        }
    }


}

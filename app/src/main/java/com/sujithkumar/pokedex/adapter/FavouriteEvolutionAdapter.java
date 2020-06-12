package com.sujithkumar.pokedex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujithkumar.pokedex.R;

import java.util.ArrayList;

public class FavouriteEvolutionAdapter extends RecyclerView.Adapter<FavouriteEvolutionAdapter.viewholder> {

    ArrayList<String> name;

    public FavouriteEvolutionAdapter(ArrayList<String> temp) {
        name = temp;
    }


    @NonNull
    @Override
    public FavouriteEvolutionAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.evolutiontemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteEvolutionAdapter.viewholder holder, int position) {
        holder.name.setText(name.get(position));
        if (position == name.size() - 1)
            holder.downarrow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView downarrow;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
            downarrow = itemView.findViewById(R.id.downarrow);
        }
    }


}

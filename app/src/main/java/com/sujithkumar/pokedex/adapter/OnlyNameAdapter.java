package com.sujithkumar.pokedex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.recyclerclicklistner;

import java.util.ArrayList;

public class OnlyNameAdapter extends RecyclerView.Adapter<OnlyNameAdapter.viewholder> {

    ArrayList<NameandUrl> list;
    recyclerclicklistner clicker;

    public OnlyNameAdapter(ArrayList<NameandUrl> listt) {
        list = listt;
    }

    public void setclicker(recyclerclicklistner clicker) {
        this.clicker = clicker;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.onlynametemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.name.setText(list.get(position).getName());
        if (clicker != null)
            holder.itemView.setOnClickListener(v -> {
                if (clicker != null)
                    if (position != RecyclerView.NO_POSITION)
                        clicker.click(position + 1, holder.itemView, list.get(position));
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void change(ArrayList<NameandUrl> n) {
        list = new ArrayList<>(n);
        notifyDataSetChanged();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView name;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }


}

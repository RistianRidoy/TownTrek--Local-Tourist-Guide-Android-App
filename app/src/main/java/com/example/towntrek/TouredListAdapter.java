package com.example.towntrek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TouredListAdapter extends RecyclerView.Adapter<TouredListAdapter.ViewHolder> {

    private ArrayList<String> touredPlaces;

    public TouredListAdapter(ArrayList<String> touredPlaces) {
        this.touredPlaces = touredPlaces;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toured_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String place = touredPlaces.get(position);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return touredPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.toured_place_name);
        }

        public void bind(String place) {
            placeName.setText(place);
        }
    }
}


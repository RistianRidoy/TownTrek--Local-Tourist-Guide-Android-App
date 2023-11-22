package com.example.towntrek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private ArrayList<String> bucketList;

    public BucketListAdapter(ArrayList<String> bucketList) {
        this.bucketList = bucketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String place = bucketList.get(position);
        holder.placeTextView.setText(place);
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.placeTextView);
        }
    }
}


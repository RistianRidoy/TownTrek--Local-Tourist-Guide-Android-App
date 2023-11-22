package com.example.towntrek.PlanningScreenAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.towntrek.MapActivity;
import com.example.towntrek.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter <FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedHelperClass> featuredLocations;
    private static OnItemClickListener mListener;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public FeaturedAdapter(Context context, ArrayList<FeaturedHelperClass> featuredLocations) {
        this.featuredLocations = featuredLocations;
        this.context = context;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        holder.image.setImageResource(featuredLocations.get(position).getImage());
        holder.title.setText(featuredLocations.get(position).getTitle());
        holder.description.setText(featuredLocations.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, description;
        CardView cardView;

        public FeaturedViewHolder(@NonNull View itemView){
            super((itemView));

            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_desc);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            FeaturedHelperClass item = featuredLocations.get(position);
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("title", featuredLocations.get(position).getTitle());
            intent.putExtra("latitude", item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());
            context.startActivity(intent);
        }
    }
}


package com.example.rumi.tourassistant.Nearby;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rumi.tourassistant.R;

import java.util.List;

public class NearByPlaceAdapter extends RecyclerView.Adapter<NearByPlaceAdapter.PlaceViewHolder>{
    private List<Result>results;

    public NearByPlaceAdapter(List<Result> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_place_row,parent,false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.nameTv.setText(results.get(position).getName());
        holder.ratingTv.setText(String.valueOf(results.get(position).getRating()));
        holder.addressTv.setText(results.get(position).getVicinity());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, ratingTv, addressTv , openorclose;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.row_place_name);
            ratingTv = itemView.findViewById(R.id.row_place_rating);
            addressTv = itemView.findViewById(R.id.row_place_address);
        }
    }

    public void updateList(List<Result>updatedList){
        this.results = updatedList;
        notifyDataSetChanged();
    }
}

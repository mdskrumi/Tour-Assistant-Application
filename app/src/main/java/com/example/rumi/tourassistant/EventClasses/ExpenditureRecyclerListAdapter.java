package com.example.rumi.tourassistant.EventClasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.rumi.tourassistant.ExpenditureDetailViewActivity;
import com.example.rumi.tourassistant.ExpenditureUpAndDelActivity;
import com.example.rumi.tourassistant.HomePageActivity;
import com.example.rumi.tourassistant.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ExpenditureRecyclerListAdapter extends RecyclerView.Adapter<ExpenditureRecyclerListAdapter.ExpenditureViewHolder>  {

    private String eventId;
    private List<Expenditure>expenditures ;
    public ExpenditureRecyclerListAdapter(List<Expenditure> expenditures , String eventId) {
        this.expenditures = expenditures;
        this.eventId = eventId;
    }
    @NonNull
    @Override
    public ExpenditureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenditure_recycler_view_row_layout,parent,false);
        ExpenditureViewHolder expenditureViewHolder = new ExpenditureViewHolder(view);
        return expenditureViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ExpenditureViewHolder holder, final int position) {
        holder.expenditureNameTv.setText(expenditures.get(position).getItemName());
        holder.expenditureTypeTv.setText(expenditures.get(position).getItemType());
        holder.expenditureCostTv.setText(Double.toString(expenditures.get(position).getValue()));
        holder.expenditureQuatityTv.setText(Integer.toString(expenditures.get(position).getQuantity()));
        holder.expenditureNetCostTv.setText(Long.toString( (long) expenditures.get(position).getValue()*expenditures.get(position).getQuantity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ExpenditureUpAndDelActivity.class)
                        .putExtra("id",expenditures.get(position).getExpenditureId())
                        .putExtra("eventId",eventId)
                        .putExtra("WHAT_TO_DO" , 1));
            }
        });

    }
    @Override
    public int getItemCount() {
        return expenditures.size();
    }
    public static class ExpenditureViewHolder extends RecyclerView.ViewHolder {
        TextView expenditureNameTv, expenditureTypeTv , expenditureCostTv , expenditureQuatityTv , expenditureNetCostTv;
        public ExpenditureViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureNameTv = itemView.findViewById(R.id.expenditureItemNameForListRow);
            expenditureTypeTv = itemView.findViewById(R.id.expenditureItemTypeForListRow);
            expenditureCostTv = itemView.findViewById(R.id.expenditureItemCostForListRow);
            expenditureQuatityTv = itemView.findViewById(R.id.expenditureItemQuantityForListRow);
            expenditureNetCostTv = itemView.findViewById(R.id.expenditureItemNetCostForListRow);
        }
    }

    public void updateList(List<Expenditure>expenditure){
        this.expenditures = expenditure;
        notifyDataSetChanged();
    }

}

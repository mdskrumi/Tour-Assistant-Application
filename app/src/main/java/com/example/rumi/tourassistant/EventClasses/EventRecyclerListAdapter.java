package com.example.rumi.tourassistant.EventClasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rumi.tourassistant.EventDetailViewActivity;
import com.example.rumi.tourassistant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventRecyclerListAdapter extends RecyclerView.Adapter<EventRecyclerListAdapter.EventListViewHolder>  {

    private ArrayList<Event> eventList;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    public EventRecyclerListAdapter(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_view_row_layout,parent,false);
        EventListViewHolder eventListViewHolder = new EventListViewHolder(view);
        return eventListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListViewHolder holder, final int position) {
        holder.eventName.setText(eventList.get(position).getEventName());
        holder.eventDestination.setText(eventList.get(position).getDestinationName());
        holder.eventStartDate.setText(dateFormatter.format(eventList.get(position).getEventStartDate()));
        holder.eventStatus.setText(eventList.get(position).getEventStatus());
        holder.eventEndDate.setText(dateFormatter.format(eventList.get(position).getEndingDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EventDetailViewActivity.class).putExtra("eventId",eventList.get(position).getEventId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventListViewHolder extends RecyclerView.ViewHolder{
        TextView eventName,  eventDestination,eventStartDate ,eventEndDate , eventStatus;
        public EventListViewHolder(@NonNull final View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameForListRow);
            eventDestination = itemView.findViewById(R.id.eventdestinationForListRow);
            eventStartDate = itemView.findViewById(R.id.eventStartingDateForListRow);
            eventEndDate = itemView.findViewById(R.id.eventEndingDateForListRow);
            eventStatus = itemView.findViewById(R.id.eventStatusForListRow);

        }
    }

    public void updateList(ArrayList<Event>events){
        this.eventList = events;
        notifyDataSetChanged();
    }


}

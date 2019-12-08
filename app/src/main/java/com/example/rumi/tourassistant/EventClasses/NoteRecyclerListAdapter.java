package com.example.rumi.tourassistant.EventClasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rumi.tourassistant.ExpenditureUpAndDelActivity;
import com.example.rumi.tourassistant.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoteRecyclerListAdapter extends RecyclerView.Adapter<NoteRecyclerListAdapter.NoteViewHolder> {

    private List<Note> notes;
    private String eventId;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    public NoteRecyclerListAdapter(List<Note> notes, String eventId) {
        this.notes = notes;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recycler_view_row_layout,parent,false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
        holder.note.setText(notes.get(position).getNote());
        holder.date.setText(dateFormatter.format(notes.get(position).getNoteDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ExpenditureUpAndDelActivity.class)
                        .putExtra("noteId",notes.get(position).getNoteId())
                        .putExtra("eventId",eventId)
                        .putExtra("WHAT_TO_DO" , 2));
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView note , date ;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.noteForListRow);
            date = itemView.findViewById(R.id.notedateForListRow);
        }
    }

    public void updateList(List<Note>notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
}

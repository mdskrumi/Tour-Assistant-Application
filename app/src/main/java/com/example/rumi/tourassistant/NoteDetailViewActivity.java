package com.example.rumi.tourassistant;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rumi.tourassistant.EventClasses.Note;
import com.example.rumi.tourassistant.EventClasses.NoteRecyclerListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteDetailViewActivity extends AppCompatActivity {

    private RecyclerView noteRecyclerView;
    private NoteRecyclerListAdapter noteRecyclerListAdapter;
    private LinearLayoutManager llm;
    private List<Note> notes = new ArrayList<>();
    private ImageView addNoteImageButton;

    private TextView noData;

    private DatabaseReference  noteRef;
    private FirebaseUser firebaseUser;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail_view);

        noData = findViewById(R.id.noNoteTextView);

        eventId =  getIntent().getStringExtra("eventId");

        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        addNoteImageButton = findViewById(R.id.addNewNoteImageButton);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        noteRef = FirebaseDatabase.getInstance()
                .getReference("Tour AssistantUser")
                .child("Users")
                .child(firebaseUser.getUid())
                .child("Events")
                .child(eventId)
                .child("eventNotes");


        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    notes.add(ds.getValue(Note.class));
                }
                if(noteRecyclerListAdapter == null){
                    noteRecyclerListAdapter = new NoteRecyclerListAdapter(notes , eventId);
                    llm = new LinearLayoutManager(NoteDetailViewActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    noteRecyclerView.setLayoutManager(llm);
                    noteRecyclerView.setAdapter(noteRecyclerListAdapter);
                    if(notes.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }else
                        noData.setVisibility(View.GONE);
                }
                else{
                    noteRecyclerListAdapter.updateList(notes);
                    if(notes.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }else
                        noData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addNoteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(NoteDetailViewActivity.this).inflate(R.layout.create_new_note_dialog,null);

                final EditText noteET = view.findViewById(R.id.noteInDialog);
                Button doneButton = view.findViewById(R.id.doneNoteInDialogButton);
                Button cancelButton = view.findViewById(R.id.cancleNoteInDialogButton);

                final AlertDialog alertDialog = new AlertDialog.Builder(NoteDetailViewActivity.this)
                        .setView(view)
                        .show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note = noteET.getText().toString();
                        if(note.isEmpty()){
                            noteET.setError("This Field is Required");
                            return;
                        }
                        Date date = Calendar.getInstance().getTime();
                        String noteId = noteRef.push().getKey();
                        Note n = new Note(noteId,note,date);
                        noteRef.child(noteId).setValue(n);
                        alertDialog.cancel();
                    }
                });

            }
        });

    }
}

package com.example.rumi.tourassistant;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.EventClasses.Event;
import com.example.rumi.tourassistant.EventClasses.Expenditure;
import com.example.rumi.tourassistant.EventClasses.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class ExpenditureUpAndDelActivity extends AppCompatActivity {


    private Expenditure expenditure;
    private Event event;
    private Note myNote;
    private DatabaseReference expenditureRef , eventRef , toexpenditureRef , noteRef , toNoteRef;
    private FirebaseUser firebaseUser;

    private Button update , delete ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_up_and_del);

        delete = findViewById(R.id.deleteInActivityButton);
        update = findViewById(R.id.updateInActivityButton);

        String eventId = getIntent().getStringExtra("eventId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        eventRef = FirebaseDatabase.getInstance()
                .getReference("Tour AssistantUser")
                .child("Users")
                .child(firebaseUser.getUid())
                .child("Events")
                .child(eventId);

        final String id = getIntent().getStringExtra("id");
        final String noteId = getIntent().getStringExtra("noteId");

        final int WHAT_TO_DO = getIntent().getIntExtra("WHAT_TO_DO",0);


        if(WHAT_TO_DO == 1){
            expenditureRef = FirebaseDatabase.getInstance()
                    .getReference("Tour AssistantUser")
                    .child("Users")
                    .child(firebaseUser.getUid())
                    .child("Events")
                    .child(eventId)
                    .child("eventExpenditures");

            toexpenditureRef = expenditureRef.child(id);

            eventRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    event = dataSnapshot.getValue(Event.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            toexpenditureRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    expenditure = dataSnapshot.getValue(Expenditure.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expenditureRef.child(id).removeValue();
                    onBackPressed();
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(ExpenditureUpAndDelActivity.this).inflate(R.layout.create_new_expenditure_dialog,null);
                    final EditText nameET , costET , quantityET;
                    final TextView tilte;
                    final Spinner typeSPN;
                    Button doneButton , cancelButton;

                    tilte = view.findViewById(R.id.titleofdialog);
                    nameET = view.findViewById(R.id.expenditureNameinDialog);
                    costET = view.findViewById(R.id.exoenditureCostInDialog);
                    quantityET = view.findViewById(R.id.exoenditureQuantityInDialog);
                    typeSPN = view.findViewById(R.id.expenditureTypeSpinnerInDialog);
                    doneButton = view.findViewById(R.id.doneExpenditureInDialogButton);
                    cancelButton = view.findViewById(R.id.cancleExpenditureInDialogButton);

                    tilte.setText("Update Expenditure");
                    nameET.setText(expenditure.getItemName());
                    costET.setText(Double.toString(expenditure.getValue()));
                    quantityET.setText(Integer.toString(expenditure.getQuantity()));

                    final AlertDialog alertDialog = new AlertDialog.Builder(ExpenditureUpAndDelActivity.this)
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
                            String name = nameET.getText().toString();
                            String squantity = quantityET.getText().toString();
                            String svalue = costET.getText().toString();
                            String type = typeSPN.getSelectedItem().toString();
                            if(name.isEmpty()){
                                nameET.setError("This Field is Required");
                                return;
                            }
                            if(squantity.isEmpty()){
                                quantityET.setError("This Field is Required");
                                return;
                            }
                            if(svalue.isEmpty()){
                                costET.setError("This Field is Required");
                                return;
                            }
                            int quantity = Integer.parseInt(squantity);
                            double value = Double.parseDouble(svalue);

                            if(event.getNetExpenses() + (quantity*value) > event.getEventBudget()){
                                Toast.makeText(ExpenditureUpAndDelActivity.this, "Budget Limit Exceeded", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Expenditure expenditure = new Expenditure( id,  name,  type,  quantity, value);
                            expenditureRef.child(id).setValue(expenditure);
                            alertDialog.cancel();
                            onBackPressed();

                        }
                    });
                }
            });
        }
        else if(WHAT_TO_DO == 2){
            noteRef = FirebaseDatabase.getInstance()
                    .getReference("Tour AssistantUser")
                    .child("Users")
                    .child(firebaseUser.getUid())
                    .child("Events")
                    .child(eventId)
                    .child("eventNotes");

            toNoteRef = noteRef.child(noteId);


            toNoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myNote = dataSnapshot.getValue(Note.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteRef.child(noteId).removeValue();
                    onBackPressed();
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(ExpenditureUpAndDelActivity.this).inflate(R.layout.create_new_note_dialog,null);

                    final EditText noteET = view.findViewById(R.id.noteInDialog);
                    Button doneButton = view.findViewById(R.id.doneNoteInDialogButton);
                    Button cancelButton = view.findViewById(R.id.cancleNoteInDialogButton);

                    noteET.setText(myNote.getNote());

                    final AlertDialog alertDialog = new AlertDialog.Builder(ExpenditureUpAndDelActivity.this)
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
                            Note n = new Note(noteId,note,date);
                            noteRef.child(noteId).setValue(n);
                            alertDialog.cancel();
                            onBackPressed();
                        }
                    });
                }
            });

        }
        else {
            onBackPressed();
        }




    }

}

package com.example.rumi.tourassistant;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.EventClasses.Event;
import com.example.rumi.tourassistant.EventClasses.Expenditure;
import com.example.rumi.tourassistant.EventClasses.ExpenditureRecyclerListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenditureDetailViewActivity extends AppCompatActivity {

    private RecyclerView expenditureRecyclerView;
    private ExpenditureRecyclerListAdapter expenditureRecyclerListAdapter;
    private List<Expenditure> expenditures = new ArrayList<>();
    private LinearLayoutManager llm;

    private TextView noData;

    private ImageView addNewExpenditure;
    private Spinner expenditureFilterSpinner;

    private DatabaseReference expenditureRef , eventRef;
    private FirebaseUser firebaseUser;

    private Event event;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_detail_view);

        noData = findViewById(R.id.noExpenditureTextView);

        expenditureRecyclerView = findViewById(R.id.expenditureRecyclerView);

        addNewExpenditure = findViewById(R.id.addNewExpenditureImageButton);
        expenditureFilterSpinner = findViewById(R.id.expenditureFilterSpinner);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        eventId =  getIntent().getStringExtra("eventId");


        eventRef = FirebaseDatabase.getInstance()
                .getReference("Tour AssistantUser")
                .child("Users")
                .child(firebaseUser.getUid())
                .child("Events")
                .child(eventId);

        expenditureRef = FirebaseDatabase.getInstance()
                .getReference("Tour AssistantUser")
                .child("Users")
                .child(firebaseUser.getUid())
                .child("Events")
                .child(eventId)
                .child("eventExpenditures");


        expenditureFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = expenditureFilterSpinner.getSelectedItem().toString();
                if(query.equals("All")){
                    expenditureRecyclerListAdapter.updateList(expenditures);
                    if(expenditures.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }
                    else{
                        noData.setVisibility(View.GONE);
                    }
                }
                else{
                    List<Expenditure>filtered = new ArrayList<>();
                    for(int i = 0 ; i < expenditures.size(); i++){
                        if(expenditures.get(i).getItemType().equals(query)){
                            filtered.add(expenditures.get(i));
                        }
                    }
                    if(filtered.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }
                    else{
                        noData.setVisibility(View.GONE);
                    }
                    expenditureRecyclerListAdapter.updateList(filtered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expenditureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenditures.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    expenditures.add(ds.getValue(Expenditure.class));
                }
                if(expenditureRecyclerListAdapter == null){
                    expenditureRecyclerListAdapter = new ExpenditureRecyclerListAdapter(expenditures,eventId);
                    llm = new LinearLayoutManager(ExpenditureDetailViewActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    expenditureRecyclerView.setLayoutManager(llm);
                    expenditureRecyclerView.setAdapter(expenditureRecyclerListAdapter);
                    if(expenditures.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }
                    else{
                        noData.setVisibility(View.GONE);
                    }
                }
                else{
                    expenditureRecyclerListAdapter.updateList(expenditures);
                    if(expenditures.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    }
                    else{
                        noData.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        addNewExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(ExpenditureDetailViewActivity.this).inflate(R.layout.create_new_expenditure_dialog,null);
                final EditText nameET , costET , quantityET  ;
                final TextView tilte;
                final Spinner typeSPN;
                Button doneButton , cancelButton;
                nameET = view.findViewById(R.id.expenditureNameinDialog);
                costET = view.findViewById(R.id.exoenditureCostInDialog);
                quantityET = view.findViewById(R.id.exoenditureQuantityInDialog);
                typeSPN = view.findViewById(R.id.expenditureTypeSpinnerInDialog);
                doneButton = view.findViewById(R.id.doneExpenditureInDialogButton);
                cancelButton = view.findViewById(R.id.cancleExpenditureInDialogButton);
                tilte = view.findViewById(R.id.titleofdialog);

                tilte.setText("New Expenditure");

                final AlertDialog alertDialog = new AlertDialog.Builder(ExpenditureDetailViewActivity.this)
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
                        double value = Math.round(Double.parseDouble(svalue));

                        if(event.getNetExpenses() + (quantity*value) > event.getEventBudget()){
                            Toast.makeText(ExpenditureDetailViewActivity.this, "Budget Limit Exceeded", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String expenditureId = expenditureRef.push().getKey();
                        Expenditure expenditure = new Expenditure( expenditureId,  name,  type,  quantity, value);
                        expenditureRef.child(expenditureId).setValue(expenditure);
                        alertDialog.cancel();

                    }
                });
            }
        });

    }

}

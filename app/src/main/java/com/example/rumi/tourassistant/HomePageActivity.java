package com.example.rumi.tourassistant;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.EventClasses.Event;
import com.example.rumi.tourassistant.EventClasses.EventRecyclerListAdapter;
import com.example.rumi.tourassistant.EventClasses.Expenditure;
import com.example.rumi.tourassistant.EventClasses.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomePageActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private ImageView openDrawerButton , addNewEventButton;
    private TextView noEvent;
    private RecyclerView eventRecyclerView;
    private EventRecyclerListAdapter eventRecyclerListAdapter;
    private LinearLayoutManager llm;
    private  Calendar sC , eC;
    private SimpleDateFormat dateFormatter;
    private ArrayList<Event> events = new ArrayList<>();

    private int backclick = 0;


    private DatabaseReference rootRef , userRef , eventRef , updateEventRef;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        backclick = 0;

        noEvent = findViewById(R.id.eventNotFoundTextView);
        drawerLayout = findViewById(R.id.drawer_layout);
        openDrawerButton = findViewById(R.id.openDrawerButtonImage);
        addNewEventButton = findViewById(R.id.addNewEventImageButton);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView headerEmail = headerView.findViewById(R.id.nevigationHeaderUserEmailTextView);


        sC = Calendar.getInstance();
        eC = sC;

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference("Tour AssistantUser").child("Users");

        //Log.e("MY ROOT REF", rootRef.toString() );

        if(firebaseUser != null){
            userRef = rootRef.child(firebaseUser.getUid());
            eventRef = userRef.child("Events");
            headerEmail.setText(firebaseUser.getEmail().toString());
        }




        //******************************************** On Click Listeners From Here **********************************************************




       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.weather_menu_item){
                    startActivity(new Intent(HomePageActivity.this,WeatherActivity.class));
                }
                if(item.getItemId() == R.id.log_out_menu_item){
                    FirebaseAuth.getInstance().signOut();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                }
                if(item.getItemId() == R.id.map_menu_item){
                    startActivity(new Intent(HomePageActivity.this,MapsActivity.class));
                }
                if(item.getItemId() == R.id.nearby_menu_item){
                    startActivity(new Intent(HomePageActivity.this,NearByPlacesActivity.class));
                }

                return false;
            }
        });

        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
                backclick = 0;
            }
        });




        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Event incomingEvent = ds.getValue(Event.class);

                   /* incomingEvent.setCurrentDate(sC.getTime());

                    if(incomingEvent.getCurrentDate().after(incomingEvent.getEventStartDate())){
                        incomingEvent.setEventStatus("Running");
                    }

                    if(incomingEvent.getCurrentDate().after(incomingEvent.getEndingDate())){
                        incomingEvent.setEventStatus("Finished");
                    }

                    updateEventRef = eventRef.child(incomingEvent.getEventId());
                    updateEventRef.setValue(incomingEvent);*/

                    events.add(incomingEvent);
                }

                if(eventRecyclerListAdapter == null){
                    eventRecyclerListAdapter = new EventRecyclerListAdapter(events);
                    llm = new LinearLayoutManager(HomePageActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    eventRecyclerView.setLayoutManager(llm);
                    eventRecyclerView.setAdapter(eventRecyclerListAdapter);
                }else {
                    eventRecyclerListAdapter.updateList(events);
                }

                if(events.isEmpty()){
                   noEvent.setVisibility(View.VISIBLE);
                }
                else{
                   noEvent.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.create_event_dialog,null);
                final EditText eventName = view.findViewById(R.id.eventNameinDialog);
                final EditText eventDestination = view.findViewById(R.id.eventdestinationNameinDialog);
                final EditText eventBudget = view.findViewById(R.id.eventBudgetinDialog);
                final TextView eventStartDate = view.findViewById(R.id.selectedStartingDateByDatePickerinDialog);
                final TextView eventEndDate = view.findViewById(R.id.selectedEndingDateByDatePickerinDialog);

                Button showStartDatePicker = view.findViewById(R.id.showStartingDatePickerButtoninDialog);
                Button showEndDatePicker = view.findViewById(R.id.showEndingDatePickerButtoninDialog);

                Button createEventButton = view.findViewById(R.id.createEventButtoninDialog);
                Button cancel = view.findViewById(R.id.cancleCreatingButton);

                final AlertDialog alertDialog = new AlertDialog.Builder(HomePageActivity.this)
                        .setView(view)
                        .show();
                final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        sC.set(Calendar.YEAR, year);
                        sC.set(Calendar.MONTH, monthOfYear);
                        sC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        eventStartDate.setText(dateFormatter.format(sC.getTime()).toString());
                    }
                };

                showStartDatePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(HomePageActivity.this, sDate, sC
                                .get(Calendar.YEAR), sC.get(Calendar.MONTH),
                                sC.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                final DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        eC.set(Calendar.YEAR, year);
                        eC.set(Calendar.MONTH, monthOfYear);
                        eC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        eventEndDate.setText(dateFormatter.format(eC.getTime()).toString());
                    }
                };
                showEndDatePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(HomePageActivity.this, eDate, eC
                                .get(Calendar.YEAR), eC.get(Calendar.MONTH),
                                eC.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                createEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = eventName.getText().toString();
                        String destination = eventDestination.getText().toString();
                        String budget = eventBudget.getText().toString();
                        Date start = sC.getTime();
                        Date end =  eC.getTime();
                        Date current =  Calendar.getInstance().getTime();
                        String status;
                        if(name.isEmpty()){
                            eventName.setError("This Field is Required");
                            return;
                        }
                        if(destination.isEmpty()){
                            eventDestination.setError("This Field is Required");
                            return;
                        }
                        if(budget.isEmpty()){
                            eventBudget.setError("This Field is Required");
                            return;
                        }
                        if(end.before(current) || end.before(start)){
                            Toast.makeText(HomePageActivity.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(start.before(current)){
                            status = "Running";
                        }
                        else if(start.after(current)){
                            status = "Upcoming";
                        }
                        else status = "Finished";

                        String eventId = eventRef.push().getKey();
                        Event event = new Event( eventId , name,  destination, start, current,  end, status, Integer.parseInt(budget), 0 , new HashMap<String, Note>(), new HashMap<String, Expenditure>()) ;
                        eventRef.child(eventId).setValue(event);

                        sC = Calendar.getInstance();
                        eC = sC;
                        alertDialog.cancel();
                    }
                });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        backclick = 0;
        if(events.isEmpty()){
            noEvent.setVisibility(View.VISIBLE);
        }
        else{
            noEvent.setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            if(backclick==0){
                backclick = 1;
                Toast.makeText(this, "Press Back Again To Log Out ", Toast.LENGTH_SHORT).show();
            }else
                super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        backclick = 0;
        super.onRestart();
    }

}

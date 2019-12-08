package com.example.rumi.tourassistant;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.EventClasses.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailViewActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private TextView eventName , budgetInNumber ;
    private ProgressBar budgetProgress;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    private String mCurrentPhotoPath;

    private Button expenditureButton, noteButton, eventButton, capturePhoto , addBudgetButton;
    private String eventId;


    private Event event;

    private DatabaseReference eventRef;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_view);

        eventId = getIntent().getStringExtra("eventId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        eventRef = FirebaseDatabase.getInstance()
                .getReference("Tour AssistantUser")
                .child("Users")
                .child(firebaseUser.getUid())
                .child("Events")
                .child(eventId);

        eventName = findViewById(R.id.detailEventNameTextView);
        budgetInNumber = findViewById(R.id.detailbudgetStatusInNumberTextView);
        budgetProgress = findViewById(R.id.determinateBar);

        addBudgetButton = findViewById(R.id.detailEventAddBudgetButton);
        expenditureButton = findViewById(R.id.detailEventExpenditureButton);
        noteButton = findViewById(R.id.detailEventNoteButton);
        eventButton = findViewById(R.id.detailEventButton);
        capturePhoto = findViewById(R.id.detailCapturePhotoButton);





        //***************************** ON CLICK LISTENERS ****************************************
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                if(event == null){
                    return;
                }
                Toast.makeText(EventDetailViewActivity.this, "Today's Date : " + dateFormatter.format(event.getCurrentDate())  , Toast.LENGTH_LONG).show();
                eventName.setText(event.getEventName());
                budgetProgress.setMax(event.getEventBudget());
                if (event.getEventExpenditures() == null) {
                    budgetInNumber.setText("( 0/" + event.getEventBudget() + ")");
                    budgetProgress.setProgress(0);
                }
                else {
                    budgetInNumber.setText("( " +  (long)event.getNetExpenses() + "/" + event.getEventBudget() + ")");
                    budgetProgress.setProgress((int) event.getNetExpenses());
                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        addBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(EventDetailViewActivity.this).inflate(R.layout.add_budget_dialog,null);

                final EditText budgetET = view.findViewById(R.id.addBudgetAmountInDialog);
                Button cancel = view.findViewById(R.id.cancleButtonInAddBudgetDialog);
                Button add = view.findViewById(R.id.addButtonInAddBudgetDialog);
                final AlertDialog alertDialog = new AlertDialog.Builder(EventDetailViewActivity.this)
                        .setView(view)
                        .show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String check = budgetET.getText().toString();
                    if(check.isEmpty()){
                        budgetET.setError("This Field is Required");
                        return;
                    }
                    int aBudget = Integer.parseInt(check);
                 //  ( ,  ,  ,  ,  ,  endingDate,  eventStatus, eventBudget, expenditure, HashMap<String, Note> eventNotes, HashMap<String, Expenditure> eventExpenditures)
                    eventRef.setValue(new Event(eventId,event.getEventName(),event.getDestinationName(),event.getEventStartDate(),event.getCurrentDate()
                    ,event.getEndingDate(),event.getEventStatus(),event.getEventBudget()+ aBudget , event.getExpenditure() , event.getEventNotes(),event.getEventExpenditures()));
                alertDialog.cancel();
                }
            });

            }
        });


        expenditureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailViewActivity.this,ExpenditureDetailViewActivity.class)
                        .putExtra("eventId",eventId));
            }
        });

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailViewActivity.this,NoteDetailViewActivity.class)
                        .putExtra("eventId",eventId));
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(EventDetailViewActivity.this,eventButton);
                popupMenu.getMenuInflater().inflate(R.menu.update_delete_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.update_menu_item:
                                updateEvent();
                                break;
                            case R.id.delete_menu_item:
                                deleteEvent();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                if(mCurrentPhotoPath!=null){
                    galleryAddPic();
                }
            }
        });


    }

    private void deleteEvent(){
        eventRef.removeValue();
        onBackPressed();
    }

    private void updateEvent(){

        if(event.getEventStatus().equals("Running")){
            Toast.makeText(this, "Can Not Update a Running Event", Toast.LENGTH_SHORT).show();
            return;
        }

        final View view = LayoutInflater.from(EventDetailViewActivity.this).inflate(R.layout.create_event_dialog,null);

        final Calendar sC  = Calendar.getInstance() , eC = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final TextView title = view.findViewById(R.id.titleofdialog);
        title.setText("Update Event");
        final EditText eventName = view.findViewById(R.id.eventNameinDialog);
        final EditText eventDestination = view.findViewById(R.id.eventdestinationNameinDialog);
        final EditText eventBudget = view.findViewById(R.id.eventBudgetinDialog);
        final TextView eventStartDate = view.findViewById(R.id.selectedStartingDateByDatePickerinDialog);
        final TextView eventEndDate = view.findViewById(R.id.selectedEndingDateByDatePickerinDialog);

        eventName.setText(event.getEventName());
        eventDestination.setText(event.getDestinationName());


        Button showStartDatePicker = view.findViewById(R.id.showStartingDatePickerButtoninDialog);
        Button showEndDatePicker = view.findViewById(R.id.showEndingDatePickerButtoninDialog);

        Button createEventButton = view.findViewById(R.id.createEventButtoninDialog);
        Button cancel = view.findViewById(R.id.cancleCreatingButton);

        createEventButton.setText("Update");

        final AlertDialog alertDialog = new AlertDialog.Builder(EventDetailViewActivity.this)
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
                new DatePickerDialog(EventDetailViewActivity.this, sDate, sC
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
                new DatePickerDialog(EventDetailViewActivity.this, eDate, eC
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
                    Toast.makeText(EventDetailViewActivity.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(start.before(current)){
                    status = "Running";
                }
                else if(start.after(current)){
                    status = "Upcoming";
                }
                else status = "Finished";

                Event nEvent = new Event( eventId , name,  destination, start, current,  end, status, Integer.parseInt(budget), 0 , event.getEventNotes(), event.getEventExpenditures()) ;
                eventRef.setValue(nEvent);
                alertDialog.cancel();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = event.getEventName() + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}

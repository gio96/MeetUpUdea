package com.example.galonsogomez.meetupudea;

import android.content.ContentResolver;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener{

    private Button buttonDate, buttonStartHour,buttonFinishHour;
    private int dia,mes,año,hora,minutos;

    private  EditText editTextNameEvent,editTextPlaceEvent,editTextDateEvent, editTextStartHour,editTextFinishHour,editTextDescriptionEvent;
    private Button buttonAddImage,buttonCreateEvent;

    private ImageView imgPictureEvent;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgEventUri;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    //private FirebaseUser firebaseUser;

    String uidGroup = "";

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        //Get data from Activity
        Bundle b = getActivity().getIntent().getExtras();
        uidGroup = b.getString("groupUID");

        castingViews(view);
        initFirebase();
        return view;
    }

    //Show Image in ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imgEventUri = data.getData();
            imgPictureEvent.setImageURI(imgEventUri);
        }
    }

    private boolean checkFields(){

        //CHECK ASSISTANT FIELD
        if(editTextNameEvent.getText().toString().equals("") || editTextPlaceEvent.getText().toString().equals("") ||
                editTextDateEvent.getText().toString().equals("") || editTextStartHour.getText().toString().equals("") ||
                editTextDescriptionEvent.getText().toString().equals("") || editTextFinishHour.getText().toString().equals(""))
        {

            Toast.makeText(getActivity().getApplicationContext(),"Existen campos vacíos",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void castingViews(View view){
        buttonDate = (Button) view.findViewById(R.id.btn_Date_Event);
        buttonDate.setOnClickListener(this);

        buttonStartHour = (Button) view.findViewById(R.id.btn_Start_Hour_Event);
        buttonStartHour.setOnClickListener(this);

        buttonFinishHour = (Button) view.findViewById(R.id.btn_Finish_Hour_Event);
        buttonFinishHour.setOnClickListener(this);

        editTextNameEvent = (EditText) view.findViewById(R.id.text_Name_Event);
        editTextPlaceEvent = (EditText) view.findViewById(R.id.text_Place_Event);
        editTextDateEvent = (EditText) view.findViewById(R.id.text_Date_Event);
        editTextStartHour = (EditText) view.findViewById(R.id.text_Start_Hour_Event);
        editTextFinishHour = (EditText) view.findViewById(R.id.text_Finish_Hour_Event);
        editTextDescriptionEvent = (EditText) view.findViewById(R.id.text_Description_Event);

        buttonAddImage = (Button) view.findViewById(R.id.btn_Add_Picture_Event);
        buttonAddImage.setOnClickListener(this);

        buttonCreateEvent = (Button) view.findViewById(R.id.btn_Create_Event);
        buttonCreateEvent.setOnClickListener(this);

        imgPictureEvent = (ImageView) view.findViewById(R.id.img_Picture_Event);
    }

    //Open File
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    // create extension to the image selected
    private String getFileExtension(Uri uri){
        //-------------------------------------------------------------------------------------------------------
        //POSSIBLE ERROR ABOUT getContentResolver()
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_Create_Event:
                if(checkFields()){
                    uploadEvent();
                }
                break;

            case R.id.btn_Add_Picture_Event:
                openFileChooser();
                break;

            case R.id.btn_Date_Event:
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM");
                        calendar.set(Calendar.DAY_OF_MONTH,day);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);
                        //Log.d("fechass",simpleDateFormat.format(calendar.getStarHour()));
                        editTextDateEvent.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },dia,mes,año);
                datePickerDialog.updateDate(2018,0,1);
                datePickerDialog.show();
                break;

            case R.id.btn_Start_Hour_Event:
                final Calendar cal = Calendar.getInstance();
                hora = cal.get(Calendar.HOUR);
                minutos = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa");
                        cal.set(Calendar.HOUR,hourOfDay);
                        cal.set(Calendar.MINUTE,minute);
                        editTextStartHour.setText(simpleDateFormat.format(cal.getTime()));
                    }
                },hora,minutos, false);
                timePickerDialog.show();
                break;

            case R.id.btn_Finish_Hour_Event:
                final Calendar cal2 = Calendar.getInstance();
                hora = cal2.get(Calendar.HOUR);
                minutos = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog2 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa");
                        cal2.set(Calendar.HOUR,hourOfDay);
                        cal2.set(Calendar.MINUTE,minute);
                        editTextFinishHour.setText(simpleDateFormat.format(cal2.getTime()));
                    }
                },hora,minutos, false);
                timePickerDialog2.show();
                break;
        }
    }

    private void uploadEvent(){

        if(imgEventUri!=null){
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()+
                    "." + getFileExtension(imgEventUri));


            // to can obtain Url from storage
            UploadTask uploadTask = fileReference.putFile(imgEventUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String linkPicture = downloadUri.toString();
                        // Create object Event

                        setEvent(uidGroup,linkPicture);


                        Toast.makeText(getActivity().getApplicationContext(),"Evento creado",Toast.LENGTH_SHORT).show();


                        cleanFields();
                        setNotificationFollowing(uidGroup);
                        Log.d("createEvent", linkPicture);
                    }
                }
            });
        }
        else{
            //Possible Error with the context
            Toast.makeText(getActivity().getApplicationContext(),"Imagen no seleccionada",Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanFields(){

        editTextNameEvent.setText("");
        editTextPlaceEvent.setText("");
        editTextDateEvent.setText("");
        editTextStartHour.setText("");
        editTextFinishHour.setText("");
        editTextDescriptionEvent.setText("");
        imgPictureEvent.setImageResource(R.drawable.ic_photo_black_24dp);
        imgEventUri= null;
    }

   private void initFirebase(){
        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //POSSIBLE ERROR with the CONTEXT
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        mStorageReference = FirebaseStorage.getInstance().getReference("ImageEvent");
    }

    public void setEvent(String uidGroup,String linkPicture)
    {
        Event event = new Event();
            event = new Event(UUID.randomUUID().toString(),editTextNameEvent.getText().toString(),editTextPlaceEvent.getText().toString()
                    ,editTextDateEvent.getText().toString(), editTextStartHour.getText().toString(),editTextFinishHour.getText().toString(),linkPicture,editTextDescriptionEvent.getText().toString());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("groups").child(uidGroup).child("events").child(event.getEventUID()).setValue(event);
    }

    public void setNotificationFollowing(final String uidGroup){
        final String idGroup = uidGroup;

        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRefUser = mFirebaseDatabase.getReference().child("users");

        mDatabaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dsu : dataSnapshot.getChildren()) {
                            String userId = dsu.getKey();

//Problem Where Not  is found idGroup setValue create a new register with idgroup notification each user
                            FirebaseDatabase mFirebaseDatabaseNotification = FirebaseDatabase.getInstance();
                            DatabaseReference mDatabaseRefNotification = mFirebaseDatabaseNotification.getReference().child("users").child(userId).child("following");
                            //mDatabaseRefNotification.child("users").child(userId).child("following");
                            mDatabaseRefNotification.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //for(DataSnapshot ds : dataSnapshot.getChildren()){

                                        if(dataSnapshot.hasChild(idGroup)){
                                            dataSnapshot.getRef().child(idGroup).child("notification").setValue(true);

                                            Log.d("sefue", "existo");
                                        }else {
                                            Log.d("sefue", "No esta ese grupo");
                                        }
                                    //}
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("mani", "No hay ningun datos");
                    }
                });


    }

}

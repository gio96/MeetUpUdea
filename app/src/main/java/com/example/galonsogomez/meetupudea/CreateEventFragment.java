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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener{

    private Button buttonDate,buttonTime;
    private int dia,mes,año,hora,minutos;

    private  EditText editTextNameEvent,editTextPlaceEvent,editTextDateEvent,editTextTimeEvent,editTextDescriptionEvent,editTextQuantity;
    private Button buttonAddImage,buttonCreateEvent;
    private Switch aSwitch;

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

        Log.d("sies", uidGroup);

        castingViews(view);
        initFirebase();


        //Handle behavior of switch
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    Log.d("switch","verdad");
                    editTextQuantity.setEnabled(true);
                }else {
                    editTextQuantity.setEnabled(false);
                    editTextQuantity.setText("");
                    Log.d("switch","falso");
                }
            }
        });
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

    //Check form fields except for field Quantity
    private boolean checkFields(){

        //CHECK ASSISTANT FIELD
        if(editTextNameEvent.getText().toString().equals("") || editTextPlaceEvent.getText().toString().equals("") ||
                editTextDateEvent.getText().toString().equals("") || editTextTimeEvent.getText().toString().equals("") ||
                editTextDescriptionEvent.getText().toString().equals(""))
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

        buttonTime = (Button) view.findViewById(R.id.btn_Time_Event);
        buttonTime.setOnClickListener(this);

        editTextNameEvent = (EditText) view.findViewById(R.id.text_Name_Event);
        editTextPlaceEvent = (EditText) view.findViewById(R.id.text_Place_Event);
        editTextDateEvent = (EditText) view.findViewById(R.id.text_Date_Event);
        editTextTimeEvent = (EditText) view.findViewById(R.id.text_Time_Event);
        editTextDescriptionEvent = (EditText) view.findViewById(R.id.text_Description_Event);
        editTextQuantity = (EditText) view.findViewById(R.id.text_Quantity_Event);

        buttonAddImage = (Button) view.findViewById(R.id.btn_Add_Picture_Event);
        buttonAddImage.setOnClickListener(this);

        buttonCreateEvent = (Button) view.findViewById(R.id.btn_Create_Event);
        buttonCreateEvent.setOnClickListener(this);

        aSwitch = (Switch) view.findViewById(R.id.switch_Event);

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
                    public void onDateSet(DatePicker datePicker, int year, int monthOfyear, int dayOfMonth) {
                        datePicker.init(2018,9,28,null);
                        editTextDateEvent.setText(dayOfMonth+"/"+ (monthOfyear + 1)+"/"+year);
                    }
                },dia,mes,año);
                datePickerDialog.show();
                break;

            case R.id.btn_Time_Event:
                final Calendar cal = Calendar.getInstance();
                hora = cal.get(Calendar.HOUR);
                minutos = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        editTextTimeEvent.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,false);
                timePickerDialog.show();
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
                        // Add child
                        /*mDatabaseReference.child(group.getGroupUID()).setValue(group);*/
                        Toast.makeText(getActivity().getApplicationContext(),"Evento creado",Toast.LENGTH_SHORT).show();

                        cleanFields();
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
        editTextTimeEvent.setText("");
        editTextDescriptionEvent.setText("");
        editTextQuantity.setText("");
        imgPictureEvent.setImageDrawable(null);
    }

   private void initFirebase(){
        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //POSSIBLE ERROR with the CONTEXT
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        mStorageReference = FirebaseStorage.getInstance().getReference("ImageEvent");
    }

    public void setEvent(String uidGroup,String linkPicture)
    {
        Event event = new Event(UUID.randomUUID().toString(),editTextNameEvent.getText().toString(),editTextPlaceEvent.getText().toString()
                ,editTextDateEvent.getText().toString(),editTextTimeEvent.getText().toString(),linkPicture,editTextDescriptionEvent.getText().toString());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("groups").child(uidGroup).child("events").child(event.getEventUID()).setValue(event);
        Log.d("raro",event.getEventUID());
    }

}

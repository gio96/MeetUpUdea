package com.example.galonsogomez.meetupudea;


import android.content.ContentResolver;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCourseFragment extends Fragment  implements View.OnClickListener{

    // Views
    private EditText textTitleCourse,textPlaceCourse,textScheduleCourse,textDescriptionCourse;
    private ImageView imgPictureCourse;
    private Button buttonAddImage, buttonCreateCourse;


    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgCourseUri;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    //private FirebaseUser firebaseUser;

    String uidGroup = "";

    public CreateCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_course, container, false);

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
            imgCourseUri = data.getData();
            imgPictureCourse.setImageURI(imgCourseUri);
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_Create_Course:
                if (checkFields()) {
                    uploadEvent();
                }
                break;

            case R.id.btn_Add_Picture_Course:
                openFileChooser();
                break;

        }
    }

    private boolean checkFields(){

        //CHECK ASSISTANT FIELD
        if(textTitleCourse.getText().toString().equals("") || textPlaceCourse.getText().toString().equals("") ||
                textScheduleCourse.getText().toString().equals("") || textDescriptionCourse.getText().toString().equals(""))
        {
            Toast.makeText(getActivity().getApplicationContext(),"Existen campos vacíos",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void castingViews(View view){

        textTitleCourse = view.findViewById(R.id.text_Name_Course);
        textPlaceCourse =  view.findViewById(R.id.text_Place_Course);
        textScheduleCourse = view.findViewById(R.id.text_Schedule_Course);
        textDescriptionCourse = view.findViewById(R.id.text_Description_Course);


        buttonAddImage =  view.findViewById(R.id.btn_Add_Picture_Course);
        buttonAddImage.setOnClickListener(this);

        buttonCreateCourse =  view.findViewById(R.id.btn_Create_Course);
        buttonCreateCourse.setOnClickListener(this);

        imgPictureCourse = (ImageView) view.findViewById(R.id.img_Picture_Course);
    }

    private void initFirebase(){

        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        mStorageReference = FirebaseStorage.getInstance().getReference("ImageCourse");
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

    private void uploadEvent(){

        if(imgCourseUri!=null){
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()+
                    "." + getFileExtension(imgCourseUri));


            // to can obtain Url from storage
            UploadTask uploadTask = fileReference.putFile(imgCourseUri);
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

                        setCourse(uidGroup,linkPicture);
                        // Add child
                        /*mDatabaseReference.child(group.getGroupUID()).setValue(group);*/
                        Toast.makeText(getActivity().getApplicationContext(),"Curso creado",Toast.LENGTH_SHORT).show();

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


    public void setCourse(String uidGroup,String linkPicture){

        Course course = new Course();
        course = new Course(UUID.randomUUID().toString(),textTitleCourse.getText().toString(),textPlaceCourse.getText().toString()
                ,textScheduleCourse.getText().toString(),linkPicture,textDescriptionCourse.getText().toString());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("groups").child(uidGroup).child("courses").child(course.getCourseUID()).setValue(course);
    }

    private void cleanFields(){

        textTitleCourse.setText("");
        textPlaceCourse.setText("");
        textScheduleCourse.setText("");
        textDescriptionCourse.setText("");
        imgPictureCourse.setImageResource(R.drawable.ic_photo_black_24dp);
        imgCourseUri= null;
    }

}

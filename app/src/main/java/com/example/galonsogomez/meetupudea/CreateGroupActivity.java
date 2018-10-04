package com.example.galonsogomez.meetupudea;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateGroupActivity extends AppCompatActivity {

    // Views
    private TextView textMeetUpUdeA;
    private EditText textTitleGroup;
    private EditText textDescriptionGroup;
    private ImageView imgPictureGroup;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgGroupUri;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        // To assigne the views
        castingViews();
        initFirebase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imgGroupUri = data.getData();
            imgPictureGroup.setImageURI(imgGroupUri);
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_Add_Picture:
                openFileChooser();
                break;

            case R.id.btn_CreateGroup:

                uploadGroup();
                break;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    // create extension to the image slected
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void castingViews(){
        textMeetUpUdeA = (TextView) findViewById(R.id.text_MeetUpUdeA);
        textTitleGroup = (EditText) findViewById(R.id.text_Title_Group);
        textDescriptionGroup = (EditText)findViewById(R.id.text_Description_Group);
        imgPictureGroup = (ImageView)findViewById(R.id.img_Picture_Group);
    }

    private void initFirebase(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference("ImageGroup");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("groups");
    }

    private void uploadGroup(){
        initFirebase();
        if(checkFields() && imgGroupUri!=null) {

                final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() +
                        "." + getFileExtension(imgGroupUri));


                // to can obtain Url from storage
                UploadTask uploadTask = fileReference.putFile(imgGroupUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String linkPicture = downloadUri.toString();
                            // Create object group
                            Group group = new Group(UUID.randomUUID().toString(), firebaseUser.getUid(),
                                    textTitleGroup.getText().toString(), linkPicture,
                                    textDescriptionGroup.getText().toString());
                            // Add child
                            mDatabaseReference.child(group.getGroupUID()).setValue(group);
                            Toast.makeText(getApplicationContext(), "Grupo creado", Toast.LENGTH_SHORT).show();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                            mDatabaseReference.child("users").child(firebaseUser.getUid()).child("myGroups")
                                    .push()
                                    .setValue(group.getGroupUID());

                            cleanFields();
                            Log.d("createGroup", linkPicture);
                        }
                    }
                });
            } else {
            Toast.makeText(getApplicationContext(),"Existen campos vacíos",Toast.LENGTH_SHORT).show();
            }

    }

    private boolean checkFields(){
        if(textTitleGroup.getText().toString().equals("") ||textDescriptionGroup.getText().toString().equals("")){
            //Toast.makeText(getApplicationContext(),"Existen campos vacíos",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Log.d("checkFields", "checkFields: Crear grupo");
            return true;
        }
    }

    private void cleanFields(){
        textTitleGroup.setText("");
        textDescriptionGroup.setText("");
        imgPictureGroup.setImageDrawable(null);
        imgGroupUri = null;
    }

}

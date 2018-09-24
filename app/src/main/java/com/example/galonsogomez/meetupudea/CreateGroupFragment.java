package com.example.galonsogomez.meetupudea;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends Fragment implements View.OnClickListener{

    // Views
    private TextView textMeetUpUdeA;
    private EditText textTitleGroup;
    private EditText textDescriptionGroup;
    private ImageView imgPictureGroup;
    private Button btnAddPicture;
    private Button btnCreateGroup;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgGroupUri;
    private Typeface andora;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseUser firebaseUser;


    public CreateGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        // To assigne the views
        castingViews(view);

        // change the type of the text
        String fuenteMUdeA = "fonts/Andora.ttf";
        this.andora = Typeface.createFromAsset(getContext().getAssets(),fuenteMUdeA);
        textMeetUpUdeA.setTypeface(andora);

        initFirebase();
        return view;

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

    private void initFirebase(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(this.getContext());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference("ImageGroup");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("groups");
    }

    // create extension to the image slected
    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadGroup(){

        if(imgGroupUri!=null){
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()+
            "." + getFileExtension(imgGroupUri));


            // to can obtain Url from storage
            UploadTask uploadTask = fileReference.putFile(imgGroupUri);
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
                        // Create object group
                        Group group = new Group(UUID.randomUUID().toString(),firebaseUser.getUid(),
                            textTitleGroup.getText().toString(),linkPicture,
                            textDescriptionGroup.getText().toString());
                        // Add child
                        mDatabaseReference.child(group.getGroupUID()).setValue(group);
                        Toast.makeText(getContext(),"Grupo creado",Toast.LENGTH_SHORT).show();
                        Log.d("createGroup", linkPicture);
                    }
                }
            });
        }
        else{
            Toast.makeText(getContext(),"Imagen no seleccionada",Toast.LENGTH_SHORT).show();
        }
    }


    private void castingViews(View view){
        textMeetUpUdeA = (TextView) view.findViewById(R.id.text_MeetUpUdeA);
        textTitleGroup = (EditText) view.findViewById(R.id.text_Title_Group);
        textDescriptionGroup = (EditText) view.findViewById(R.id.text_Description_Group);
        imgPictureGroup = (ImageView) view.findViewById(R.id.img_Picture_Group);
        btnAddPicture = (Button)view.findViewById(R.id.btn_Add_Picture);
        btnAddPicture.setOnClickListener(this);
        btnCreateGroup = (Button)view.findViewById(R.id.btn_CreateGroup);
        btnCreateGroup.setOnClickListener(this);

    }



}

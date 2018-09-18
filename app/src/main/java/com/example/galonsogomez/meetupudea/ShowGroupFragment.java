package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGroupFragment extends Fragment {

    TextView textView;
    ImageView groupPicture;
    FloatingActionButton floatingActionButton;
    String uidGroup;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public ShowGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*String data = getArguments().getString("title");
        Log.d("frag", data);*/
        //Obtener datos de la activity
        Bundle b = getActivity().getIntent().getExtras();


        //Set the data
       View view = inflater.inflate(R.layout.fragment_show_group, container, false);
       setTitle(b.getString("title"),view);
       setPicture(b.getString("picture"),getActivity().getApplicationContext(),view);

       //Set uidGroup from Bundle
       uidGroup = b.getString("UID");

       //Agregar idGroup a firebase en users
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Siguiendo",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                //floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.heart));

                /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("idCurrentUser",firebaseUser.getUid());*/

                setFollowing(uidGroup);

            }
        });


       return view;
    }

    public void setFollowing(String uid)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new  User(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child("userUID").child("following").setValue(uid);
        //mDatabaseReference.child("users").setValue(user);
    }

    public void setTitle(String title, View view)
    {
        textView =(TextView) view.findViewById(R.id.text_Group_Fragment);
        textView.setText(title);
    }

   public void setPicture(String picture, Context c, View view) {
        groupPicture = (ImageView) view.findViewById(R.id.img_Group_Fragment);
        Picasso.with(c).load(picture)
                .fit()
                .centerCrop()
                .into(groupPicture);
    }


}

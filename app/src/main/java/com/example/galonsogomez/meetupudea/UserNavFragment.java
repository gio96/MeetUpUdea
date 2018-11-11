package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserNavFragment extends Fragment implements View.OnClickListener{

    // Views
    private TextView txtUserName;
    private Button btnLogOut;
    private Button btnCreate;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private RecyclerView recyclerViewUser;

    //Firebase
    private DatabaseReference mReferenceMyGroups;
    private DatabaseReference mReference;
    private FirebaseDatabase mFirebaseDatabase;


    public UserNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nav, container, false);

        recyclerViewUser = (RecyclerView) view.findViewById(R.id.recycler_View_Fragment_User);
        recyclerViewUser.setHasFixedSize(true);

        //this change to getActivity() /problem
        recyclerViewUser.setLayoutManager(new GridLayoutManager(getActivity(),2));

        btnLogOut = view.findViewById(R.id.btn_Logout);
        btnLogOut.setOnClickListener(this);
        btnCreate = view.findViewById(R.id.btn_Create);
        btnCreate.setOnClickListener(this);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setData(view,firebaseUser);
        setPicture(view,getActivity().getApplicationContext(),firebaseUser);

        return view;
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }

    public void setData(View view,FirebaseUser user){
        txtUserName = view.findViewById(R.id.txt_User_Name);
        String name = user.getDisplayName();
        txtUserName.setText(name);
    }

    public void setPicture(View view ,Context c,FirebaseUser user) {
        circleImageView = view.findViewById(R.id.avatar);
        Picasso.with(c).load(user.getPhotoUrl())
                .fit()
                .centerCrop()
                .into(circleImageView);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_Logout:
                signOut();
                break;
            case R.id.btn_Create:

                Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
                startActivity(intent);
                break;

        }
    }
}

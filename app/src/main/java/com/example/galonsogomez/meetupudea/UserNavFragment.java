package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserNavFragment extends Fragment implements View.OnClickListener{

    // Views
    private TextView txtUserName;
    private Button btnLogOut;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;

    public UserNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nav, container, false);
        btnLogOut = view.findViewById(R.id.btn_Logout);
        btnLogOut.setOnClickListener(this);


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
                Fragment createGroup = new CreateGroupFragment();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,createGroup).commit();
                //signOut();
                break;
        }
    }
}

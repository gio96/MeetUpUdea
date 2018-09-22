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
public class UserNavFragment extends Fragment {

    TextView textView;
    Button button;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    public UserNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nav, container, false);
        button = view.findViewById(R.id.btn_Logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setData(view,firebaseUser);
        setPicture(view,getActivity().getApplicationContext(),firebaseUser);

        return view;
    }

    public void signOut(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }

    public void setData(View view,FirebaseUser user)
    {
        textView = view.findViewById(R.id.txt_User_Frag);
        String name=user.getDisplayName();
        textView.setText(name);
    }

    public void setPicture(View view ,Context c,FirebaseUser user) {
        circleImageView = view.findViewById(R.id.avatar);
        Picasso.with(c).load(user.getPhotoUrl())
                .fit()
                .centerCrop()
                .into(circleImageView);
    }

}

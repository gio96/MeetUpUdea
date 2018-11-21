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
import android.view.Menu;
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

    //recyclerV_Events_Nav

    private RecyclerView recyclerVUsermyGroups;

    //Firebase
    private DatabaseReference mReferenceMyGroups;
    private DatabaseReference mReference;
    private FirebaseDatabase mFirebaseDatabase;

    public UserNavFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nav, container, false);
         //MyGroups
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Query myGroupsQuery = FirebaseDatabase.getInstance().getReference().child("users")
                .child(firebaseUser.getUid()).child("myGroups");
        Log.d("query", myGroupsQuery.toString());
        recyclerVUsermyGroups = (RecyclerView) view.findViewById(R.id.recycler_View_Fragment_User);
        mReference = myGroupsQuery.getRef();
        mReference.keepSynced(true);

        recyclerVUsermyGroups.setHasFixedSize(true);
        recyclerVUsermyGroups.setLayoutManager(new GridLayoutManager(getActivity(),2));



        btnLogOut = view.findViewById(R.id.btn_Logout);
        btnLogOut.setOnClickListener(this);
        btnCreate = view.findViewById(R.id.btn_Create);
        btnCreate.setOnClickListener(this);



        setData(view,firebaseUser);
        setPicture(view,getActivity().getApplicationContext(),firebaseUser);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Create firebaseRecyclerAdapter with a viewHolder to show the info of the
        // Database

        FirebaseRecyclerAdapter<Group,HomeNavFragment.GroupViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Group,HomeNavFragment.GroupViewHolder>
                        (Group.class,R.layout.item_group,HomeNavFragment.GroupViewHolder.class,mReference) {

                    @Override
                    public  void populateViewHolder(HomeNavFragment.GroupViewHolder groupViewHolder,
                                                    final Group model , int position){

                        // To assign the info from Database to cardView
                        //groupViewHolder.setIdGroup(model.getGroupUID());
                        Log.d("model", model.getTitle());
                        groupViewHolder.setTitle(model.getTitle());
                        groupViewHolder.setIcon();
                        groupViewHolder.setPicture(model.getPicture(),getActivity().getApplicationContext());
                        groupViewHolder.cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Show info of the group to edit
                                Bundle bundleGroup = sendData(model);
                                Intent intent = new Intent(getActivity(),ShowGroupActivity.class);
                                intent.putExtras(bundleGroup);
                                startActivity(intent);
                            }
                        });

                    }
                };

        recyclerVUsermyGroups.setAdapter(firebaseRecyclerAdapter);
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

    public Bundle sendData(Group group){
        Bundle bundle = new Bundle();
        bundle.putString("UID",group.getGroupUID());
        bundle.putString("title",group.getTitle());
        bundle.putString("picture",group.getPicture());
        bundle.putString("description",group.getDescription());
        return bundle;
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

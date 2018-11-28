package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetUpNavFragment extends Fragment {

    // Views
    private RecyclerView recyclerVGroups;
    private TextView message;

    //Firebase
    private DatabaseReference mreference;

    public MeetUpNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meet_up_nav, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //call the Database just the first 10 groups
        Query groups = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("following");
        recyclerVGroups = (RecyclerView) view.findViewById(R.id.recyclerV_Group);
        message = (TextView) view.findViewById(R.id.text_Message_MeetUp);
        mreference = groups.getRef();
        mreference.keepSynced(true);
        groups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    message.setVisibility(TextView.GONE);
                    recyclerVGroups.setHasFixedSize(true);
                    recyclerVGroups.setLayoutManager(new GridLayoutManager(getActivity(),2));
                }else{
                    message.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Create firebaseRecyclerAdapter with a viewHolder to show the info of the
        // Database

        FirebaseRecyclerAdapter<Group,MeetUpNavFragment.GroupViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Group,MeetUpNavFragment.GroupViewHolder>
                        (Group.class,R.layout.item_group_meetup,MeetUpNavFragment.GroupViewHolder.class,mreference) {

                    @Override
                    public  void populateViewHolder(MeetUpNavFragment.GroupViewHolder groupViewHolder,
                                                    final Group model , int position) {
                        // To assign the info from Database to cardView
                        if (model.isNotification()) {
                            groupViewHolder.setNotification();
                        }


                        groupViewHolder.setTitle(model.getTitle());

                        groupViewHolder.setPicture(model.getPicture(), getActivity().getApplicationContext());
                        groupViewHolder.cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Bundle bundleGroup = sendData(model);
                                Intent intent = new Intent(getActivity(), ShowGroupActivity.class);
                                intent.putExtras(bundleGroup);
                                startActivity(intent);
                            }
                        });

                    }
                };

        recyclerVGroups.setAdapter(firebaseRecyclerAdapter);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        View mview;
        TextView groupTitle;
        ImageView groupPicture;
        ImageView groupNotification;
        CardView cardViewEvent1 = (CardView) itemView.findViewById(R.id.cv_Meetup);
        public  GroupViewHolder(View itemView){
            super(itemView);
            mview = itemView;

        }

        public void setTitle(String title){
            groupTitle = (TextView) itemView.findViewById(R.id.text_Group);
            groupTitle.setText(title);

        }

        public void setPicture(String picture, Context c) {
            groupPicture = (ImageView) itemView.findViewById(R.id.img_Group);
            Picasso.with(c).load(picture)
                    .fit()
                    .centerCrop()
                    .into(groupPicture);
        }

        public  void setNotification(){
            groupNotification = (ImageView) itemView.findViewById(R.id.img_Notification);
            groupNotification.setVisibility(View.VISIBLE);
        }
    }

    public Bundle sendData(Group group){
        Bundle bundle = new Bundle();
        bundle.putString("UID",group.getGroupUID());
        bundle.putString("title",group.getTitle());
        bundle.putString("picture",group.getPicture());
        bundle.putString("description",group.getDescription());
        bundle.putBoolean("notification", group.isNotification());
        return bundle;
    }

}

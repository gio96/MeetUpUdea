package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeNavFragment extends Fragment {
    
    // Views
    private RecyclerView recyclerVGroups;

    //Firebase
    private DatabaseReference mreference;

    public HomeNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_nav, container, false);


        //call the Database just the first 10 groups
        Query groups = FirebaseDatabase.getInstance().getReference().child("groups").limitToFirst(10);
        recyclerVGroups = (RecyclerView) view.findViewById(R.id.recyclerV_Group);
        mreference = groups.getRef();
        mreference.keepSynced(true);


        recyclerVGroups.setHasFixedSize(true);
        recyclerVGroups.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Create firebaseRecyclerAdapter with a viewHolder to show the info of the
        // Database

        FirebaseRecyclerAdapter<Group,GroupViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Group,GroupViewHolder>
                        (Group.class,R.layout.item_group,GroupViewHolder.class,mreference) {

                    @Override
                    public  void populateViewHolder(GroupViewHolder groupViewHolder,
                                                    final Group model , int position){

                        // To assign the info from Database to cardView
                        //groupViewHolder.setIdGroup(model.getGroupUID());
                        groupViewHolder.setTitle(model.getTitle());

                        groupViewHolder.setPicture(model.getPicture(),getActivity().getApplicationContext());
                        groupViewHolder.cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Bundle bundleGroup = sendData(model);
                                Intent intent = new Intent(getActivity(),ShowGroupActivity.class);
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
        CardView cardViewEvent1 = (CardView) itemView.findViewById(R.id.cv_Group);
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
    }

    /* public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }*/

    public Bundle sendData(Group group){
        Bundle bundle = new Bundle();
        bundle.putString("UID",group.getGroupUID());
        bundle.putString("title",group.getTitle());
        bundle.putString("picture",group.getPicture());
        bundle.putString("description",group.getDescription());
        return bundle;
    }
}

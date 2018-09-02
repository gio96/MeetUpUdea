package com.example.galonsogomez.meetupudea;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button logOutButton;
    private List<Group> lstGroup;

    private RecyclerView myrv;

    private DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        lstGroup = new ArrayList<>();
        /*
        lstGroup.add(new Group("Meetup",R.drawable.meetup));
        lstGroup.add(new Group("Prueba2",R.drawable.prueba2));
        lstGroup.add(new Group("Prueba3",R.drawable.prueba3));
        lstGroup.add(new Group("Prueba4",R.drawable.prueba4));
        lstGroup.add(new Group("Prueba5",R.drawable.prueba5));
        lstGroup.add(new Group("Prueba6",R.drawable.prueba6));
        lstGroup.add(new Group("Prueba3",R.drawable.prueba3));
        lstGroup.add(new Group("Prueba4",R.drawable.prueba4));
        lstGroup.add(new Group("Prueba5",R.drawable.prueba5));
        lstGroup.add(new Group("Prueba6",R.drawable.prueba6));*/

        //call the Database groups

        //just the first 10
        Query groups = FirebaseDatabase.getInstance().getReference().child("groups").limitToFirst(10);
        mreference = groups.getRef();
        mreference.keepSynced(true);

        myrv = (RecyclerView) findViewById(R.id.recycler_View);
        myrv.setHasFixedSize(true);

        //RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lstGroup);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        //myrv.setAdapter(myAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Create firebaseRecyclerAdapter with a viewHolder to show the info of the
        // Database

        FirebaseRecyclerAdapter<Group,GroupViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Group,GroupViewHolder>
                (Group.class,R.layout.item_group,GroupViewHolder.class,mreference) {

            @Override
            public  void populateViewHolder(GroupViewHolder groupViewHolder,Group model ,int position){

                // To assign the info from Database to cardView
                groupViewHolder.setTitle(model.getTitle());
                groupViewHolder.setPicture(model.getPicture(),getApplicationContext());
            }
        };
        myrv.setAdapter(firebaseRecyclerAdapter);
    }


    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public  GroupViewHolder(View itemView){
            super(itemView);
            mview = itemView;
        }


        public void setTitle(String title){
            TextView groupTitle = (TextView) itemView.findViewById(R.id.text_Group);
            groupTitle.setText(title);

        }

        public void setPicture(String picture, Context c) {
            ImageView groupPicture = (ImageView) itemView.findViewById(R.id.img_Group);
            Picasso.with(c).load(picture)
                    .fit()
                    .centerCrop()
                    .into(groupPicture);
        }

    }



    /* public void onClick(View v){

        switch (v.getId()){
            case R.id.btn_SignOut:
                signOut();
                break;
        }
    }*/

    public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

package com.example.galonsogomez.meetupudea;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button logOutButton;
    private RecyclerView myrv;
    private DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //call the Database groups

        //just the first 10
        Query groups = FirebaseDatabase.getInstance().getReference().child("groups").limitToFirst(10);
        mreference = groups.getRef();
        mreference.keepSynced(true);

        myrv = (RecyclerView) findViewById(R.id.recycler_View);
        myrv.setHasFixedSize(true);

        myrv.setLayoutManager(new GridLayoutManager(this,2));
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
            public  void populateViewHolder(GroupViewHolder groupViewHolder, final Group model , int position){

                // To assign the info from Database to cardView
                //groupViewHolder.setIdGroup(model.getGroupUID());
                groupViewHolder.setTitle(model.getTitle());
                groupViewHolder.setPicture(model.getPicture(),getApplicationContext());
                groupViewHolder.cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(view.getContext(),model.getGroupUID(), Toast.LENGTH_SHORT).show();

                        //mandar datos a la activity
                        Bundle bundle = new Bundle();
                        bundle.putString("UID",model.getGroupUID());
                        bundle.putString("title",model.getTitle());
                        bundle.putString("picture",model.getPicture());
                        bundle.putString("description",model.getDescription());

                        //Log.d("bulde", bundle.toString());
                        //Log.d("idcard",model.getGroupUID());

                        Intent intent = new Intent(HomeActivity.this,ShowGroupActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });

            }
        };

        myrv.setAdapter(firebaseRecyclerAdapter);
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

    /*public void onClick(View v){

        switch (v.getId()){
            case R.id.btn_SignOut:
                signOut();
                break;
        }
    }*/

   /* public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }*/
}

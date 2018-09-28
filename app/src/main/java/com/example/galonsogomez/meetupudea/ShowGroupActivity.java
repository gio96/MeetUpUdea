package com.example.galonsogomez.meetupudea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowGroupActivity extends AppCompatActivity {

    // Views
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageGroup;
    private TextView textTitleGroup;
    private FloatingActionButton floatingActionButton;

    private String uidGroup;
    boolean following = false;
    boolean isOwn = false;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        //Get data from each group from HomeNavFragment
        Bundle b = getIntent().getExtras();
        ShowGroupInforFragment showGroupInforFragment = new ShowGroupInforFragment();
        //Send data to fragments
        showGroupInforFragment.setArguments(b);


        //Set Data
        setTitle(b.getString("title"));
        setPicture(b.getString("picture"),getApplicationContext());
        //Set uidGroup from Bundle
        uidGroup = b.getString("UID");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        final OvershootInterpolator interpolator = new OvershootInterpolator();

        esMio(uidGroup);

        if(!isOwn){

            //Through the whole section of "following" in Firebase
            //---------------------------------------------------------------------------------------------------------
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users")
                    .child(firebaseUser.getUid()).child("following");
            mDatabaseReferenceFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Group group = new Group();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        group = ds.getValue(Group.class);
                        if(uidGroup.equals(group.getGroupUID())){

                            //Variable to handle the follow and unfollow
                            following = true;
                            floatingActionButton.setImageDrawable(ContextCompat
                                    .getDrawable(getApplicationContext(),R.drawable.heart));
                        }else{
                            //---------------
                            //following = false;
                            Log.d("unfollow", "No sigue al grupo");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //following/unfollowing/create Event
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOwn){
                    //Call the Activity to create Events and send data
                    /*Bundle bundle = new Bundle();
                    bundle.putString("groupUID",uidGroup);*/

                    Intent intent = new Intent(ShowGroupActivity.this,CreateEventActivity.class);
                    intent.putExtra("groupUID",uidGroup);
                    startActivity(intent);

                    Log.d("isOWn", "CREANDO LA ACTIVIDAD");
                }else{
                    if(following){

                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                                ,R.drawable.heart_outline));
                        Snackbar.make(view, "Dejar de seguir",Snackbar.LENGTH_LONG)
                                .setAction("Action",null).show();

//---------------------------------------------------------------------------------------------------------------------------
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users")
                                .child(firebaseUser.getUid()).child("following");

                        mDatabaseReferenceFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Group groupDelete = new Group();
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    groupDelete = ds.getValue(Group.class);
                                    if(uidGroup.equals(groupDelete.getGroupUID())){
                                        ds.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        following =false;

                    }else{
                        Snackbar.make(view, "Siguiendo",Snackbar.LENGTH_LONG)
                                .setAction("Action",null).show();
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.heart));
                        setFollowing(uidGroup);
                    }

                }
            }
        });
        createTabs();

    }

    public void esMio(String groupUiD){


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users").child(firebaseUser.getUid()).child("following");


        DatabaseReference mDatabaseReferenceGroup = mFirebaseDatabase.getReference().child("groups").child(groupUiD);

        mDatabaseReferenceGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uidUser= dataSnapshot.getValue(User.class).getUserUID();
                if(uidUser.equals(firebaseUser.getUid())){
                    Log.d("mani","el grupo es mio");
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_add_black_24dp));

                    //The group is mine
                    isOwn = true;

                }else {
                    Log.d("mani","el grupo NO es mio");
                    isOwn = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("mani","No hay ningun datos");
            }
        });
    }

    public void setFollowing(String uidGroup)
    {
        Group group = new Group(uidGroup);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new  User(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child(firebaseUser.getUid()).child("following").push().setValue(group);
    }

    public void setTitle(String title){

        textTitleGroup = (TextView) findViewById(R.id.text_Title_Group);
        textTitleGroup.setText(title);
    }

    public void setPicture(String picture, Context c) {
        imageGroup = (ImageView) findViewById(R.id.img_Group);
        Picasso.with(c).load(picture)
                .fit()
                .centerCrop()
                .into(imageGroup);
    }

    public void createTabs(){
        //Tabs
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //add Fragments
        adapter.addFragment(new ShowGroupEventsFragment(), "Upcoming");
        adapter.addFragment(new ShowGroupInforFragment(), "Information");
        adapter.addFragment(new ShowGroupCoursesFragment(), "Courses");
        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

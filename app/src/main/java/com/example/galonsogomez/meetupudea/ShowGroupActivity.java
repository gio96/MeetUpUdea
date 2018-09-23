package com.example.galonsogomez.meetupudea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

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
        createTabs();

        firebaseDb();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = new Group();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                   group = ds.getValue(Group.class);
                   //Log.d("grupoD",group.getGroupUID());

                   //Verify if the user following the user and fill the heart
                   if(uidGroup.equals(group.getGroupUID())){

                       //Variable to handle the follow and unfollow
                        following = true;
                       floatingActionButton.setImageDrawable(ContextCompat
                               .getDrawable(getApplicationContext(),R.drawable.heart));
                   }else{
                       Log.d("unfollow", "No sigue al grupo");
                   }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("follow", "Data not found");
            }
        });



        //following/unfollowing
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(following){

                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.heart_outline));
                    Snackbar.make(view, "Dejar de seguir",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    firebaseDb();
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });
    }


    public void setFollowing(String uid)
    {
        Group group = new Group(uid);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new  User(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
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

    public void firebaseDb(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users")
                .child(firebaseUser.getUid()).child("following");
    }

}

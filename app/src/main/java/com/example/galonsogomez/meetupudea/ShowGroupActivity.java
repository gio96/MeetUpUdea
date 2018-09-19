package com.example.galonsogomez.meetupudea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShowGroupActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;
    private TextView textView;
    private FloatingActionButton floatingActionButton;
    String uidGroup;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        //Recibo los datos enviados desde home
        Bundle b = getIntent().getExtras();

        //Set Data
        setTitle(b.getString("title"));
        setPicture(b.getString("picture"),getApplicationContext());

        //Set uidGroup from Bundle
        uidGroup = b.getString("UID");

        //Agregar idGroup a firebase en users
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Siguiendo",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                //floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.heart));

                /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("idCurrentUser",firebaseUser.getUid());*/

                setFollowing(uidGroup);
            }
        });


        ShowGroupInforFragment showGroupInforFragment = new ShowGroupInforFragment();

        //Se le envian los datos a los fragments
        showGroupInforFragment.setArguments(b);

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

    public void setFollowing(String uid)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new  User(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child(firebaseUser.getUid()).child("following").setValue(uid);
        //mDatabaseReference.child("users").setValue(user);
    }

    public void setTitle(String title)
    {
        textView = (TextView) findViewById(R.id.textView_Title_Group);
        textView.setText(title);
    }

    public void setPicture(String picture, Context c) {
        imageView = (ImageView) findViewById(R.id.imageView__Group);
        Picasso.with(c).load(picture)
                .fit()
                .centerCrop()
                .into(imageView);
    }

}

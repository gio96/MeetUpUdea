package com.example.galonsogomez.meetupudea;

import android.animation.Animator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ShowGroupActivity extends AppCompatActivity {

    // Views
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageGroup;
    private TextView textTitleGroup;
    private FloatingActionButton floatingActionButton;

    private String uidGroup;
    private String titleGroup;
    private boolean notification = false;

    boolean following = false;
    boolean isOwn = false;
    Bundle bundle = new Bundle();

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        //Get data from each group from HomeNavFragment
        bundle = getIntent().getExtras();
        ShowGroupInforFragment showGroupInforFragment = new ShowGroupInforFragment();
        ShowGroupEventsFragment showGroupEventsFragment = new ShowGroupEventsFragment();
        //Send data to fragments
        showGroupInforFragment.setArguments(bundle);
        showGroupEventsFragment.setArguments(bundle);

        //Set Data
        notification = bundle.getBoolean("notification");
        Log.d("intefr", String.valueOf(notification));
        titleGroup = bundle.getString("title");
        setTitle(bundle.getString("title"));
        setPicture(bundle.getString("picture"), getApplicationContext());
        //Set uidGroup from Bundle
        uidGroup = bundle.getString("UID");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        isMyGroup(uidGroup);

        if (!isOwn) {

            //Through the whole section of "following" in Firebase
            //---------------------------------------------------------------------------------------------------------
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users")
                    .child(firebaseUser.getUid()).child("following");
            mDatabaseReferenceFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Group group = new Group();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        group = ds.getValue(Group.class);
                        if (uidGroup.equals(group.getGroupUID())) {

                            //Variable to handle the follow and unfollow
                            following = true;
                            floatingActionButton.setImageDrawable(ContextCompat
                                    .getDrawable(getApplicationContext(), R.drawable.heart));

                            //Notification module
                            if(notification){
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                FirebaseDatabase mFirebaseDatabaseNotification = FirebaseDatabase.getInstance();
                                DatabaseReference mDatabaseRefNotification = mFirebaseDatabaseNotification.getReference();
                                mDatabaseRefNotification.child("users").child(firebaseUser.getUid()).child("following")
                                        .child(uidGroup).child("notification").setValue(false);
                            }


                        } else {
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
                if (isOwn) {
                    //Call the Activity to create Events and send data
                    /*Bundle bundle = new Bundle();
                    bundle.putString("groupUID",uidGroup);*/

                    //Intent intent = new Intent(ShowGroupActivity.this,CreateEventActivity.class);
                    Intent intent = new Intent(ShowGroupActivity.this, CreateGroupElementsActivity.class);
                    intent.putExtra("groupUID", uidGroup);
                    startActivity(intent);

                    Log.d("isOWn", "CREANDO LA ACTIVIDAD");
                } else {
                    if (following) {

                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                                , R.drawable.heart_outline));
                        Snackbar.make(view, "Dejar de seguir", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

//---------------------------------------------------------------------------------------------------------------------------
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users")
                                .child(firebaseUser.getUid()).child("following");

                        mDatabaseReferenceFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Group groupDelete = new Group();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    groupDelete = ds.getValue(Group.class);
                                    if (uidGroup.equals(groupDelete.getGroupUID())) {
                                        ds.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        following = false;

                    } else {
                        Snackbar.make(view, "Siguiendo", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.heart));
                        setFollowing(bundle);
                    }

                }
            }
        });
        createTabs();

    }

    public void isMyGroup(String groupUiD) {


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users").child(firebaseUser.getUid()).child("following");

        DatabaseReference mDatabaseReferenceGroup = mFirebaseDatabase.getReference().child("groups").child(groupUiD);

        mDatabaseReferenceGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uidUser = dataSnapshot.getValue(User.class).getUserUID();
                if (uidUser.equals(firebaseUser.getUid())) {
                    Log.d("mani", "el grupo es mio");
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_add_black_24dp));
                    //The group is mine
                    isOwn = true;
                } else {
                    Log.d("mani", "el grupo NO es mio");
                    isOwn = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("mani", "No hay ningun datos");
            }
        });
    }

    //esteeeeee es el que hay que corregir
    public void setFollowing(Bundle groupData) {
        String idGroup = groupData.getString("UID");
        Group group = new Group(idGroup, groupData.getString("title"), groupData.getString("picture"), groupData.getString("description"));
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new  User(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        //mDatabaseReference.child("users").child(firebaseUser.getUid()).child("following").push().setValue(group);
        mDatabaseReference.child("users").child(firebaseUser.getUid()).child("following").child(uidGroup).setValue(group);
    }

    public void setTitle(String title) {

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

    public void createTabs() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (!isOwn) {
            menu.setGroupVisible(R.menu.menu, false);
            return false;
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDelete();
                return true;

            /*case R.id.action_block:
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void deleteGroup(){
        deleteAttend();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference mDatabaseReferenceFollowing = mFirebaseDatabase.getReference().child("users").child(firebaseUser.getUid()).child("following");


        // Delete from table groups
        DatabaseReference mDatabaseReferenceGroup = mFirebaseDatabase.getReference().child("groups").child(uidGroup);
        mDatabaseReferenceGroup.removeValue();


        // Delete from myGroups
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReferenceGroup = mFirebaseDatabase.getReference().child("users").child(firebaseUser.getUid())
                .child("myGroups").child(uidGroup);
        Log.d("cosita", mDatabaseReferenceGroup.toString());
        mDatabaseReferenceGroup.removeValue();
        //finish();
        Log.d("borrar", "deleteGroup: borrado");
        //Toast.makeText(getApplicationContext(), "Se eliminó", Toast.LENGTH_SHORT).show();

        // Delete from Following

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReferenceGroup = mFirebaseDatabase.getReference().child("users");
        mDatabaseReferenceGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // String uidUser = dataSnapshot.getValue(User.class).getUserUID();
                User user = new User();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //user = ds.getValue(User.class);;
                    //Log.d("delete", ds.getKey());
                    /*String getGroup = ds.child("following").getValue(Group.class).getGroupUID();

                    if(getGroup==uidGroup){

                    }*/
                    DatabaseReference mDatabaseReferenceDelete = mFirebaseDatabase.getReference().child("users")
                            .child(ds.getKey()).child("following").child(uidGroup);
                    mDatabaseReferenceDelete.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("canceled", "No hay ningun datos");
            }
        });
    }

    public void deleteAttend(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReferenceAttend = mFirebaseDatabase.getReference().child("groups").child(uidGroup)
        .child("events");
        mDatabaseReferenceAttend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String eventAttend = ds.getKey();

                    final FirebaseDatabase mFirebaseDatabaseAttend = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabaseReferenceAttendUser = mFirebaseDatabaseAttend
                            .getReference().child("users");

                    mDatabaseReferenceAttendUser
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dsu : dataSnapshot.getChildren()) {
                                String userId = dsu.getKey();

                                DatabaseReference mDatabaseReferenceDelete = mFirebaseDatabaseAttend
                                        .getReference().child("users")
                                        .child(userId).child("attend").child(eventAttend);

                                mDatabaseReferenceDelete.removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("mani", "No hay ningun datos");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("mani", "No hay ningun datos");
            }
        });
    }

    public void showAlertDelete(){
        new AlertDialog.Builder(ShowGroupActivity.this)
                .setTitle("Eliminar grupo")
                .setMessage(("¿Desea eliminar el grupo " + titleGroup +" ?"))
                .setIcon(R.drawable.ic_delete_forever_black_24dp)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                deleteGroup();
                               Intent intent = new Intent(ShowGroupActivity.this, BottomNavActivity.class);
                                startActivity(intent);
                                dialog.cancel();

                            }
                        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}

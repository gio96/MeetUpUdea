package com.example.galonsogomez.meetupudea;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowEventActivity extends AppCompatActivity {

    TextView eventTitle,eventPlace, eventStartHour,eventFinishHour,eventDate,eventFinishDate,eventDescription;
    ImageView eventPicture;
    private FloatingActionButton floatingActionButtonAttendEvent;
    private String eventUID;
    private boolean attend = false;
    Bundle bundle;
    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        //data sent from ShowGroupEventsFragment or EventsNavFragment
        bundle = getIntent().getExtras();

        //Set Data
        setDataEvent(bundle);
        setPictureEvent(bundle.getString("pictureEvent"));
        //Log.d("pic",bundle.getString("pictureEvent"));

        //Id event
        eventUID = bundle.getString("uidEvent");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReferenceAttend = mFirebaseDatabase.getReference().child("users")
                .child(firebaseUser.getUid()).child("attend");
        mDatabaseReferenceAttend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*Object idEvent = dataSnapshot.getValue(Event.class).getEventUID();
                Event event = new Event();*/
                if(dataSnapshot.hasChild(eventUID))
                {
                    floatingActionButtonAttendEvent.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.thumb_up));
                    Log.d("manito", "Relleno");
                    attend = true;
                }else{
                    floatingActionButtonAttendEvent.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.thumb_up_outline));
                    Log.d("manito", "Vacio");
                    attend = false;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        floatingActionButtonAttendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attend){
                    floatingActionButtonAttendEvent.setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.thumb_up_outline));
                    Toast.makeText(getApplicationContext(),"Dejar de Seguir",Toast.LENGTH_SHORT).show();

                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabaseReferenceAttend = mFirebaseDatabase.getReference().child("users")
                            .child(firebaseUser.getUid()).child("attend").child(eventUID);

                    mDatabaseReferenceAttend.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Event eventDelete = new Event();
                            /*for(DataSnapshot ds : dataSnapshot.getChildren()){
                                eventDelete = ds.getValue(Event.class);
                                if(eventUID.equals(eventDelete.getEventUID())){
                                    ds.getRef().removeValue();
                                }
                            }*/
                            Log.d("celu",dataSnapshot.getRef().toString());
                            dataSnapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    attend = false;
                }else{
                    setAttend(bundle);
                    Toast.makeText(getApplicationContext(),"Siguiendo",Toast.LENGTH_SHORT).show();
                    floatingActionButtonAttendEvent.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.thumb_up));
                    attend = true;

                }
                //Toast.makeText(getApplicationContext(),"Me clickeo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAttend(Bundle b)
    {
        Event event = new Event(b.getString("uidEvent"),b.getString("titleEvent"),b.getString("placeEvent"),b.getString("dateEvent")
                ,b.getString("startHourEvent"),b.getString("finishHourEvent"),b.getString("pictureEvent"),b.getString("descriptionEvent"));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child(firebaseUser.getUid()).child("attend").child(b.getString("uidEvent")).setValue(event);

    }

    public void setDataEvent(Bundle b){
        eventTitle = (TextView) findViewById(R.id.text_Title_Show_Event);
        eventTitle.setText(b.getString("titleEvent"));

        eventPlace = (TextView) findViewById(R.id.text_Place_Show_Event);
        eventPlace.setText(b.getString("placeEvent"));

        eventStartHour = (TextView) findViewById(R.id.text_Start_Hour_Show_Event);
        eventStartHour.setText(b.getString("startHourEvent"));

        eventFinishHour = (TextView) findViewById(R.id.text_Finish_Hour_Show_Event);
        eventFinishHour.setText(b.getString("finishHourEvent"));

        eventDate = (TextView) findViewById(R.id.text_Date_Show_Event);
        eventDate.setText(b.getString("dateEvent"));

        eventFinishDate = (TextView) findViewById(R.id.text_Finish_Date_Show_Event);
        eventFinishDate.setText(b.getString("dateEvent"));

        eventDescription = (TextView) findViewById(R.id.text_Description_Show_Event);
        eventDescription.setText(b.getString("descriptionEvent"));

        floatingActionButtonAttendEvent = (FloatingActionButton) findViewById(R.id.floatingActionButton_Attend_Event);

    }

    public void setPictureEvent(String picture) {
        eventPicture = (ImageView) findViewById(R.id.img_Show_Event);
        //eventPicture.setImageResource(R.drawable.udea);
        Picasso.with(getApplicationContext()).load(picture)
                .fit().centerInside()
                .into(eventPicture);
    }
}

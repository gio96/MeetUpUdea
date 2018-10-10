package com.example.galonsogomez.meetupudea;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShowEventActivity extends AppCompatActivity {

    TextView eventTitle,eventPlace,eventHour,eventDate,eventDescription;
    ImageView eventPicture;
    String idEvent = "";
    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        //data sent from ShowGroupEventsFragment
        Bundle bundle = getIntent().getExtras();

        //Set Data
        idEvent= bundle.getString("uidEvent");
        setDataEvent(bundle);
        setPictureEvent(bundle.getString("pictureEvent"));
        //Log.d("pic",bundle.getString("pictureEvent"));
    }

    //Information Dropdown
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Show_Event:
                setAssist(idEvent);
                Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setAssist(String uidEvent)
    {
        Event event = new Event(uidEvent);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child(firebaseUser.getUid()).child("assist").push().setValue(event);
    }

    public void setDataEvent(Bundle b){
        eventTitle = (TextView) findViewById(R.id.text_Title_Show_Event);
        eventTitle.setText(b.getString("titleEvent"));

        eventPlace = (TextView) findViewById(R.id.text_Place_Show_Event);
        eventPlace.setText(b.getString("placeEvent"));

        eventHour = (TextView) findViewById(R.id.text_Hour_Show_Event);
        eventHour.setText(b.getString("HourEvent"));

        eventDate = (TextView) findViewById(R.id.text_Date_Show_Event);
        eventDate.setText(b.getString("dateEvent"));

        eventDescription = (TextView) findViewById(R.id.text_Description_Show_Event);
        eventDescription.setText(b.getString("descriptionEvent"));

    }

    public void setPictureEvent(String picture) {
        eventPicture = (ImageView) findViewById(R.id.img_Show_Event);
        //eventPicture.setImageResource(R.drawable.udea);
        Picasso.with(getApplicationContext()).load(picture)
                .fit().centerInside()
                .into(eventPicture);
    }
}

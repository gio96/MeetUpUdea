package com.example.galonsogomez.meetupudea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowCourseActivity extends AppCompatActivity {

    //Views
    private TextView courseTitle,coursePlace, courseSchedule,courseDescription;
    private ImageView coursePicture;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course);

        //data sent from ShowGroupEventsFragment or EventsNavFragment
        bundle = getIntent().getExtras();

        //Set Data
        setDataCourse(bundle);
        setPictureCourse(bundle.getString("pictureCourse"));
    }

    public void setDataCourse(Bundle b){
        courseTitle = (TextView) findViewById(R.id.text_Title_Show_Course);
        courseTitle.setText(b.getString("titleCourse"));

        coursePlace = (TextView) findViewById(R.id.text_Place_Show_Course);
        coursePlace.setText(b.getString("placeCourse"));

        courseSchedule = (TextView) findViewById(R.id.text_schedule_Show_Course);
        courseSchedule.setText(b.getString("scheduleCourse"));

        courseDescription = (TextView) findViewById(R.id.text_Description_Show_Course);
        courseDescription.setText(b.getString("descriptionCourse"));

    }

    public void setPictureCourse(String picture) {
        coursePicture = (ImageView) findViewById(R.id.img_Show_Course);
        //eventPicture.setImageResource(R.drawable.udea);
        Picasso.with(getApplicationContext()).load(picture)
                .fit().centerInside()
                .into(coursePicture);
    }
}

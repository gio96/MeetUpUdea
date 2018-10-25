package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class ShowGroupCoursesFragment extends Fragment {

    private RecyclerView recyclerVShowCourse;
    //Firebase
    private DatabaseReference mreference;

    public ShowGroupCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle b = getActivity().getIntent().getExtras();
        String uidGroup = b.getString("UID");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_group_courses, container, false);

        Query courses = FirebaseDatabase.getInstance().getReference().child("groups").child(uidGroup).child("courses");
        recyclerVShowCourse = (RecyclerView) view.findViewById(R.id.recyclerV_Show_Course);
        mreference = courses.getRef();
        mreference.keepSynced(true);

        recyclerVShowCourse.setHasFixedSize(true);
        recyclerVShowCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Course, ShowGroupCoursesFragment.ItemViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Course, ShowGroupCoursesFragment.ItemViewHolder>
                        (Course.class, R.layout.item_course, ShowGroupCoursesFragment.ItemViewHolder.class, mreference) {

                    @Override
                    public void populateViewHolder(ShowGroupCoursesFragment.ItemViewHolder courseViewHolder, final Course model, int position) {

                        // To assign the info from Database to cardView
                        //groupViewHolder.setIdGroup(model.getGroupUID());
                        courseViewHolder.setData(model.getTitle(), model.getPlace(), model.getSchedule(), model.getDescription());
                        courseViewHolder.setPicture(model.getPicture(), getActivity().getApplicationContext());
                        Log.d("showcourses", model.getPicture());

                    }
                };




        recyclerVShowCourse.setAdapter(firebaseRecyclerAdapter);


    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mview;
        TextView courseTitle,coursePlace,courseSchedule,courseDescription;
        ImageView coursePicture;
        //CardView cardViewEvent1 = (CardView) itemView.findViewById(R.id.cv_Item_Tab);
        public  ItemViewHolder(View itemView){
            super(itemView);
            mview = itemView;
        }

        public void setData(String title,String place,String schedule,String description){
            courseTitle = (TextView) itemView.findViewById(R.id.text_Title_Course_Tab);
            courseTitle.setText(title);

            coursePlace = (TextView) itemView.findViewById(R.id.text_Place_Course_Tab);
            coursePlace.setText(place);

            courseSchedule = (TextView) itemView.findViewById(R.id.text_Schedule_Course_Tab);
            courseSchedule.setText(schedule);

            courseDescription = (TextView) itemView.findViewById(R.id.text_Description_Course_Tab);
            courseDescription.setText(description);

        }

        public void setPicture(String picture, Context c) {
            coursePicture = (ImageView) itemView.findViewById(R.id.img_Picture_Course_Tab);
            Picasso.with(c).load(picture)
                    .fit()
                    .centerCrop()
                    .into(coursePicture);
        }

    }

}

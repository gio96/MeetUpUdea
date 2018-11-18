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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsNavFragment extends Fragment {

    //recyclerV_Events_Nav

    private RecyclerView recyclerVEventNav;
    //Firebase
    private DatabaseReference mreference;

    public EventsNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_nav, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //call the Database just the first 10 groups
        Query events = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("attend");
        Log.d("fragNav",events.getRef().toString());
        recyclerVEventNav = (RecyclerView) view.findViewById(R.id.recyclerV_Events_Nav);
        mreference = events.getRef();
        mreference.keepSynced(true);

        recyclerVEventNav.setHasFixedSize(true);
        recyclerVEventNav.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Event,EventsNavFragment.ItemViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Event,EventsNavFragment.ItemViewHolder>
                        (Event.class,R.layout.item_event_tab,EventsNavFragment.ItemViewHolder.class,mreference) {

                    @Override
                    public  void populateViewHolder(EventsNavFragment.ItemViewHolder groupViewHolder, final Event model , int position){

                        // To assign the info from Database to cardView
                        //groupViewHolder.setIdGroup(model.getGroupUID());
                        groupViewHolder.setData(model.getTitle(),model.getPlace(),model.getStarHour(),model.getDate());
                        groupViewHolder.setPicture(model.getPicture(),getActivity().getApplicationContext());
                        groupViewHolder.cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(view.getContext(),model.getStarHour(), Toast.LENGTH_SHORT).show();

                                //Send data to ShowEventActivity
                                Bundle bundle = new Bundle();
                                bundle.putString("uidEvent",model.getEventUID());
                                bundle.putString("titleEvent",model.getTitle());
                                bundle.putString("pictureEvent",model.getPicture());
                                bundle.putString("descriptionEvent",model.getDescription());
                                bundle.putString("dateEvent", model.getDate());
                                bundle.putString("placeEvent", model.getPlace());
                                bundle.putString("startHourEvent", model.getStarHour());
                                bundle.putString("finishHourEvent", model.getFinishHour());

                                Intent intent = new Intent(getActivity(),ShowEventActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });

                    }
                };

        recyclerVEventNav.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mview;
        TextView eventTitle,eventPlace,eventHour,eventDate;
        ImageView eventPicture;
        CardView cardViewEvent1 = (CardView) itemView.findViewById(R.id.cv_Item_Tab);
        public  ItemViewHolder(View itemView){
            super(itemView);
            mview = itemView;
        }

        public void setData(String title,String place,String hour,String date){
            eventTitle = (TextView) itemView.findViewById(R.id.text_Title_Event_Tab);
            eventTitle.setText(title);

            eventPlace = (TextView) itemView.findViewById(R.id.text_Place_Event_Tab);
            eventPlace.setText(place);

            eventHour = (TextView) itemView.findViewById(R.id.text_Hour_Event_Tab);
            eventHour.setText(hour);

            eventDate = (TextView) itemView.findViewById(R.id.text_Date_Event_Tab);
            eventDate.setText(date);

        }

        public void setPicture(String picture, Context c) {
            eventPicture = (ImageView) itemView.findViewById(R.id.img_Picture_Event_Tab);
            Picasso.with(c).load(picture)
                    .fit()
                    .centerCrop()
                    .into(eventPicture);
        }

    }

}

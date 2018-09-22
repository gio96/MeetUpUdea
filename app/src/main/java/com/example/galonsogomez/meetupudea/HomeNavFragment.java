package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class HomeNavFragment extends Fragment {

    private RecyclerView myrv;
    private DatabaseReference mreference;
    public HomeNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_nav, container, false);


        //call the Database just the first 10 groups
        Query groups = FirebaseDatabase.getInstance().getReference().child("groups").limitToFirst(10);
        mreference = groups.getRef();
        mreference.keepSynced(true);

        myrv = (RecyclerView) view.findViewById(R.id.recycler_View);
        myrv.setHasFixedSize(true);

        //POSIBLE PROBLEMA CON EL CONTEXTO
        myrv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Create firebaseRecyclerAdapter with a viewHolder to show the info of the
        // Database

        FirebaseRecyclerAdapter<Group,HomeActivity.GroupViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Group,HomeActivity.GroupViewHolder>
                        (Group.class,R.layout.item_group,HomeActivity.GroupViewHolder.class,mreference) {

                    @Override
                    public  void populateViewHolder(HomeActivity.GroupViewHolder groupViewHolder, final Group model , int position){

                        // To assign the info from Database to cardView
                        //groupViewHolder.setIdGroup(model.getGroupUID());
                        groupViewHolder.setTitle(model.getTitle());

                        //POSIBLE ERROR DEL CONTEXT
                        groupViewHolder.setPicture(model.getPicture(),getActivity().getApplicationContext());
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

                                Intent intent = new Intent(getActivity(),ShowGroupActivity.class);
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

    /* public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }*/
}

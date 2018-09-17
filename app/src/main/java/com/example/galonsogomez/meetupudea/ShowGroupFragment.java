package com.example.galonsogomez.meetupudea;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGroupFragment extends Fragment {

    TextView textView;
    ImageView groupPicture;
    public ShowGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*String data = getArguments().getString("title");
        Log.d("frag", data);*/

        //Obtener datos de la activity
        Bundle b = getActivity().getIntent().getExtras();
        Log.d("fragil", b.getString("title"));
        //changeData(b.getString("title"));

       View view = inflater.inflate(R.layout.fragment_show_group, container, false);
       setTitle(b.getString("title"),view);
       setPicture(b.getString("picture"),getActivity().getApplicationContext(),view);
       return view;
    }

    public void setTitle(String title, View view)
    {
        textView =(TextView) view.findViewById(R.id.text_Group_Fragment);
        textView.setText(title);
    }

   public void setPicture(String picture, Context c, View view) {
        groupPicture = (ImageView) view.findViewById(R.id.img_Group_Fragment);
        Picasso.with(c).load(picture)
                .fit()
                .centerCrop()
                .into(groupPicture);
    }

}

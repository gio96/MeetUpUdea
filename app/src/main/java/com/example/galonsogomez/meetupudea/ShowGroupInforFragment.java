package com.example.galonsogomez.meetupudea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGroupInforFragment extends Fragment {

    EditText editText;
    TextView textView;
    public ShowGroupInforFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Get data from Activity
        Bundle b = getActivity().getIntent().getExtras();

        //Log.d("DESCRIPTION", b.getString("description"));

        View view= inflater.inflate(R.layout.fragment_show_group_infor, container, false);
        setDescription(b.getString("description"),view);
        return view;
    }

    public void setDescription(String description, View view){
        textView = view.findViewById(R.id.textViewInfor);
        textView.setText(description);
    }

}

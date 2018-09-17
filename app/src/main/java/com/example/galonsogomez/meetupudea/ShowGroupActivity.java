package com.example.galonsogomez.meetupudea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowGroupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        //datos enviados desde home
        Bundle b = getIntent().getExtras();

        /*String id = b.getString("UID");
        String title = b.getString("title");
        String picture = b.getString("picture");
        Log.d("Valores", "bundle: " + id + title + picture);*/

        ShowGroupFragment showGroupFragment = new ShowGroupFragment();
        //Se le envian los datos al fragment
        showGroupFragment.setArguments(b);
    }

}

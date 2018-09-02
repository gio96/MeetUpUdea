package com.example.galonsogomez.meetupudea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button logOutButton;
    List<Group> lstGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lstGroup = new ArrayList<>();
        lstGroup.add(new Group("Meetup",R.drawable.meetup));
        lstGroup.add(new Group("Prueba2",R.drawable.prueba2));
        lstGroup.add(new Group("Prueba3",R.drawable.prueba3));
        lstGroup.add(new Group("Prueba4",R.drawable.prueba4));
        lstGroup.add(new Group("Prueba5",R.drawable.prueba5));
        lstGroup.add(new Group("Prueba6",R.drawable.prueba6));
        lstGroup.add(new Group("Prueba3",R.drawable.prueba3));
        lstGroup.add(new Group("Prueba4",R.drawable.prueba4));
        lstGroup.add(new Group("Prueba5",R.drawable.prueba5));
        lstGroup.add(new Group("Prueba6",R.drawable.prueba6));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_View);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lstGroup);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
    }

   /* public void onClick(View v){

        switch (v.getId()){
            case R.id.btn_SignOut:
                signOut();
                break;
        }
    }*/

    public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

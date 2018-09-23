package com.example.galonsogomez.meetupudea;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class BottomNavActivity extends AppCompatActivity {


    //Views
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.btn_Navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Default view
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container,new HomeNavFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeNavFragment();
                    break;
                case R.id.navigation_Meetups:
                    selectedFragment = new MeetUpNavFragment();
                    break;
                case R.id.navigation_Events:
                    selectedFragment = new EventsNavFragment();
                    break;
                case R.id.navigation_User:
                    selectedFragment = new UserNavFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

}

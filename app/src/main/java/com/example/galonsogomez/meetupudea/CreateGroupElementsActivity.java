package com.example.galonsogomez.meetupudea;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CreateGroupElementsActivity extends AppCompatActivity {

    // Views
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_elements);

        //Obtener el valor uidgroup enviado desde showGroup
        //String showGroup = getIntent().getStringExtra("groupUID");
        Bundle bundle = getIntent().getExtras();
        CreateEventFragment createEventFragment = new CreateEventFragment();
        createEventFragment.setArguments(bundle);
        //Log.d("createEvent", b);

        createTabs();
    }

    public void createTabs(){
        //Tabs
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutElements);
        viewPager = (ViewPager) findViewById(R.id.viewPagerElements);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //add Fragments
        adapter.addFragment(new CreateEventFragment(), "Eventos");
        adapter.addFragment(new CreateCourseFragment(), "Cursos");
        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

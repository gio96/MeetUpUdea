package com.example.galonsogomez.meetupudea;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonDate,buttonTime;
    EditText editTextDate,editTextTime;
    private int dia,mes,año,hora,minutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        String b = getIntent().getStringExtra("groupUID");
        Log.d("createEvent", b);

        buttonDate = (Button) findViewById(R.id.btn_Date);
        editTextDate = (EditText) findViewById(R.id.text_Date);
        buttonDate.setOnClickListener(this);

        buttonTime = (Button) findViewById(R.id.btn_Time);
        editTextTime = (EditText) findViewById(R.id.text_Time);
        buttonTime.setOnClickListener(this);
        //finish();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_Date:
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfyear, int dayOfMonth) {
                        datePicker.init(2018,9,28,null);
                        editTextDate.setText(dayOfMonth+"/"+ (monthOfyear + 1)+"/"+year);
                    }
                },dia,mes,año);
                datePickerDialog.show();
                break;

            case R.id.btn_Time:
                final Calendar cal = Calendar.getInstance();
                hora = cal.get(Calendar.HOUR);
                minutos = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,false);
                timePickerDialog.show();
                break;
        }
    }
}

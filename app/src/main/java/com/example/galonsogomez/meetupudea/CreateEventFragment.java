package com.example.galonsogomez.meetupudea;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener{

    Button buttonDate,buttonTime;
    EditText editTextDate,editTextTime;
    private int dia,mes,año,hora,minutos;
    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        buttonDate = (Button) view.findViewById(R.id.btn_Date);
        editTextDate = (EditText) view.findViewById(R.id.text_Date);
        buttonDate.setOnClickListener(this);

        buttonTime = (Button) view.findViewById(R.id.btn_Time);
        editTextTime = (EditText) view.findViewById(R.id.text_Time);
        buttonTime.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_Date:
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

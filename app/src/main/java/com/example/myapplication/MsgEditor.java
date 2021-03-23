package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.myapplication.pickers.DatePickerFragment;
import com.example.myapplication.pickers.TimePickerFragment;

import java.util.Calendar;
import java.util.Objects;


public class MsgEditor extends OptionsMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    DBHelper mDatabase;

    private EditText phoneField;
    private EditText textField;
    private EditText dateField;
    private EditText timeField;
    private long dateMillis;
    private long timeMillis;
    Calendar mainCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_editor);
        mDatabase = new DBHelper(this);
        mainCalendar = Calendar.getInstance();
        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Create new message");
        }


        phoneField = findViewById(R.id.phoneField);
        textField = findViewById(R.id.textField);

        dateField = findViewById(R.id.dateField);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        timeField = findViewById(R.id.timeField);
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

    }




    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

//        mainCalendar.add(Calendar.DATE,1);
        mainCalendar.set(Calendar.YEAR, year);
        mainCalendar.set(Calendar.MONTH, month);
        mainCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        @SuppressLint("DefaultLocale") String currentDate = String.format("%d-%02d-%02d", year, month+1, dayOfMonth);
        dateField.setText(currentDate);

//        dateMillis = cal.getTimeInMillis();
}

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//        Calendar cal = Calendar.getInstance();
////        cal.set(Calendar.HOUR, hourOfDay);
////        cal.set(Calendar.MINUTE, minute);
////        cal.set(Calendar.SECOND, 0);
        mainCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mainCalendar.set(Calendar.MINUTE, minute);
        mainCalendar.set(Calendar.SECOND, 0);

//        if (cal.before(Calendar.getInstance())) {
//            cal.add(Calendar.DATE, 1);
//        }
        @SuppressLint("DefaultLocale") String currentDate = String.format("%02d:%02d", hourOfDay, minute);
        timeField.setText(currentDate);

        //TODO: This has to be fixed now.
//        timeMillis = cal.getTimeInMillis() - System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void addItem() {
        if(phoneField.getText().toString().trim().length() == 0) {
            toastMessage("Phone number must be specified!");
            return;
        } else if(timeField.getText().toString().trim().length() == 0) {
            toastMessage("Time must be specified!");
            return;
        }else if(dateField.getText().toString().trim().length() == 0) {
            toastMessage("Date must be specified!");
            return;
        } else if(textField.getText().toString().trim().length() == 0) {
            toastMessage("Message is required!");
            return;
        }
        String number = phoneField.getText().toString();
        String time = timeField.getText().toString();
        String date = dateField.getText().toString();
        String text = textField.getText().toString();
        //TODO:[FinalUpdate] time + date in miliseconds
        // Trebuie sa nu functioneze cand este pe minus valoarea lui alarmMiillis, alarmMillis = 0; (Check it, not sure)

        final long alarmId = System.currentTimeMillis();
        final long alarmMillis = Math.abs(mainCalendar.getTimeInMillis() - alarmId);


        boolean insertData = mDatabase.addData(number, time, date, text, alarmId);
        if(!insertData) {
            Toast.makeText(this, "Error inserting data!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Very required
        Intent intent = new Intent();
        toastMessage("ALarm milis: " + alarmMillis);
        intent.putExtra("alarmMillis", alarmMillis);
        setResult(RESULT_OK, intent);
        finish();

    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

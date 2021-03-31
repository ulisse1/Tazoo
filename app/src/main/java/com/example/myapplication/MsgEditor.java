package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.pickers.DatePickerFragment;
import com.example.myapplication.pickers.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class MsgEditor extends OptionsMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private int PICK_CONTACT = 99;

    DBHelper mDatabase;
    private EditText phoneField;
    private EditText textField;
    private EditText dateField;
    private ImageButton numberButton;
    private ImageButton dateButton;
    private ImageButton timeButton;
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create new message");


        phoneField = findViewById(R.id.phoneField);
        timeField = findViewById(R.id.timeField);
        dateField = findViewById(R.id.dateField);
        textField = findViewById(R.id.textField);
        numberButton = findViewById(R.id.addNumberButton);
        timeButton = findViewById(R.id.addTimeButton);
        dateButton = findViewById(R.id.addDateButton);



        numberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

    }


    //TODO: FIX Agenda output

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            if(contactUri != null) {
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    toastMessage(""+cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

                cursor.close();
            }



        }
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



        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm");
        String input = date + " " + time;


        final long alarmId = System.currentTimeMillis();
        final long alarmMillis = mainCalendar.getTimeInMillis() - alarmId;
        if(alarmMillis <= 0) {
            toastMessage("Date is invalid!");
            return;
        }


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

package com.example.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends OptionsMenuActivity {



    //TODO [UPDATE]
    // + removeItemById(alarmId)
    // + onTimeSet -> cal.getTimeInMillis() - System.currentTimeInMillis();
    // + Database column -> alarmId
    // + recycleItems [RENAMED] -> Alarms;
    // + ------ FIX milliseconds gap ------
    // + ----- FIX Mysterious bug Cancel Alarm -----


    //TODO: AlarmManager: cancelAlarm, fara refresh pe alarma, conversie data+timp in milisecunde.
    //TODO: Fix cancelAlarm
    //TODO: Big and at last: Layout has to be modified to be rescalable



    //TODO: [LAST UPDATE]: Am scos "startAlarm()" de la onResume(onClickListener), cancelAlarm merge, startAlarm merge la timp, trebuie sa mai fac teste pe
    // alarmMillis, variabila ce trebuie sa preia data si timpul si sa converteasca in milisecunde, se transmite in onActivityResult ca parametru in extra de la un intent, iar apoi este trimis
    // catre startAlarm.
    // Ai la baza de date removeItem in functie de time, date, number si removeItemByID in caz ca ai nevoie.
    // Gandeste-te ce face alarmUp mai exact, s-ar putea doar sa previna un bug ca mesajul sa fie instantiat de 2 ori, tu vrei sa poti sa verifici daca exista deja o alarma la acea ora
    // catre acel numar, pe acea data. Sugestie -> Cauta comenzi de SQL pentru baza de date si modifica, vezi daca exista ceva pentru a verifica in SQL


    //TODO:
    // Am rezolvat functionalitea startAlarm, cancelAlarm.
    // mai am de fixat UI(rescale), modificat SMSManager, fixed alarmMillis(trebuie fixat pe long < 0).


    private ArrayList<Alarm> Alarms;
    private RecyclerView mRecyclerView;
    public CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public DBHelper dbHelper;
    private int CREATE_MESSAGE = 414;
    //TODO: SMS SENDER

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Database implemented
        dbHelper = new DBHelper(this);
        Alarms = populateList();



//        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new CardAdapter(recycleItems);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

        buildRecyclerView();


    }




    @Override
    public void openMsgEditor() {
        Intent intent = new Intent(MainActivity.this, MsgEditor.class);
        startActivityForResult(intent, CREATE_MESSAGE);
//        startActivity(new Intent(MainActivity.this, MsgEditor.class));
    }

    public ArrayList<Alarm> populateList() {

        ArrayList<Alarm> Alarms = new ArrayList<>();
        Cursor data = dbHelper.getData();
        if (data != null) {
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                Alarm aux = new Alarm(data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getLong(5));
                Alarms.add(aux);
            }

        }

        return Alarms;
    }

//    public void showData() {
//        Cursor data = dbHelper.getData();
//        while(data.moveToNext()) {
//            toastMessage(data.getString(0) + " " + data.getString(1) + " " + data.getString(2));
//        }
//    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_MESSAGE && resultCode == RESULT_OK) {

                if(data != null) {
                    long alarmMillis = data.getLongExtra("alarmMillis", 0);
                    if(alarmMillis == 0) {
                        toastMessage("Alarm will not start, internal error!");
                    } else {
                        toastMessage("Milliseconds till proc: " + alarmMillis);
                        Alarms = populateList();
                        int position = Alarms.size() -1;
                        startAlarm(position, alarmMillis);
                    }

                mAdapter = new CardAdapter(Alarms);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CardAdapter(Alarms);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }




    public void removeItem(int position) {
        String time, date, number;
        number = Alarms.get(position).getPhoneNo();
        time = Alarms.get(position).getTime();
        date = Alarms.get(position).getDate();
        boolean isRemoved = dbHelper.removeData(number, time, date);
        if(isRemoved) {
            toastMessage("Record deleted");
        } else {
            toastMessage("Error deleting record");
        }
        mAdapter.notifyItemRemoved(position);
        Alarms.remove(position);
    }

    public void removeItemById(int position) {

        boolean isRemoved = dbHelper.removeDataById(Alarms.get(position).getMillis());
        if(isRemoved) {
            toastMessage("Record deleted by ID");
        } else {
            toastMessage("Error deleting record by ID");
        }
        mAdapter.notifyItemRemoved(position);
        Alarms.remove(position);
    }


    public void openDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete record")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAlarm(position);
//                        removeItem(position);
                        removeItemById(position);
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.show();
    }
//    private void startAlarm(final int position) {
//
//        toastMessage("This item's position is " + position);
//    }


//    TODO: GOOD ONE
    private void startAlarm(final int position, long alarmMillis) {

        //Todo: Important testing purposes
        String date = Alarms.get(position).getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD", Locale.ENGLISH);
        Date aux = null;
        try {
            aux = sdf.parse(date);
            toastMessage("Date is " + aux.toString());
        } catch (ParseException e) {
            //e.printStackTrace();
        }


        boolean alarmUp = (PendingIntent.getBroadcast(this,  (int)Alarms.get(position).getMillis(),
                new Intent("com.example.myapplication"),
                PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp) {
            toastMessage("Alarm running!");
            return;
        }


        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.setAction("SomeAction");
        intent.putExtra("phoneNumber", Alarms.get(position).getPhoneNo());
        intent.putExtra("message", Alarms.get(position).getShortenMsg());

        //TODO: Changed PendingIntent.FLAG_UPDATE_CURRENT at "flags"
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)Alarms.get(position).getMillis(), intent, 0);
        //TODO: Here

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        toastMessage("Milliseconds: " + alarmMillis);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmMillis, pendingIntent);



        //TODO: Permission check which is checked somewhere else
//        boolean permission = checkSMSPermission();
//        if(permission == true) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                //TODO: TOO LOW API LEVEL
//                Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
//                        16000, pendingIntent);
//            }
//        } else {
//            toastMessage("No permission granted!");
//        }
    }






//    //TODO: [VERIFY]
//
//    private void startAlarm(int position, long alarmMillis) {
//
//        Intent intent = new Intent(this, AlarmReceiver.class);
////        intent.setAction("SomeAction");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)Alarms.get(position).getMillis(), intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //TODO: TOO LOW API LEVEL
//            Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, alarmMillis, pendingIntent);
//        }
//
//
//    }



    private void cancelAlarm(final int position) {


        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("phoneNumber", Alarms.get(position).getPhoneNo());
        intent.putExtra("message", Alarms.get(position).getShortenMsg());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) Alarms.get(position).getMillis(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO: Testing zone, put whatever you wish here, should've been an edit on click
            }
            @Override
            public void onDeleteClick(final int position) {


                //TODO: Check if it works (9/14/2020)
                openDialog(position);
            }
        });
    }


}


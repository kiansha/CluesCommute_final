package com.example.aakansha.newpool.activity.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.aakansha.newpool.R;

/**
 * Created by aakansha on 7/21/2017.
 */

public class BroadcastTest extends Activity {

   // private static final String TAG = "BroadcastTest++";
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_map);
        intent = new Intent(BroadcastTest.this, BroadcastService.class);
    }

    private BroadcastReceiver broadcastReceivers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceivers, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceivers);
        stopService(intent);
    }

    private void updateUI(Intent intent) {

        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        Log.d("inside update","inside update"+counter);
        Double lat= intent.getDoubleExtra("current_latitude",0.0);
        Double longi= intent.getDoubleExtra("current_longitude",0.0);
        String driver_id = intent.getStringExtra("driver_id");
        Log.d("+++++++++++", counter);
        Log.d("+++++++++++", time);
        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        TextView txtDriver = (TextView) findViewById(R.id.txtdriverid);
        TextView txtLat = (TextView) findViewById(R.id.txtlat);
        TextView txtlong = (TextView) findViewById(R.id.txtlong);
        txtDateTime.setText(time);
        txtCounter.setText(counter);
        txtDriver.setText(driver_id);
        txtLat.setText(String.valueOf(lat));
        txtlong.setText(String.valueOf(longi));
    }
}

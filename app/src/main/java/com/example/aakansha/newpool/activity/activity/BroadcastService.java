package com.example.aakansha.newpool.activity.activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aakansha.newpool.activity.app.AppConfig;
import com.example.aakansha.newpool.activity.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aakansha on 7/21/2017.
 */
//service for fetching current location of driver from database and to re-draw it on the passenger's map, after every 10 seconds
//used in PassengerMapsActivity
public class BroadcastService extends Service {

    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.example.aakansha.finalmap";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 10000); // 10 seconds
        }
    };

    private void DisplayLoggingInfo() {

        //function volley
        check("C225");
//        Log.d(TAG, "entered DisplayLoggingInfo");
//        intent.putExtra("time", new Date().toLocaleString());
//        intent.putExtra("counter", String.valueOf(++counter));
//        sendBroadcast(intent);
    }

    private void check(final String employee_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PASSENGER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject res = jObj.getJSONObject("response");
                        String driver_id = res.getString("driver_id");
                        Double current_latitude = res.getDouble("current_latitude");
                        Double current_longitude = res.getDouble("current_longitude");
                        intent.putExtra("time", new Date().toLocaleString());
                        intent.putExtra("counter", String.valueOf(++counter));
                        intent.putExtra("driver_id",driver_id);
                        Log.d("Driver id++",driver_id);
                        intent.putExtra("current_latitude",current_latitude);
                        intent.putExtra("current_longitude",current_longitude);
                        sendBroadcast(intent);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("employee_id", employee_id);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}

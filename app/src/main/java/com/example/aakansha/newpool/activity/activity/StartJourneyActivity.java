package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aakansha.newpool.R;
import com.example.aakansha.newpool.activity.app.AppConfig;
import com.example.aakansha.newpool.activity.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StartJourneyActivity extends AppCompatActivity {

    Button startJourney;
    Button startJourneyPass;
    //fetch employee_id from app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_journey);

        startJourney=(Button)findViewById(R.id.btn_startJourney);
        startJourneyPass=(Button)findViewById(R.id.btn_startJourneyPass);

        startJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcStartJourney("T5339");
            }
        });

        startJourneyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcStartJourneyPass("C225");
            }
        });
    }
    public void funcStartJourney(final String employee_id ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_START_JOURNEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("++++", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(StartJourneyActivity.this, "Maps api called", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(StartJourneyActivity.this,NextRideActivity.class);
                        i.putExtra("employee_id",employee_id);
                        startActivity(i);
                        // tv.setText(random);
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
                Log.e("+++++","Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("employee_id",employee_id);
                return params;
            }
        };
        // Adding request to request queue
        Log.d("checking", String.valueOf(strReq));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void funcStartJourneyPass(final String employee_id ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PASSENGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("++++", "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject res = jObj.getJSONObject("response");
                        Double current_latitude = res.getDouble("current_latitude");
                        Double current_longitude = res.getDouble("current_longitude");
                        Double my_latitude = res.getDouble("passenger_latitude");
                        Double my_longitude = res.getDouble("passenger_longitude");
                        Intent intent= new Intent(StartJourneyActivity.this,PassengerMapsActivity.class);
                        intent.putExtra("current_latitude",current_latitude);
                        intent.putExtra("current_longitude",current_longitude);
                        intent.putExtra("passenger_longitude",my_latitude);
                        intent.putExtra("passenger_longitude",my_longitude);
                        intent.putExtra("shopclues_longitude",my_latitude);
                        intent.putExtra("shopclues_longitude",my_longitude);
                        startActivity(intent);
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
                Log.e("+++++","Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("employee_id",employee_id);
                return params;
            }
        };
        // Adding request to request queue
        Log.d("checking", String.valueOf(strReq));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}

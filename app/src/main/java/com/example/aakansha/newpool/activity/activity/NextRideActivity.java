package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class NextRideActivity extends AppCompatActivity {

    public static final String TAG="TAG";
    TextView tv_name;
    TextView tv_address;
    TextView tv_time;
    TextView tv_mobile;
    TextView tv_id;
    Button btn_startRide;
    static SharedPreferences sharedPref;
    public static String d_employee_id; //getting intent from startJourney

    // load from server in this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_ride);
        sharedPref= PreferenceManager.getDefaultSharedPreferences(NextRideActivity.this);
        Intent i= getIntent();
        d_employee_id = i.getExtras().getString("employee_id");
        Log.d("d_employee_id",d_employee_id);

        tv_address = (TextView) findViewById(R.id.address);
        tv_name = (TextView) findViewById(R.id.name);
        tv_time = (TextView) findViewById(R.id.time);
        tv_mobile = (TextView) findViewById(R.id.mobile);
        tv_id = (TextView) findViewById(R.id.eid);
        btn_startRide=(Button)findViewById(R.id.startRide);

        //fetch here and set everything,volley
        getLatLng(d_employee_id);

    }

    private void getLatLng(final String d_employee_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_latLng";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MAP, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Map Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    final String p_employee_id = jObj.getString("employee_id");

                    // Check for error node in json
                    if (p_employee_id!=null) {

                        String p_name = jObj.getString("employee_name");
                        String p_address = jObj.getString("address");
                        String p_mobile = jObj.getString("mobile_number");
                        Double p_latitude= jObj.getDouble("latitude");
                        Double p_longitude= jObj.getDouble("longitude");
                        Double d_latitude= jObj.getDouble("driver_latitude");
                        Double d_longitude=jObj.getDouble("driver_longitude");

                        // Inserting data in preferences
                        SharedPreferences.Editor editor= sharedPref.edit();
                        editor.clear();
                        editor.putString("d_employee_id",d_employee_id);
                        editor.putString("p_employee_id",p_employee_id);
                        editor.putString("d_latitude", d_latitude.toString());
                        editor.putString("d_longitude", d_longitude.toString());
                        editor.putString("p_latitude", p_latitude.toString());
                        editor.putString("p_longitude", p_longitude.toString());
                        editor.putString("p_address",p_address);
                        editor.putString("p_name",p_name);
                        editor.putString("p_mobile",p_mobile);
                        editor.apply();
                        Log.d("p_next_destination", String.valueOf(d_latitude)+d_longitude+"oncreate::driver");
                        //setting in ui
                        sharedPref= PreferenceManager.getDefaultSharedPreferences(NextRideActivity.this);
                        tv_address.setText(sharedPref.getString("p_address", "p_address"));
                        tv_name.setText(sharedPref.getString("p_name","p_name"));
                        tv_mobile.setText(sharedPref.getString("p_mobile","p_mobile"));
                        tv_id.setText(sharedPref.getString("p_employee_id","p_employee_id"));

                        btn_startRide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i= new Intent(NextRideActivity.this,MapsActivity.class);
                                startActivity(i);
                            }
                        });

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<String, String>();
            params.put("employee_id", d_employee_id);
            return params;
        }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}

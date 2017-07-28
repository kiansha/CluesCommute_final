package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aakansha.newpool.R;
import com.example.aakansha.newpool.activity.app.AppConfig;
import com.example.aakansha.newpool.activity.app.AppController;
import com.example.aakansha.newpool.activity.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BeginActivity extends AppCompatActivity {

    public RadioGroup radioGroup;
    public static String accessType;
    public static int access_as;
    private SQLiteHandler db;

    Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        go=(Button)findViewById(R.id.go);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String employee_id = user.get("employee_id");


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                access_as = radioGroup.indexOfChild(findViewById(checkedId));

                accessAs(employee_id, access_as);

                switch (access_as){

                    case 0:
                        Toast.makeText(getBaseContext(), "Accessing App as Passenger", Toast.LENGTH_SHORT).show();
//                        Intent i= new Intent(BeginActivity.this,NavMainActivity.class);
//                        startActivity(i);
                        break;
                    case 1:
                        Toast.makeText(getBaseContext(), "Accessing App as Driver"+access_as, Toast.LENGTH_SHORT).show();
//                        Intent p= new Intent(BeginActivity.this,NavMainActivity.class);
//                        startActivity(p);
                        break;
                    default:
                        break;
                }

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p= new Intent(BeginActivity.this,NavMainActivity.class);
                startActivity(p);
            }
        });
    }

    public void changeAccessType(){
        if(access_as==1)
            accessType = "driver";
        else if (access_as==0)
            accessType = "passenger";
    }

    public void accessAs(String employee_id,int access_as){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_ACCESS+employee_id+"&access_as="+access_as, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("request", "Request: " + response.toString());

                try {
                    JSONObject jo = new JSONObject(response);

                    //Log.d("********", response);
                    boolean error = jo.getBoolean("error");

                    if(!error){
                        String toastText = jo.getString("response");

                        Toast.makeText(BeginActivity.this, toastText, Toast.LENGTH_SHORT).show();

                    }

                    else{
                        String errorMsg = jo.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request", "Request List Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hideDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}

package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewRequestAllAcceptedActivity extends AppCompatActivity {

    public Button showMaps;
    Button startAsPass;
    //public ListView lv1;

    public RecyclerView recyclerView;

    ArrayList<RequestSent> arrayListAccepted =new ArrayList<>();

    String employee_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_all_accepted);

        startAsPass=(Button)findViewById(R.id.startAsPass);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView5);
        showMaps = (Button)findViewById(R.id.ShowMaps);
        employee_id = getIntent().getExtras().getString("employee_id");

        showMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcStartJourney(employee_id);
            }
        });

        startAsPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcStartJourneyPass(employee_id);
            }
        });
        fetchRequest();

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;

            button = (Button) itemView.findViewById(R.id.selectButton);
            employeeNameShow = (TextView) itemView.findViewById(R.id.employee_name_show);
            employeeIdShow = (TextView) itemView.findViewById(R.id.employee_id_show);
            address = (TextView) itemView.findViewById(R.id.address);

        }

        View rootView;
        Button button;
        TextView employeeNameShow;
        TextView employeeIdShow;
        TextView address;

    }

    public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li = getLayoutInflater();
            View convertView;

            convertView = li.inflate(R.layout.list_item_request, parent, false);

            ItemViewHolder itemViewHolder = new ItemViewHolder(convertView);

            return itemViewHolder;
        }


        @Override
        public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {

            RequestSent itm = arrayListAccepted.get(position);

            itemViewHolder.employeeIdShow.setText(itm.getEmployee_id());

            Log.d("&&&&&&&&&&", itm.getEmployee_id());
            itemViewHolder.employeeNameShow.setText(itm.getEmployee_name());

            itemViewHolder.address.setText(itm.getAddress());

            itemViewHolder.button.setText("CANCEL");


            // if(itemViewHolder.button!=null)
            itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getApplicationContext(), "CANCELLING", Toast.LENGTH_SHORT).show();
                    sendResponseCancel((String) itemViewHolder.employeeIdShow.getText());
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayListAccepted.size();
        }


    }
    public void fetchRequest(){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REQUEST_ACCEPTED+employee_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("request", "Request: " + response.toString());

                try {
                    JSONObject jo = new JSONObject(response);

                    Log.d("*******API", response);

                    boolean error = jo.getBoolean("error");

                    if (arrayListAccepted.size()>0)
                    arrayListAccepted.clear();

                    if(!error){

                        JSONArray jArr = jo.getJSONArray("response");
                        Log.d("*******CONVERTED", response);
                        Log.d("SIZE OUTPUT API", String.valueOf(jArr.length()));



                        for (int i = 0; i < jArr.length(); i++) {

                            Log.d("********%@@@@@@@@@@@@", String.valueOf(jArr));
                            Log.d("********%@@@@@@@@@@@@", String.valueOf(jArr.length()));

                            JSONObject eventObj = jArr.getJSONObject(i);
                            String acceptor_id = eventObj.getString("employee_id");
                            String acceptor_name = eventObj.getString("employee_name");
                            String address = eventObj.getString("address");

                            //String address = status+" "+message_sent+" "+notify;
                            arrayListAccepted.add(new RequestSent(acceptor_id,acceptor_name,address));

                            Log.d("********!!", String.valueOf(arrayListAccepted));


                        }



                    }
                    else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jo.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }

                    ItemViewAdapter itemAdapter = new ItemViewAdapter();
                    LinearLayoutManager lm = new LinearLayoutManager(ViewRequestAllAcceptedActivity.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListAccepted.size());
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

    public void sendResponseCancel(String sender_id) {

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESPONSE_CANCEL+ this.employee_id +"&sender_id="+sender_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("request", "Request: " + response.toString());

                try {
                    JSONObject jo = new JSONObject(response);

                    Log.d("*******API", response);

                    boolean error = jo.getBoolean("error");



                    if(!error){

                        boolean response1 = jo.getBoolean("response");

                        Log.d("*******CONVERTED", response);
                        //Log.d("SIZE OUTPUT API", String.valueOf(response1.length()));
                        Toast.makeText(ViewRequestAllAcceptedActivity.this, "Successfully Cancelled! ", Toast.LENGTH_SHORT).show();
                        fetchRequest();


                    }
                    else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jo.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }

                    ItemViewAdapter itemAdapter = new ItemViewAdapter();
                    LinearLayoutManager lm = new LinearLayoutManager(ViewRequestAllAcceptedActivity.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListAccepted.size());
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
                        Toast.makeText(ViewRequestAllAcceptedActivity.this, "Maps api called", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(ViewRequestAllAcceptedActivity.this,NextRideActivity.class);
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
                        Intent intent= new Intent(ViewRequestAllAcceptedActivity.this,PassengerMapsActivity.class);
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



package com.example.aakansha.newpool.activity.activity;

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

public class MakeRequestAsDriver extends AppCompatActivity {

    //public ListView lv1;

    public RecyclerView recyclerView;
    ArrayList<RequestSent> arrayListPassengers =new ArrayList<>();
    String employee_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request_as_driver);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView2);
        employee_id = getIntent().getExtras().getString("employee_id");
        fetchRequest();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;

            button = (Button) itemView.findViewById(R.id.selectButton);
            employeeIdShow = (TextView) itemView.findViewById(R.id.employee_id_show);
            employeeNameShow = (TextView) itemView.findViewById(R.id.employee_name_show);
            address = (TextView) itemView.findViewById(R.id.address);

        }

        View rootView;
        Button button;
        TextView employeeIdShow;
        TextView address;
        TextView employeeNameShow;

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

            RequestSent itm = arrayListPassengers.get(position);

            itemViewHolder.employeeIdShow.setText(itm.getEmployee_id());

            Log.d("&&&&&&&&&&", itm.getEmployee_id());

            itemViewHolder.employeeNameShow.setText(itm.getEmployee_name());

            itemViewHolder.address.setText(itm.getAddress());

            itemViewHolder.button.setText("OFFER RIDE");


            // if(itemViewHolder.button!=null)
            itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "MAKING REQUEST AS DRIVER", Toast.LENGTH_SHORT).show();
                    makeRequest((String) itemViewHolder.employeeIdShow.getText());
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayListPassengers.size();
        }


    }
    public void fetchRequest(){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SAME_ZONE_PEOPLE+employee_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("request", "Request: " + response.toString());

                try {
                    JSONObject jo = new JSONObject(response);

                    Log.d("%%%%%%", response);

                    boolean error = jo.getBoolean("error");

                    if (arrayListPassengers.size()>0)
                        arrayListPassengers.clear();

                    if(!error){
                        JSONArray jArr = jo.getJSONArray("response");

                        if (arrayListPassengers.size()>0)
                            arrayListPassengers.clear();

                        for (int i = 0; i < jArr.length(); i++) {

                            Log.d("********", String.valueOf(jArr));
                            Log.d("********", String.valueOf(jArr.length()));

                            JSONObject eventObj = jArr.getJSONObject(i);
                            String person_id = eventObj.getString("employee_id");
                            String person_name = eventObj.getString("employee_name");
                            String address = eventObj.getString("address");

                            arrayListPassengers.add(new RequestSent(person_id,person_name,address));

                            Log.d("********!!", String.valueOf(arrayListPassengers));


                        }


                    }

                    else{
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jo.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }

                    ItemViewAdapter itemAdapter = new ItemViewAdapter();
                    LinearLayoutManager lm = new LinearLayoutManager(MakeRequestAsDriver.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListPassengers.size());
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

    public void makeRequest(String requestee_id) {

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_REQUEST+ this.employee_id +"&requestee_id="+requestee_id, new Response.Listener<String>() {

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
                        Toast.makeText(MakeRequestAsDriver.this, "Successfully Sent Request! ", Toast.LENGTH_SHORT).show();
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
                    LinearLayoutManager lm = new LinearLayoutManager(MakeRequestAsDriver.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListPassengers.size());
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



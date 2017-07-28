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

public class ViewRequestAllReceivedActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    ArrayList<RequestSent> arrayListRequestReceived =new ArrayList<>();

    String employee_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_all_received);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView1);
        employee_id = getIntent().getExtras().getString("employee_id");

        fetchRequest();

//        loadRequest1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fetchRequest();
//            }
//        });

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
        TextView employeeNameShow;
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

            RequestSent itm = arrayListRequestReceived.get(position);

            itemViewHolder.employeeIdShow.setText(itm.getEmployee_id());

            Log.d("&&&&&&&&&&", itm.getEmployee_id());
            itemViewHolder.employeeNameShow.setText(itm.getEmployee_name());
            itemViewHolder.address.setText(itm.getAddress());
            itemViewHolder.button.setText("ACCEPT");


            // if(itemViewHolder.button!=null)
            itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getApplicationContext(), "TRYING TO ACCEPT", Toast.LENGTH_SHORT).show();
                    sendResponseAccept((String) itemViewHolder.employeeIdShow.getText());
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayListRequestReceived.size();
        }


    }
    public void fetchRequest(){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REQUEST_RECEIVED+employee_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("request", "Request: " + response.toString());

                try {
                    JSONObject jo = new JSONObject(response);

                    //Log.d("********", response);

                    boolean error = jo.getBoolean("error");

                    if (arrayListRequestReceived.size()>0)
                        arrayListRequestReceived.clear();

                    if(!error){
                        JSONArray jArr = jo.getJSONArray("response");

                        if (arrayListRequestReceived.size()>0)
                            arrayListRequestReceived.clear();

                        for (int i = 0; i < jArr.length(); i++) {

                            Log.d("********", String.valueOf(jArr));
                            Log.d("********", String.valueOf(jArr.length()));

                            JSONObject eventObj = jArr.getJSONObject(i);
                            String id = eventObj.getString("employee_id");
                            String name = eventObj.getString("employee_name");
                            String address = eventObj.getString("address");

                            arrayListRequestReceived.add(new RequestSent(id,name,address));

                            Log.d("********!!", String.valueOf(arrayListRequestReceived));


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
                    LinearLayoutManager lm = new LinearLayoutManager(ViewRequestAllReceivedActivity.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListRequestReceived.size());
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

    public void sendResponseAccept(String sender_id) {

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESPONSE_ACCEPT+ this.employee_id +"&sender_id="+sender_id, new Response.Listener<String>() {

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
                        Toast.makeText(ViewRequestAllReceivedActivity.this, "Successfully Accepted! ", Toast.LENGTH_SHORT).show();
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
                    LinearLayoutManager lm = new LinearLayoutManager(ViewRequestAllReceivedActivity.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(itemAdapter);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d("ArrayList","size"+ arrayListRequestReceived.size());
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



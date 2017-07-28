package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.aakansha.newpool.R;

public class Main2Activity extends AppCompatActivity {

    Button b1, b2,b3,b4,b5;
    String employee_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        b1= (Button) findViewById(R.id.goToAllSent);
        b2= (Button) findViewById(R.id.goToAllReceived);
        b3= (Button) findViewById(R.id.goToAllPassengers);
        b4= (Button) findViewById(R.id.goToAllDrivers);
        b5= (Button) findViewById(R.id.goToAllAccepted);

        final Bundle bun = this.getIntent().getExtras();
        employee_id = bun.getString("employee_id");


//        employee_id = getIntent().getExtras().getString("employee_id");
//        accessType = getIntent().getExtras().getString()

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Main2Activity.this,
                        ViewRequestAllSentActivity.class);


                intent.putExtra("employee_id",employee_id);



                startActivity(intent);
            }
        });

         b2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(Main2Activity.this,ViewRequestAllReceivedActivity.class);
                 i.putExtra("employee_id",employee_id);
                 startActivity(i);

             }
         });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this,MakeRequestAsDriver.class);
                i.putExtra("employee_id",employee_id);
                startActivity(i);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this,MakeRequestAsPassenger.class);
                i.putExtra("employee_id",employee_id);
                startActivity(i);

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this,ViewRequestAllAcceptedActivity.class);
                i.putExtra("employee_id",employee_id);
                startActivity(i);

            }
        });
    }

}

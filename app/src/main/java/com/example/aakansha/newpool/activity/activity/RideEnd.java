package com.example.aakansha.newpool.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.aakansha.newpool.R;

//ride end activity on reaching shopclues, in driver's app

public class RideEnd extends AppCompatActivity {

    Button go_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_end);
        go_home =(Button)findViewById(R.id.button_go_home);

        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(RideEnd.this,NavMainActivity.class);
                startActivity(i);
            }
        });
    }
}

package com.example.aakansha.newpool.activity.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.aakansha.newpool.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//passenger map updated
public class PassengerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    private Marker marker;
    static String d_name;
    static double dlat,dlong,plat,plong,slat,slong;
    static LatLng dlatt,platt,slatt;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_maps);
        i= getIntent();
        d_name=i.getExtras().getString("current_latitude");
        dlat=i.getExtras().getDouble("current_latitude");
        dlong=i.getExtras().getDouble("current_longitude");
        plat=i.getExtras().getDouble("passenger_latitude");
        plong=i.getExtras().getDouble("passenger_longitude");
        plat=i.getExtras().getDouble("shopclues_latitude");
        plong=i.getExtras().getDouble("shopclues_longitude");
        dlatt= new LatLng(dlat,dlong);
        platt= new LatLng(plat,plong);
        slatt= new LatLng(slat,slong);
        intent = new Intent(PassengerMapsActivity.this, BroadcastService.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private BroadcastReceiver broadcastReceivers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMap(mMap,intent);
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
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("seq","onMapReady");
        mMap = googleMap;
         //Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(dlatt).title(d_name));
        mMap.addMarker(new MarkerOptions().position(platt).title("My Home"));
        mMap.addMarker(new MarkerOptions().position(slatt).title(d_name));
    }

    public void updateMap(GoogleMap googleMap, Intent intent){
        Log.d("seq","updateMap");
        mMap=googleMap;
        Double lat= intent.getDoubleExtra("current_latitude",0.0);
        Double longi= intent.getDoubleExtra("current_longitude",0.0);
        LatLng latLng = new LatLng(lat, longi);
        if(marker!=null){
            marker.remove();
        }
        marker=mMap.addMarker(new MarkerOptions().position(latLng).title("Marker current position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
}

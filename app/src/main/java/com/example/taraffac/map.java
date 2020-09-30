package com.example.taraffac;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//speedometer imports

public class map extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    Button profile;
    Button log;
    Button add;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    LatLng latLng;
    TextView txtCurrentSpeed;
    private Geocoder geocoder;
    DatabaseReference ref;


    private static final String TAG = "MapsActivity";

    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        profile = findViewById(R.id.but_pofile_map);
        log = findViewById(R.id.but_logout_map);
        add = (Button) findViewById(R.id.add_bump2);
        ref = FirebaseDatabase.getInstance().getReference().child("SpeedBump");
        client = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        showbumps();

        //speedometer code
        // LocationManager lm =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //  this.onLocationChanged(null);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // mMap.setOnMapLongClickListener(this);
        // mMap.setOnMarkerDragListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } } }


    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //We can show a dialog that permission is not granted...
            }
        }
    }


    public void go_to_profile(View v) {
        Intent profile = new Intent(this, profile.class);
        startActivity(profile);
    }

    public void log_out(View v) {
        FirebaseAuth.getInstance().signOut();// R add it
        Intent log = new Intent(this, login.class);
        startActivity(log);
        finish();// R add it
    }

    // show markers on bumps
    private void showbumps() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SpeedBump> bumps = new ArrayList<>();
                bumps.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    SpeedBump bump = postSnapshot.getValue(SpeedBump.class);
                    bumps.add(bump);

                    // here you can access to name property like university.name
                   double bump_lat = bump.getLatitude();
                   double bump_long = bump.getLongitude();

                    LatLng latLng = new LatLng(bump_lat,bump_long);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }});
    }


// add new speed bump
    public void add() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double loc_lat = location.getLatitude();
                double loc_long = location.getLongitude();

                Intent intent = new Intent(map.this, add.class);
                intent.putExtra("Latitude", loc_lat);
                intent.putExtra("Longitude", loc_long);
                startActivity(intent);
            }
        });





        //Intent a = new Intent(this, add.class);
       // startActivity(a);
    }



    //speedometer code

    @Override
    public void onLocationChanged(Location location) {

        if (location==null){
            // if you can't get speed because reasons :)
            txtCurrentSpeed.setText("00 km/h");
        }
        else{
            //int speed=(int) ((location.getSpeed()) is the standard which returns meters per second. In this example i converted it to kilometers per hour

            int speed=(int) ((location.getSpeed()*3600)/1000);

            txtCurrentSpeed.setText(speed+" km/h");
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderDisabled(String provider) {


    }
}
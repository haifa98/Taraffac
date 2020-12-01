package com.example.taraffac;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import  androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//speedometer imports

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class map extends FragmentActivity implements LocationListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    ////////////Interface_Menu ///////////
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ////////////Interface_Menu ///////////



    private GoogleMap mMap;
    Button profile;
    Button log;
    Button add;
    //FloatingActionButton add;
    Button notify;
    ToggleButton active;

    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    LatLng latLng;
    TextView txtCurrentSpeed;
    private Geocoder geocoder;
    DatabaseReference ref, refV;
    FirebaseUser user;
    AlertDialog.Builder builder;
    boolean state = false;
    String addingType;
    double not_lat;
    double not_long;
    String sub;
    SpeedBump bump;
    String CheckAddingType;
    ////
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    FirebaseUser  firebaseUser;
    public String voiceType ="Voice command";
    double notify_long,notify_lat,add_lat,add_long;
    //public Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    String bump_key,bump_loc;
    int deleteCount;

    /////

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SpeedBump sb;
    private static final String TAG = "MapsActivity";
    TextView type1;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
      //  profile = findViewById(R.id.but_pofile_map);
        //log = findViewById(R.id.but_logout_map);
        add =  findViewById(R.id.add_bump2);
        //notify = (Button) findViewById(R.id.showNotificationBtn);
        active = findViewById(R.id.map_deactive);

        ////////////Interface_Menu ///////////
        //---------HOOKS-----------------
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
       androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.toolbar);//    <string name="navi"> toolbar</string>
        setSupporActionBar(toolbar);

        /////////////////////// tool bar//////////////


        //     navigation Drawer menu
        navigationView.bringToFront();
//drawerLayout,Toolbar=toolbar, R.string.navi_o,R.string.navi_c);
      toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,  R.string.navi_o,R.string.navi_c);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        //     navigation Drawer menu




        //
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        fStore =FirebaseFirestore.getInstance();
        //
        ref = FirebaseDatabase.getInstance().getReference().child("SpeedBump");
        client = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        showbumps();


        // get state value from activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            state = extras.getBoolean("state");
        }
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.taraffac", MODE_PRIVATE);
        active.setChecked(sharedPrefs.getBoolean("active", state));
/*
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify();
            }
        });

 */
        // activate and deactivate
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isActive();
            }
        });


// check if active for add
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (active.isChecked()) {
                    add();
                }
            }
        });



        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(active.isChecked()){ Notify();}
                handler.postDelayed(this, 5000);
            }
        };

//Start
        handler.postDelayed(runnable, 1000);
        ////////////////////////////////////////////////////////////////
       // Check Adding Type

        final DocumentReference documentReference =fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {//start method
                assert documentSnapshot != null;
                CheckAddingType= documentSnapshot.getString("addingType");
                Toast.makeText(map.this, CheckAddingType, Toast.LENGTH_SHORT).show();
             /*  if (CheckAddingType.equals(voiceType) ){
                   ;
                }else{
                   }*/

            }
        });
        
        /////////////////////////////////////////////////////////////

    }// end on create

    private void setSupporActionBar(Toolbar toolbar) {

    }
    //     navigation Drawer menu

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //     navigation Drawer menu


    // check if it is activate or deactivate
    public void isActive() {
        if (active.isChecked()) {
            //getSpeechInput();
            // active.setText("Activated");
            SharedPreferences.Editor editor = getSharedPreferences("com.example.taraffac", MODE_PRIVATE).edit();
            editor.putBoolean("active", true);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("com.example.taraffac", MODE_PRIVATE).edit();
            editor.putBoolean("active", false);
            editor.commit();
        }// end if else
    }
/////


    @Override // set the map
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
    }

    // set the user location
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    // zoom on user location
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

    @Override // check if user allow GPS service
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {

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
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot bumpSnapshot : locationSnapshot.getChildren()) {
                        SpeedBump bump = bumpSnapshot.getValue(SpeedBump.class);
                        bumps.add(bump);

                        double bump_lat = bump.getLatitude();
                        double bump_long = bump.getLongitude();
                        String bump_type = bump.getType();
                        String bump_size = bump.getSize();

                        LatLng latLng = new LatLng(bump_lat, bump_long);
                        String bump_info = " type : " + bump_type + " size : " + bump_size;
// set height & width - apply style
                        int height = 130;
                        int width = 130;
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                        //   BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);
                        MarkerOptions marker = new MarkerOptions().position(latLng).title("Bump info").snippet(bump_info).icon(smallMarkerIcon);
// create marker for bumps
                        mMap.addMarker(marker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        } // get user location
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double loc_lat = location.getLatitude();
                double loc_long = location.getLongitude();
                double  x =  distance(loc_lat,loc_long,notify_lat,notify_long);
                if(x>0.010){
                // send info to add class
                Intent intent = new Intent(map.this, add.class);
                intent.putExtra("Latitude", loc_lat);
                intent.putExtra("Longitude", loc_long);
                intent.putExtra("userType", CheckAddingType);
                    add_lat=loc_lat;
                    add_long=loc_long;
                startActivity(intent);
                }else{
                    Toast.makeText(map.this, "The bump is already exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Intent a = new Intent(this, add.class);
        // startActivity(a);
    }

    //notification code start
  /*  public void sendMessage() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } // get user location

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double user_lat = location.getLatitude();
                double user_long = location.getLongitude();

                String sub1 = new DecimalFormat("00.00").format(user_lat);
                String sub2 = new DecimalFormat("00.00").format(user_long);
                String userLoc1km = sub1.replace('.', '-') + "_" + sub2.replace('.', '-');

                double newlat = Double.parseDouble(new DecimalFormat("00.000").format(user_lat));

                double newlng = Double.parseDouble(new DecimalFormat("00.000").format(user_long));

                txtCurrentSpeed.setText(userLoc1km + newlat + newlng);
                // checkforspeedbump(userLoc1km,newlat,newlng);

            }
        });

    }  */

  /*  public void checkforspeedbump(final String usercoordinates1km, final double userlat100m, final double userlong100m) {


        FirebaseDatabase.getInstance().getReference("SpeedBump").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(usercoordinates1km)) {
                    // run some code

                    for (DataSnapshot snapshot : dataSnapshot.child(usercoordinates1km).getChildren()) {
                        sb = snapshot.getValue(SpeedBump.class);

                        if (sb.getLatitude() == userlat100m && sb.getLongitude() == userlong100m) {
                            // Do something in response to button
                            builder.setTitle("SLOW DOWN SPEED BUMP AHEAD");
                            builder.setMessage("Type: " + sb.getType() + "/n" + "Size: " + sb.getSize());
                            builder.show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }  */

   /* public void go_to_edit(DialogInterface.OnClickListener view) {
        Intent go_register = new Intent(this, edit_speed_bump.class);
        startActivity(go_register);
    }

    public void go_to_report(DialogInterface.OnClickListener view) {
        Intent go_register = new Intent(this, report.class);
        startActivity(go_register);
    }
    */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //   if (checkButton()) {
        // sendMessage();
        //   }
    }

    //speedometer code

    ////// Voice command code

    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    assert result != null;

                    if (result.contains("add")) {
                        add();
                    }
                    break;
                }
        }
    }


    public void Notify() {
   /*

        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SpeedBump> bumps = new ArrayList<>();
                bumps.clear();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                        SpeedBump bump = locationSnapshot.getValue(SpeedBump.class);
                        bumps.add(bump);

                        double bump_lat_not = bump.getLatitude();
                        double bump_long_not = bump.getLongitude();
                        String bump_type_not  = bump.getType();
                        String bump_size_not  = bump.getSize();  */

        // compare
        double[] lat = {10.001, 24.801, 10.040, 10.006, 23.007, 34.004};
        double[] lon = {33.020, 46.702, 23.050, 56.006, 22.006, 12.005};
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
                not_lat = location.getLatitude();
                not_long = location.getLongitude();}});

        String sub1 = new DecimalFormat("00.00").format(not_lat);
        String sub2=  new DecimalFormat("00.00").format(not_long);
        sub = sub1.replace('.','-')+"_"+sub2.replace('.','-');

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("SpeSpeedBumped");
        DatabaseReference zone1Ref = zonesRef.child(sub);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SpeedBump> bumps = new ArrayList<>();
                bumps.clear();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    bump_loc=locationSnapshot.getKey();
                    for (DataSnapshot bumpSnapshot : locationSnapshot.getChildren()) {
                        bump = bumpSnapshot.getValue(SpeedBump.class);
                        bumps.add(bump);

                        double bump_lat_not = bump.getLatitude();
                        double bump_long_not = bump.getLongitude();
                        String bump_type = bump.getType();
                        String bump_size = bump.getSize();
                        deleteCount = bump.getDeleteOption();
                        bump_key = bumpSnapshot.getKey();

                       // LatLng latLng = new LatLng(bump_lat, bump_long);
                        String bump_info = " type : " + bump_type + " size : " + bump_size;
// set height & width - apply style

                        double  x =  distance(bump_lat_not,bump_long_not,not_lat,not_long);
                        if(x<0.150){
                            // Toast.makeText(this, " near", Toast.LENGTH_SHORT).show();
                            if(add_lat!=bump_lat_not & add_long!=bump_long_not ){

                             if( notify_lat!=bump_lat_not || notify_long!=bump_long_not){
                                Alertt( bump_lat_not , bump_long_not, bump_type,  bump_size);
                                 notify_lat=bump_lat_not;
                                 notify_long=bump_long_not;
                            }}
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void Alertt(final double lat, final double lon, final String type, final String size) {

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(map.this);
// Setting Dialog Title
        alertDialog2.setTitle("Speed bump info");
// Setting Dialog Message
        alertDialog2.setMessage("Type: "+type+"Size: "+size);
// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent i = new Intent(getApplicationContext(), edit_speed_bump.class);
                        i.putExtra("key",bump_key);
                        i.putExtra("loc",bump_loc);
                        i.putExtra("type",type);
                        i.putExtra("size",size);
                        i.putExtra("latitude",lat);
                        i.putExtra("longitude",lon);
                        i.putExtra("deleteCount",deleteCount);
                        startActivity(i);
                    }});
// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("Report",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        Intent i = new Intent(getApplicationContext(), report.class);
                        i.putExtra("latitude",lat);
                        i.putExtra("longitude",lon);

                        startActivity(i);
                    }});
// Showing Alert Dialog
        alertDialog2.show(); }

    // lat long 2 is user
    //calculate the distance between user location and bump location
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    //     navigation Drawer menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(map.this, profile.class);
                // startActivities(intent);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(map.this, login.class);
                startActivity(intent);
                break;
        }
                drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    //     navigation Drawer menu
}
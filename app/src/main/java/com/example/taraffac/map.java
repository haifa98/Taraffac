package com.example.taraffac;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;

//speedometer imports

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class map extends FragmentActivity implements LocationListener, OnMapReadyCallback, IBaseGpsListener,RecognitionListener {
    ////////////Interface_Menu ///////////
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ////////////Interface_Menu ///////////



    private GoogleMap mMap;
    Button profile;
    Button log;
    FloatingActionButton add;
    //FloatingActionButton add;
    ToggleButton active;
    TextView textView;

    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    private Geocoder geocoder;
    DatabaseReference ref;

    boolean state = false;
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
    double notify_long,notify_lat,add_lat,add_long;
    //public Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    String bump_key, bump_locKey, bump_loc;

    int deleteCount;
    RelativeLayout logout_rl,provile_rl;

    /////
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    /* Keyword we are looking for to activate recognition */
    private static final String KEYPHRASE = "open";
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    /* Recognition object */
    private SpeechRecognizer recognizer;

    private static final String TAG = "MapsActivity";

    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
      //  profile = findViewById(R.id.but_pofile_map);
        //log = findViewById(R.id.but_logout_map);
        add = (FloatingActionButton) findViewById(R.id.add_bump2);
        //notify = (Button) findViewById(R.id.showNotificationBtn);
        active = findViewById(R.id.map_deactive);
        textView = findViewById(R.id.textView_map);
        logout_rl=findViewById(R.id.logout_rl);
        provile_rl=findViewById(R.id.provile_rl);
        provile_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(map.this, profile.class));
            }
        });
        logout_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(map.this, login.class));
            }
        });

        ////////////Interface_Menu ///////////
        //---------HOOKS-----------------

        /////////////////////// tool bar//////////////


        //     navigation Drawer menu

//drawerLayout,Toolbar=toolbar, R.string.navi_o,R.string.navi_c);

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
                handler.postDelayed(this, 2000);
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
                Toast.makeText(map.this, CheckAddingType, Toast.LENGTH_SHORT).show();} });


       //
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(CheckAddingType.toLowerCase().contains("Voice".toLowerCase()) && active.isChecked() ){ runRecognizerSetup(); }
            }}, 3000); //  1000 = 1 sec
        runRecognizerSetup();
        //speedometer
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);
/*
        CheckBox chkUseMetricUntis = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        chkUseMetricUntis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                map.this.updateSpeed(null);
            }
        });


 */

    }// end on create

    private void setSupporActionBar(Toolbar toolbar) {

    }
    //     navigation Drawer menu



    //     navigation Drawer menu


    // check if it is activate or deactivate
    public void isActive() {
        if (active.isChecked()) {
            //getSpeechInput();
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
                if(x>0.020){
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

    //speedometer code
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if(location != null)
        {
            location.setUseMetricunits(true);
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

       // String strUnits = "miles/hour";
       // if(this.useMetricUnits())
      //  {
        String strUnits = "meters/second";
      //  }

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

   /* private boolean useMetricUnits() {
        // TODO Auto-generated method stub
        CheckBox chkUseMetricUnits = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        return chkUseMetricUnits.isChecked();
    }


    */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            CLocation myLocation = new CLocation(location, true);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    //end speedometer code

    ////// Voice command code



    public void Notify() {

        // compare

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

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SpeedBump> bumps = new ArrayList<>();

                bumps.clear();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    bump_locKey= locationSnapshot.getKey();
                   // bump_loc = locationSnapshot;

                    if (bump_locKey.equals(sub)) {
                        for (DataSnapshot bumpSnapshot : locationSnapshot.getChildren()) {
                            bump = bumpSnapshot.getValue(SpeedBump.class);
                            bumps.add(bump);

                            double bump_lat_not = bump.getLatitude();
                            double bump_long_not = bump.getLongitude();
                            String bump_type = bump.getType();
                            String bump_size = bump.getSize();
                            deleteCount = bump.getDeleteOption();
                            bump_key = bumpSnapshot.getKey();
                            bump_loc= locationSnapshot.getKey();

                            // LatLng latLng = new LatLng(bump_lat, bump_long);

                            double x = distance(bump_lat_not, bump_long_not, not_lat, not_long);
                            if (x < 0.400) {

                                if (add_lat != bump_lat_not & add_long != bump_long_not) {

                                    if (notify_lat != bump_lat_not || notify_long != bump_long_not) {
                                        Alertt(bump_lat_not, bump_long_not, bump_type, bump_size);
                                        notify_lat = bump_lat_not;
                                        notify_long = bump_long_not; } } } }
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
        alertDialog2.setMessage("Type: "+type+"  Size: "+size);
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
                        i.putExtra("userType", CheckAddingType);
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
                        i.putExtra("userType", CheckAddingType);

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

    //     navigation Drawer menu


    // Voice command

    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(map.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    textView.setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */

    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals("add"))
            add();
        else if (text.equals("Good morning"))
            makeText(this, "Good morning", Toast.LENGTH_SHORT).show();

        else
            textView.setText(text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */

    public void onResult(Hypothesis hypothesis) {
        textView.setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }


    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */

    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                // Disable this line if you don't want recognizer to save raw
                // audio files to app's storage
                //.setRawLogDir(assetsDir)
                .getRecognizer();
        recognizer.addListener((RecognitionListener) this);
        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        // Create your custom grammar-based search
        File menuGrammar = new File(assetsDir, "mymenu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

    }


    public void onError(Exception error) {
        textView.setText(error.getMessage());
    }


    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }





}
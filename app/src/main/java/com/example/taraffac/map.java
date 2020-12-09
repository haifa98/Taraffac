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
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
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



import static android.widget.Toast.makeText;

//speedometer imports

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class map extends FragmentActivity implements LocationListener, OnMapReadyCallback, IBaseGpsListener, RecognitionListener {

    private GoogleMap mMap;
    FloatingActionButton add;
    ToggleButton active;
    FusedLocationProviderClient client;
    private Geocoder geocoder;
    DatabaseReference ref;
    float nCurrentSpeed;
    boolean state = false;
    double not_lat;
    double not_long;
    String sub;
    SpeedBump bump;
    String CheckAddingType;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    FirebaseUser  firebaseUser;
    double notify_long,notify_lat,add_lat,add_long;
    String bump_key, bump_locKey, bump_loc;
    double bump_lat_not, bump_long_not ;
    String bump_type , bump_size ;
    double x;
    Float f2;
    int deleteCount;
    RelativeLayout logout_rl,provile_rl;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private static final String TAG = "MapsActivity";
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;

    private void resetSpeechRecognizer() {

    if (speech != null)
        speech.destroy();
    if(active.isChecked() ) { // this check ensure the SR will stop when the system not active
    speech = SpeechRecognizer.createSpeechRecognizer(this);
    Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
    if (SpeechRecognizer.isRecognitionAvailable(this))
        speech.setRecognitionListener(this);
    else
        finish();
} }

    private void setRecogniserIntent() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        /*
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now...");
            startActivityForResult(intent, REQUEST_CODE);

         */
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        add = (FloatingActionButton) findViewById(R.id.add_bump2);
        active = findViewById(R.id.map_deactive);
        logout_rl=findViewById(R.id.logout_rl);
        provile_rl=findViewById(R.id.provile_rl);
        provile_rl.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { startActivity(new Intent(map.this, profile.class)); }});
        logout_rl.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { startActivity(new Intent(map.this, login.class)); }});

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
        add.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if (active.isChecked()) { add(); } }});

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override public void run() { if(active.isChecked()){ Notify();}handler.postDelayed(this, 2000); }};
//Start
        handler.postDelayed(runnable, 1000);

       // Check Adding Type
        final DocumentReference documentReference =fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {//start method
                assert documentSnapshot != null;
                CheckAddingType= documentSnapshot.getString("addingType"); }});

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

        // check for audio permission
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // when the user open the system and the system is activated - this code will start the speech recognition after 3 sec
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CheckAddingType!= null) {
                    if (CheckAddingType.equals("Voice command") && active.isChecked()) {
                        resetSpeechRecognizer();
                        setRecogniserIntent();
                        speech.startListening(recognizerIntent); } } }}, 3000); //  1000 = 1 sec

    }// end on create

    // check if it is activate or deactivate
    public void isActive() {
        if (active.isChecked()) {
            //getSpeechInput();
            SharedPreferences.Editor editor = getSharedPreferences("com.example.taraffac", MODE_PRIVATE).edit();
            editor.putBoolean("active", true);
            editor.commit();
    // when the user open the system and the system is deactivated - this code will start the speech recognition if the user activate the system
if(CheckAddingType.equals("Voice command")) {
            resetSpeechRecognizer();
            setRecogniserIntent();
            speech.startListening(recognizerIntent); }

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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE); } } }

    // set the user location
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        mMap.setMyLocationEnabled(true); }

    // zoom on user location
    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }}); }

    @Override // check if user allow GPS service and allow speech recognition
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech.startListening(recognizerIntent);
            } else {
                Toast.makeText(map.this, "Permission Denied!", Toast
                        .LENGTH_SHORT).show();
                finish(); } }

        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation(); } else { } }
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

                        MarkerOptions marker = new MarkerOptions().position(latLng).title("Bump info").snippet(bump_info).icon(smallMarkerIcon);
                  // create marker for bumps in the map
                        mMap.addMarker(marker); } } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }}); }


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
                // check if there is a bump within 20m on location - if there is one already exist it will not add
                if(x>0.020){
                // send info to add class
                Intent intent = new Intent(map.this, add.class);
                intent.putExtra("Latitude", loc_lat);
                intent.putExtra("Longitude", loc_long);
                intent.putExtra("userType", CheckAddingType);
                    add_lat=loc_lat;
                    add_long=loc_long;
                startActivity(intent);
                }else{ Toast.makeText(map.this, "The bump is already exist", Toast.LENGTH_SHORT).show(); } }}); }

    //speedometer code
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        nCurrentSpeed = 0;

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
        String strUnits = "km/h";
      //  }

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
       // txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
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
        { CLocation myLocation = new CLocation(location, true);
            this.updateSpeed(myLocation); } }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onGpsStatusChanged(int event) { }

    //end speedometer code


    public void Notify() {

        // check for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }//get user location
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) { // get location
                not_lat = location.getLatitude();
                not_long = location.getLongitude();}});
//
        String sub1 = new DecimalFormat("00.00").format(not_lat);
        String sub2=  new DecimalFormat("00.00").format(not_long);
        sub = sub1.replace('.','-')+"_"+sub2.replace('.','-');
// get bumps info for compare bumps
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

                            bump_lat_not = bump.getLatitude();
                            bump_long_not = bump.getLongitude();
                            bump_type = bump.getType();
                            bump_size = bump.getSize();
                            deleteCount = bump.getDeleteOption();
                            bump_key = bumpSnapshot.getKey();
                            bump_loc= locationSnapshot.getKey();
                            // x is the distance between the bump and user location
                            x = distance(bump_lat_not, bump_long_not, not_lat, not_long);
                            if (x < 0.300) {
                                if (add_lat != bump_lat_not & add_long != bump_long_not) {

                                    if (notify_lat != bump_lat_not || notify_long != bump_long_not) {
                                        f2 = 10.00f; //
                                      //  if(Float.compare(nCurrentSpeed, f2) > 0){// if the user speed less than 10k/h will not notify
                                        Alertt(bump_type, bump_size);
                                        notify_lat = bump_lat_not;
                                        notify_long = bump_long_not; } } } }} } } //}

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }}); }

    private void Alertt(final String type, final String size) {
// show alert dialog
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(map.this);
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        alertDialog2.setTitle("Speed bump info");
        alertDialog2.setMessage("Type: "+type+"  Size: "+size);
        alertDialog2.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        edit(); }});

        alertDialog2.setNegativeButton("Report",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        report(); }});
        alertDialog2.show(); }

public void edit(){
    Intent i = new Intent(getApplicationContext(), edit_speed_bump.class);
    i.putExtra("key",bump_key);
    i.putExtra("loc",bump_loc);
    i.putExtra("type",bump_type);
    i.putExtra("size",bump_size);
    i.putExtra("latitude",bump_lat_not);
    i.putExtra("longitude",bump_long_not);
    i.putExtra("deleteCount",deleteCount);
    i.putExtra("userType", CheckAddingType);
    startActivity(i);
}
public void report(){
    Intent i = new Intent(getApplicationContext(), report.class);
    i.putExtra("latitude",bump_lat_not);
    i.putExtra("longitude",bump_long_not);
    i.putExtra("userType", CheckAddingType);

    startActivity(i);

    }
    //calculate the distance between user location and bump location - // lat long 2 is user
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


    // speech recognition codes
    @Override
    public void onResume() {
            Log.i(LOG_TAG, "resume");
            super.onResume();
            resetSpeechRecognizer();
            if(recognizerIntent!= null ){
            speech.startListening(recognizerIntent);  }
         }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "pause");
        super.onPause();
        speech.stopListening();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "stop");
        super.onStop();
        if (speech != null) {
            speech.destroy();
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        speech.stopListening();
    }
// this method will get the voice input
    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String result : matches) {
            if(result.equals("at") || result.equals("add")){
                add();
            }
            if(result.equals("edit") && Float.compare(nCurrentSpeed, f2) < 0 &&x < 0.300 && notify_lat > 0){
                edit();
            }
            if(result.equals("report") && Float.compare(nCurrentSpeed, f2) < 0 && x < 0.300 && notify_lat > 0){
                report(); }
        }
        speech.startListening(recognizerIntent);
    }
// this method get the error if it happen
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.i(LOG_TAG, "FAILED " + errorMessage);

        // rest voice recogniser
        resetSpeechRecognizer();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

}
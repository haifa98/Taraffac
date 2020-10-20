package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class add extends AppCompatActivity {
    Button profile;
    Button log;
    Button add;
    double latitude;
    double longitude;
    RadioGroup type;
    RadioButton type1;
    RadioGroup size;
    RadioButton size1;
    DatabaseReference dataBymp;
    long id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        profile = (Button)findViewById(R.id.but_Ppofile3);
        log = (Button)findViewById(R.id.but_log_out3);
        add = (Button)findViewById(R.id.add_save);
        type = (RadioGroup) findViewById(R.id.add_type);
        size = (RadioGroup) findViewById(R.id.add_size);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             latitude = extras.getDouble("Latitude");
             longitude = extras.getDouble("Longitude");
        }
        dataBymp = FirebaseDatabase.getInstance().getReference("SpeedBump");



    }
    public String checkType(){
        int radioID = type.getCheckedRadioButtonId();
        type1 = findViewById(radioID);
        String t = (String) type1.getText();
        return t ;

    }

    public String checkSize(){
        int radioID = size.getCheckedRadioButtonId();
        size1 = findViewById(radioID);
        String s = (String) size1.getText();
        return s ;

    }

    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }


    public void save(View v){
        String type2 = checkType();
        String size2 = checkSize();
if(!type2.isEmpty() & !size2.isEmpty() &  longitude > 0 & latitude > 0 ) {

// add in database
    double newlat = Double.parseDouble(new DecimalFormat("##.####").format(latitude));

    double newlng = Double.parseDouble(new DecimalFormat("##.###").format(longitude));
    String id = dataBymp.push().getKey();

    SpeedBump bump = new SpeedBump ( newlat, newlng, type2, size2);
    String sub = new DecimalFormat("##.###").format(latitude) + ","+ new DecimalFormat("##.###").format(longitude);
    dataBymp.child(id).child(sub).setValue(bump);


    Toast t = Toast.makeText(this, " The Adding was successful", Toast.LENGTH_SHORT);
    t.setGravity(Gravity.TOP, 0, 90);
    t.show();
}else {
    Toast t = Toast.makeText(this, " The Adding was failed", Toast.LENGTH_SHORT);
    t.setGravity(Gravity.TOP, 0, 90);
    t.show();


} // return to map
        Intent log = new Intent(this,map.class);
        startActivity(log);

    }
}
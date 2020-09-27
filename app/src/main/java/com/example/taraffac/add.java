package com.example.taraffac;

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
        Toast t =  Toast.makeText(this, " The Adding was successful"+ type2 + " " +  longitude +" "+ size2 , Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP, 0,90);
        t.show();

        Intent log = new Intent(this,map.class);
        startActivity(log);

    }
}
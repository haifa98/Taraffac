package com.example.taraffac;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class add extends AppCompatActivity {
    Button profile;
    Button log;
    Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        profile = (Button)findViewById(R.id.but_Ppofile3);
        log = (Button)findViewById(R.id.but_log_out3);
        add = (Button)findViewById(R.id.add_save);
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
        Toast t =  Toast.makeText(this, " The Adding was successful"  , Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP, 0,90);
        t.show();

    }
}


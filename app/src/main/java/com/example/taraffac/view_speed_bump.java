package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class view_speed_bump extends AppCompatActivity {
    Button profile;
    Button log;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_speed_bump);
        profile = (Button)findViewById(R.id.but_Ppofile);
        log = (Button)findViewById(R.id.but_log_out);
        add = (Button)findViewById(R.id.but_deactivate);
    }
    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }

    public void add(View v){
        Intent a = new Intent(this,add.class);
        startActivity(a);
    }

}






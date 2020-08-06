package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class view_speed_bump extends AppCompatActivity {
    Button profile;
    Button log;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_speed_bump);
        profile = (Button)findViewById(R.id.but_logout);
        log = (Button)findViewById(R.id.but_logout);
        add = (Button)findViewById(R.id.add_bump);
    }
    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        FirebaseAuth.getInstance().signOut();// R add it
        Intent log = new Intent(this,login.class);
        startActivity(log);
        finish();// R add it
    }

    public void add(View v){
        Intent a = new Intent(this, add.class);
        startActivity(a);
    }

}






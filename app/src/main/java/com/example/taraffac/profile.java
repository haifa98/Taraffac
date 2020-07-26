package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class profile extends AppCompatActivity {
    Button edit;
    Button log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit = (Button)findViewById(R.id.edit_profile);
        log = (Button)findViewById(R.id.but_log_out5);

    }
    public void return_main(View view) {
        onBackPressed();
    }
    public void edit(View v){
        Intent profile = new Intent(this,edit_profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }

}


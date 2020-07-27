package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class notify extends AppCompatActivity {
    Button profile;
    Button log;
    Button btn_reportopt;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        profile = findViewById(R.id.but_Ppofile2);
        log = findViewById(R.id.but_log_out2);
        btn_reportopt = findViewById(R.id.btn_reportopt);

        btn_reportopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), report.class);
                startActivity(i);
            }
        });
    }
    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }
}



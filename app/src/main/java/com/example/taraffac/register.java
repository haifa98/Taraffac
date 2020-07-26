package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class register extends AppCompatActivity {
    ImageView return_main2;
    Button go_register_to_home1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        return_main2= findViewById(R.id.image_back2);
        go_register_to_home1=findViewById(R.id.butt_register);
    }

    public void return_main2(View view) {
        onBackPressed();
    }

    public void go_to_home1(View view) {
        Intent go_home1 = new Intent(this,view_speed_bump.class);
        startActivity(go_home1);
    }
}
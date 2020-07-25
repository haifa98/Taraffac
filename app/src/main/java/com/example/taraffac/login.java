package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class login extends AppCompatActivity {
    ImageView return_main1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        return_main1= findViewById(R.id.image_back);
    }

    public void return_main(View view) {
        onBackPressed();

    }
}
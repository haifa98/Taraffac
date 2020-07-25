package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class register extends AppCompatActivity {
    ImageView return_main2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        return_main2= findViewById(R.id.image_back2);
    }

    public void return_main2(View view) {
        onBackPressed();
    }
}
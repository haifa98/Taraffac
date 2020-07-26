package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class edit_profile extends AppCompatActivity {
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        save = (Button)findViewById(R.id.save_profile);
    }

    public void return_main(View view) {
        onBackPressed();

    }
    public void save(View v){
        Intent save = new Intent(this,profile.class);
        startActivity(save);
    }

}





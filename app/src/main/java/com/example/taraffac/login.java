package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class login extends AppCompatActivity {
    ImageView return_main1;
    Button go_login_to_register;
    EditText pas,usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        return_main1= findViewById(R.id.image_back);
        go_login_to_register=findViewById(R.id.butt_login);

        usr = (EditText) findViewById(R.id.email_login);
        pas = (EditText) findViewById(R.id.password_login);
    }

    public void return_main(View view) {
        onBackPressed();

    }

    public void go_to_home(View view) {
        Intent go_home = new Intent(this,view_speed_bump.class);
        startActivity(go_home);

        String user = usr.getText().toString();
        String pass = pas.getText().toString();

        background bg = new background(this);
        bg.execute(user, pass);
    }
}
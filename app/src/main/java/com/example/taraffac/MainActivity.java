package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {
 Button go_register;
 Button go_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go_register= findViewById(R.id.butt_register);
        go_login= findViewById(R.id.butt_login);

    }

    public void go_to_register(View view) {
        Intent go_register= new Intent(this,register.class);
        startActivity(go_register);
    }

    public void go_to_login(View view) {
            Intent go_login= new Intent(this,login.class);
            startActivity(go_login);
    }
}
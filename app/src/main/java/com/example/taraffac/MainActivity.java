package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    //variables
    Animation topAnim, bottomAnim;

    ImageView image;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        image=(ImageView)findViewById(R.id.logo);
        logo= (TextView)findViewById(R.id.logoText);
        ///design
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // animation
        topAnim= (Animation)AnimationUtils.loadAnimation(this,R.anim.top_animation);
       bottomAnim=(Animation) AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //Hooks
        logo.setAnimation(bottomAnim);
        image.setAnimation(topAnim);



        // logo.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);




      //  setContentView(R.layout.activity_main);
       //// go_register= findViewById(R.id.butt_register);
       // go_login= findViewById(R.id.butt_login);

    }




}
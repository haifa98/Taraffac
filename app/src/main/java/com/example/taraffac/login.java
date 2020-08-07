package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    ImageView return_main1;
    Button go_login_to_register;

    EditText LEmail,LPass;
    FirebaseAuth fAuth;
   // EditText pas,usr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        return_main1= findViewById(R.id.image_back);
        go_login_to_register=findViewById(R.id.butt_login);

        LEmail = findViewById(R.id.email_login);
        LPass = findViewById(R.id.password_login);
        fAuth = FirebaseAuth.getInstance();


        go_login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email = LEmail.getText().toString().trim();
                String Pass = LPass.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    LEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(Pass)){
                    LPass.setError("Password is Required");
                    return;
                }
                if(Pass.length() < 6){
                    LPass.setError("Password Must be 6 Characters");
                    return;
                }

                // authenticate the user

                fAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(login.this, " Logged in Successfully"  , Toast.LENGTH_SHORT).show();
                          Intent go_home1 = new Intent(getApplicationContext(),map.class);
                          startActivity(go_home1);
                      }else {
                          Toast.makeText(login.this, " Error " + task.getException().getMessage()  , Toast.LENGTH_SHORT).show();
                      }
                    }
                });

            }
        });

    //    usr = (EditText) findViewById(R.id.email_login);
     //   pas = (EditText) findViewById(R.id.password_login);
    }

    public void return_main(View view) {
        onBackPressed();

    }

    public void go_to_home(View view) {
        Intent go_home = new Intent(this,map.class);
        startActivity(go_home);


      //  String user = usr.getText().toString();
      //  String pass = pas.getText().toString();

    }
}
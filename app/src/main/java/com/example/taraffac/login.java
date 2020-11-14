package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity { // start class
    ImageView return_main1;
    Button go_login_to_register;

    EditText LEmail,LPass;
    FirebaseAuth fAuth;
    TextView forgotTextLink;
   // EditText pas,usr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {  //onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        return_main1= findViewById(R.id.image_back);
        go_login_to_register=findViewById(R.id.butt_login);

        LEmail = findViewById(R.id.email_login);
        LPass = findViewById(R.id.password_login);
        fAuth = FirebaseAuth.getInstance();
        forgotTextLink = findViewById(R.id.ForgotPassword);


        go_login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Email = LEmail.getText().toString().trim();
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
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(login.this, " Logged in Successfully"  , Toast.LENGTH_SHORT).show();
                          Intent go_home11 = new Intent(getApplicationContext(),map.class);
                          startActivity(go_home11);
                      }else {
                          Toast.makeText(login.this, " Error " + Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                      }
                    }
                });

            }
        });

    //    usr = (EditText) findViewById(R.id.email_login);
     //   pas = (EditText) findViewById(R.id.password_login);

        // forgot password
        forgotTextLink.setOnClickListener(new View.OnClickListener() {// start setOnClickListener
            @Override
            public void onClick(View view) {// start  onclick 1
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder PasswordResetDialog = new AlertDialog.Builder(view.getContext());
                PasswordResetDialog.setTitle("Reset Password ? ");
                PasswordResetDialog.setMessage(" Enter your Email To Received Reset Link.");
                PasswordResetDialog.setView(resetMail);
                PasswordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {// start DialogInterface
                    @Override
                    public void onClick(DialogInterface dialog , int which ) {// start onclick 2
                       //extract the email and send reset link
                        String  mail= resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {//start sendPasswordResetEmail
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, " Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error ! Reset Link is Not Sent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });// end sendPasswordResetEmail




                    }// end onclick 2
                });// end DialogInterface

                PasswordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {//start setNegativeButton
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//start onClick3
                             // close the dialog
                    }//end onClick3
                });// end setNegativeButton
                PasswordResetDialog.create().show();
            }// end onclick 1

        });///setOnClickListener

        } // end onCreate

    public void return_main(View view) { //start return_main
        onBackPressed();

    }// end return_main

    public void go_to_home(View view) { // start go_to_home
        Intent go_home = new Intent(this,map.class);
        startActivity(go_home);


      //  String user = usr.getText().toString();
      //  String pass = pas.getText().toString();

    }//end go_to_home

}// end class
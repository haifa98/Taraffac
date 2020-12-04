package com.example.taraffac;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    //ImageView return_main2;
    Button go_register_to_home1,return_main2,go_login;
   // EditText name,email,pass ;
    TextInputLayout name,email,pass,ConformPass ;
    //long maxid =0;
   // User user;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
  // String Display_Option;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("User") ;



        setContentView(R.layout.activity_register);
        return_main2= findViewById(R.id.butt_login_register);
        go_register_to_home1=findViewById(R.id.butt_register);

        name =  findViewById(R.id.NAME);
        email = findViewById(R.id.email_register);
        pass =  findViewById(R.id.password_register);
        ConformPass=findViewById(R.id.Confirm_password_register);

        go_login= findViewById(R.id.butt_login_register);

        fAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        go_register_to_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              final String Email = String.valueOf(email.getEditText().getText());
                final String Pass = String.valueOf(pass.getEditText().getText());
                final String Name = String.valueOf(name.getEditText().getText());
                final String ConformPass1 = String.valueOf(ConformPass.getEditText().getText());
                final   String   emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
              //  String checkPassword =
                       // "(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                       // "(?=.*[A-Z])" +         //at least 1 upper case letter
                      //  "(?=.*[a-zA-Z])";    //any letter
                       // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                       // "(?=S+$)" +           //no white spaces
                       // ".{6,}" +               //at least 6 characters;

              if(TextUtils.isEmpty(Email)){
                  email.setError("Email is Required");
                  return;
              }
                if(!Email.matches(emailPattern)){
                    email.setError("Invalid Email");
                    return;
                }
              if(TextUtils.isEmpty(Pass)) {
                  pass.setError("Password is Required");
                  return;
              }
                  if(Pass.length() < 6){
                      pass.setError("Password Must be 6 Characters");
                      return;
              }
                if(!ConformPass1.matches(Pass)){
                    ConformPass.setError("Password not matches");
                    return;
                }

              fAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(register.this, " User is registered"  , Toast.LENGTH_SHORT).show();
                          userID = fAuth.getCurrentUser().getUid();
                          DocumentReference documentReferenc = fStore.collection("users").document(userID);
                          Map<String , Object> user = new HashMap<>();
                          user.put("Name",Name);
                          user.put("email" , Email);
                          user.put("password" , Pass);
                        user.put("addingType" ,"Display Option");


                          documentReferenc.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                              }
                          });

                         Intent go_home1 = new Intent(getApplicationContext(),map.class);
                         startActivity(go_home1);

                      }else {
                          Toast.makeText(register.this, " Error " + Objects.requireNonNull(task.getException()).getMessage()  , Toast.LENGTH_SHORT).show();
                      }
                  }
              });

            }
        });


    }
    public void go_to_login(View view) {
        Intent go_login= new Intent(this,login.class);
        startActivity(go_login);
    }

}
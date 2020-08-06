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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    ImageView return_main2;
    Button go_register_to_home1;
    EditText name,email,pass ;
    //long maxid =0;
   // User user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("User") ;

       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() )
                    maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        setContentView(R.layout.activity_register);
        return_main2= findViewById(R.id.image_back2);
        go_register_to_home1=findViewById(R.id.butt_register);

        name = (EditText) findViewById(R.id.NAME);
        email = (EditText) findViewById(R.id.email_register);
        pass = (EditText) findViewById(R.id.password_register);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            Intent go_home1 = new Intent(getApplicationContext(),view_speed_bump.class);
            startActivity(go_home1);
            finish();
        }

       // user= new User();
        go_register_to_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  user.setUsername(name.getText().toString().trim());
                user.setEmail(email.getText().toString().trim());
                user.setPassword(pass.getText().toString().trim());

                myRef.child(String.valueOf(maxid+1)).setValue(user);

               Toast.makeText(register.this, " User is registered"  , Toast.LENGTH_SHORT).show();*/

              String Email = email.getText().toString().trim();
              String Pass = pass.getText().toString().trim();

              if(TextUtils.isEmpty(Email)){
                  email.setError("Email is Required");
                  return;
              }
              if(TextUtils.isEmpty(Pass)){
                  pass.setError("Password is Required");
                  return;
              }
              if(Pass.length() < 6){
                  pass.setError("Password Must be 6 Characters");
                  return;
              }

              fAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(register.this, " User is registered"  , Toast.LENGTH_SHORT).show();
                          Intent go_home1 = new Intent(getApplicationContext(),view_speed_bump.class);
                          startActivity(go_home1);

                      }else {
                          Toast.makeText(register.this, " Error " + task.getException().getMessage()  , Toast.LENGTH_SHORT).show();
                      }
                  }
              });
                /*Intent go_home1 = new Intent(getApplicationContext(),view_speed_bump.class);
                startActivity(go_home1);*/
            }
        });
    }

    public void return_main2(View view) {
        onBackPressed();
    }

}
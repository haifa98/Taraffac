package com.example.taraffac;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;
import java.util.jar.Attributes;

public class profile extends AppCompatActivity {
    Button edit;
    Button log;
    Button del;
    TextView fullName , Email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
String usedId;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit = findViewById(R.id.edit_profile);
        log = findViewById(R.id.but_log_out5);
        del = findViewById(R.id.delete_profile);

        fullName =findViewById(R.id.name_pro);
        Email= findViewById(R.id.email_pro);
        fAuth = FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();
        usedId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //retrev data ///
        final DocumentReference documentReference =fStore.collection("users").document(usedId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {//start method
                assert documentSnapshot != null;
                Email.setText(documentSnapshot.getString("email"));
                fullName.setText(documentSnapshot.getString("Name"));
            }
        });//end method


    }
    public void return_main(View view) {
        onBackPressed();
    }

    public void edit(View v){
        Intent profile = new Intent(this,edit_profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }

    public void del(View v){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Are you sure you want to delete your account ?");
        b.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.show();
    }
}


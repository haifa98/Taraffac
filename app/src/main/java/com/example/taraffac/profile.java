package com.example.taraffac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

//import android.widget.ImageView;
//import java.util.jar.Attributes;

public class profile extends AppCompatActivity {
    Button edit;
    Button log;
    Button del;
    ImageView imageProfil1;
    TextInputLayout fullName , Email, type;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference  storageReference;
    FirebaseUser  firebaseUser;

    String usedId;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit = findViewById(R.id.edit_profile);
       // log = findViewById(R.id.but_log_out5);
        del = findViewById(R.id.delete_profile);
        imageProfil1= findViewById(R.id.user_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        type=findViewById(R.id.adding_pro);

        fullName =findViewById(R.id.name_pro);
        Email= findViewById(R.id.email_pro);
        fStore =FirebaseFirestore.getInstance();
        //////////////////////

      //  usedId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //retrev data ///
        final DocumentReference documentReference =fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {//start method
                assert documentSnapshot != null;
                Email.getEditText().setText(documentSnapshot.getString("email"));
                fullName.getEditText().setText(documentSnapshot.getString("Name"));
                type.getEditText().setText(documentSnapshot.getString("addingType"));
                //setText(documentSnapshot.getString("addingType"));// type.setEditText(String.valueOf(documentSnapshot.getString("addingType"));
               // type.setText(String.valueOf(documentSnapshot.getString("addingType"));


            }
        });//end method
        //
        edit.setOnClickListener(new View.OnClickListener() {// start setOnClickListener
            @Override
            public void onClick(View v) {
                // open Gallery
                //  Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // startActivityForResult(openGalleryIntent, 1000 );
                Intent i = new Intent(v.getContext(),edit_profile.class);
                i.putExtra("fullName",String.valueOf(fullName.getEditText().getText()));//String.valueOf(LEmail.getEditText().getText())   fullName.getText().toString()
                i.putExtra("email",String.valueOf(Email.getEditText().getText()));//Email.getText().toString()
                i.putExtra("addingType",String.valueOf(type.getEditText().getText()));//type.getText().toString()


                startActivity(i);
            }
        });//End setOnClickListener
        //

        // start delet Account
        del.setOnClickListener(new View.OnClickListener() { // start setOnClickListener
            @Override
            public void onClick(View v) {// start onClick
                AlertDialog.Builder b = new AlertDialog.Builder(profile.this);
                b.setTitle("Are you sure you want to delete your account ?");
                b.setPositiveButton("yes", new DialogInterface.OnClickListener() { // start setPositiveButton
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   Toast.makeText(profile.this, "Account Deleted" , Toast.LENGTH_LONG).show();
                                   Intent intent= new Intent(profile.this,login.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);

                               }else {
                                   Toast.makeText(profile.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_LONG).show();

                               }
                            }
                        });


                    }
                });// end setPositiveButton
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                                  dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = b.create();
                alertDialog.show();

            }//end onClick
        }); // end setOnClickListener

    }//end Oncrate

    public void return_main(View view) {
        onBackPressed();

    }


    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }


    public void back_profile1(View view) {
        Intent go_map2= new Intent(this,map.class);
        startActivity(go_map2);
    }
}


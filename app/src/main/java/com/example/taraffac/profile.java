package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;
//import java.util.jar.Attributes;

public class profile extends AppCompatActivity {
    Button edit;
    Button log;
    Button del;
    Button  ChangImage1;
    ImageView imageProfil1;
   // Button edit_profile;
    TextView fullName , Email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference  storageReference;
String usedId;
/// amjad imge
  // ImageView imageprofil;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit = findViewById(R.id.edit_profile);
        log = findViewById(R.id.but_log_out5);
        del = findViewById(R.id.delete_profile);
        imageProfil1= findViewById(R.id.imageProfil1);
        ChangImage1= findViewById(R.id.ChangImage1);
        storageReference = FirebaseStorage.getInstance().getReference();

        //
       // imageprofil= findViewById(R.id.imageProfil);
        //
        fullName =findViewById(R.id.name_pro);
        Email= findViewById(R.id.email_pro);
        fAuth = FirebaseAuth.getInstance();


        fStore =FirebaseFirestore.getInstance();
      //  usedId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //retrev data ///
        final DocumentReference documentReference =fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {//start method
                assert documentSnapshot != null;
                Email.setText(documentSnapshot.getString("email"));
                fullName.setText(documentSnapshot.getString("Name"));
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
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("email",Email.getText().toString());

                startActivity(i);
            }
        });//End setOnClickListener
        //
        // start set ProfileImage1
        StorageReference profileRef = storageReference.child("user/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()  +"/ Profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfil1);
            }
        });
        ChangImage1.setOnClickListener(new View.OnClickListener() {// start setOnClickListener
            @Override
            public void onClick(View v) {
                // open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000 );

            }
        });//End setOnClickListener
        // end set ProfileImage1

    }//end Oncrate
    // set ProfileImage1
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// start method
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = Objects.requireNonNull(data).getData();
                // imageprofil.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }// end method

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImageToFirebase(Uri imageUri) {// start method
        // uplood image to firebase storage
        final StorageReference fileRef = storageReference.child("user/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()  +"/ Profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Toast.makeText(edit_profile.this, " Image Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //ImageView imageProfilEdit = null;
                        Picasso.get().load(uri).into(imageProfil1);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }// end  method
    // end set ProfileImage1
    public void return_main(View view) {
        onBackPressed();
    }

  //  public void edit(View v){
    //    Intent profile = new Intent(this,edit_profile.class);
    //    startActivity(profile);
    //}
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


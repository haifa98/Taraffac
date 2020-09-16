package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class edit_profile extends AppCompatActivity {
    Button saveProvil, ChangImage1;
    ImageView imageprofil;
    StorageReference  storageReference;
    FirebaseAuth fAuth;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //
        saveProvil = findViewById(R.id.save_profile);
        imageprofil= findViewById(R.id.imageProfilEdit);
        ChangImage1= findViewById(R.id.ChangImage);
        storageReference = FirebaseStorage.getInstance().getReference();
        //
        fAuth = FirebaseAuth.getInstance();
      // set ProfileImage1
        StorageReference profileRef = storageReference.child("user/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()  +"/ Profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageprofil);
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
        // set ProfileImage1

    }// end onCreate
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
                         Picasso.get().load(uri).into(imageprofil);

                     }
                 });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(edit_profile.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }// end  method
    // end set ProfileImage1

    public void return_main(View view) {
        onBackPressed();

    }
    public void save(View v){
        Intent saveProvil = new Intent(this,profile.class);
       startActivity(saveProvil);
}//

}





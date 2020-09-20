package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class edit_profile extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button saveProvil, ChangImage1;
    ImageView imageprofil;
    StorageReference  storageReference;
    FirebaseFirestore fStore;
   // TextView fullNameProfileEdit , EmailProfileEdit;
    FirebaseUser user;
    //String usedId;


    FirebaseAuth fAuth;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //
      //  fullNameProfileEdit = findViewById(R.id.name_edit);
        //  EmailProfileEdit = findViewById(R.id.email_edit);
        saveProvil = findViewById(R.id.save_profile);
        imageprofil= findViewById(R.id.imageProfilEdit);
        ChangImage1= findViewById(R.id.ChangImage);
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        //
        fStore= FirebaseFirestore.getInstance();

        // edit profile
        Intent data = getIntent();
        String  fullName = data.getStringExtra("fullName");
        String  email  = data.getStringExtra("email");
        Log.d(TAG, "onCreate: " + fullName + " " + email);
        final EditText ProfileEditFullName , ProfileEditEmail;
        ProfileEditFullName = findViewById(R.id.name_edit);
                ProfileEditEmail = findViewById(R.id.email_edit);
        ProfileEditFullName.setText(fullName);
        ProfileEditEmail.setText(email);
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
       // هنا اتوقع تغيير الصوره اتاكد منه شوي
        imageprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(edit_profile.this, "profile Image clicked", Toast.LENGTH_SHORT).show();
            }
        });
        // وقفت هنا
        // start edit profile
        //نبي نخزنها في الداتا بيس نسوي ابديت يعني
        saveProvil.setOnClickListener(new View.OnClickListener() {// start method
            @Override
            public void onClick(View v) { // start onclick
                if (ProfileEditFullName.getText().toString().isEmpty() || ProfileEditEmail.getText().toString().isEmpty()) {
                    Toast.makeText(edit_profile.this, "one or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;//user
                }
                final String email = ProfileEditEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() { // start OnSuccessListener
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String , Object> edited = new HashMap<>();
                       edited.put("email ",email);
                        edited.put("Name", ProfileEditFullName.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(edit_profile.this, "profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),profile.class) );
                                finish();
                            }
                        });

                        Toast.makeText(edit_profile.this, " Email Is Changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }// end onclick

        });// end method setOnClickListener

        // end edit profile

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
   // public void save(View v){
     //   Intent saveProvil = new Intent(this,profile.class);
     //  startActivity(saveProvil);
//}//

}





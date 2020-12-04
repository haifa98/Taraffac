package com.example.taraffac;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
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
    Button saveProvil;
    ImageView imageprofil;
    StorageReference  storageReference;
    FirebaseFirestore fStore;
    FirebaseUser user;
 RadioButton  Radiodisplay, RadioVoice , RadioType;
     //TextView itFullName , ProfileEditEmail;
    TextInputLayout ProfileEditFullName , ProfileEditEmail;
    RadioGroup RadioGroupTypeUpdate;

    public String voiceType ="Voice command";

    FirebaseAuth fAuth;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        saveProvil = findViewById(R.id.save_profile);
       imageprofil= findViewById(R.id.imageProfilEdit);
        Radiodisplay = findViewById(R.id.displayEdit);
        RadioVoice= findViewById(R.id.voiceEdit);
        RadioGroupTypeUpdate= findViewById(R.id.update_Adding_type);
        ProfileEditFullName = findViewById(R.id.name_edit);
        ProfileEditEmail = findViewById(R.id.email_edit);
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore= FirebaseFirestore.getInstance();

        // edit profile
        Intent data = getIntent();
        String  fullName = data.getStringExtra("fullName");
        String  email  = data.getStringExtra("email");
        String  type = data.getStringExtra("addingType");
        Log.d(TAG, "onCreate: " + fullName + " " + email + type);

        ProfileEditFullName.getEditText().setText(fullName);
        ProfileEditEmail.getEditText().setText(email);

        if (type.equals(voiceType) ){
            RadioVoice.setChecked(true);
        }else{
            Radiodisplay.setChecked(true);}

        ///////////////////////////// method ProfileImage////////////////////////////////
       // start set ProfileImage
       // Change ProfileImage
        StorageReference profileRef = storageReference.child("user/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()  +"/ Profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageprofil);
            }
        });
        imageprofil.setOnClickListener(new View.OnClickListener() { // start setOnClickListener
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000 );


            }
        }); // end setOnClickListener
        ///////////////////////////// end method ProfileImage ////////////////////////////////


        ///////////////////// ///////////////// start edit profile /////////////////////////////////////////////////////////////////////////////
        //Update Profile, save changed in firebase
        saveProvil.setOnClickListener(new View.OnClickListener() {// start method
            @Override
            public void onClick(View v) { // start onclick
                final String TypeText;
                TypeText=checkType();
                if (String.valueOf(ProfileEditFullName.getEditText().getText()).isEmpty() || String.valueOf(ProfileEditEmail.getEditText().getText()).isEmpty()|| TypeText.isEmpty()) {//ProfileEditFullName.getText().toString()   ProfileEditEmail.getText().toString()
                    Toast.makeText(edit_profile.this, "one or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;//user
                }

                user.updateEmail(String.valueOf(ProfileEditEmail.getEditText().getText())).addOnSuccessListener(new OnSuccessListener<Void>() { // start OnSuccessListener
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
                        Map<String , Object> edited = new HashMap<>();
                       edited.put("email",String.valueOf(ProfileEditEmail.getEditText().getText()));
                        edited.put("Name",String.valueOf(ProfileEditFullName.getEditText().getText()));
                        edited.put("addingType",TypeText);

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(edit_profile.this, "profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),profile.class) );
                                finish();
                            }
                        });

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
// Update adding type in profile
    public String checkType(){
        int radioID = RadioGroupTypeUpdate.getCheckedRadioButtonId();
        RadioType = findViewById(radioID);
        String CheckType = (String) RadioType.getText();
        return CheckType ;


    }
    ///////////////////// ///////////////// end edit profile /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////// method ProfileImage////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// start method
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = Objects.requireNonNull(data).getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }// end method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImageToFirebase(Uri imageUri) {// start method
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("user/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()  +"/ Profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageprofil);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }// end  method ProfileImage
    ///////////////////////////// method ProfileImage////////////////////////////////



    public void return_main(View view) {
        onBackPressed();

    }

}





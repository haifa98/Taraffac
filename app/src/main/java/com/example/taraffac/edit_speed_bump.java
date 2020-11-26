package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_speed_bump extends AppCompatActivity {
    Button profile;
    Button log, But_Update_bump, But_Delete_bump;
    String TypeValue,SizeValue;
    RadioButton RadioType1, RadioType2,RadioType3,RadioSize1,RadioSize2,RadioSize3, RadioType,RadioSize;
    RadioGroup RadioGroupTypeUpdate,RadioGroupSizeUpdate;
    public String Cushion="Cushion";
    public String Table="Table";
    public String Small="Small";

    public String Midium="Midium";
    DatabaseReference dataBympUpdate;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_speed_bump);
        profile = (Button)findViewById(R.id.but_Ppofile4);
        log = (Button)findViewById(R.id.but_log_out4);
        But_Update_bump= findViewById(R.id.update_edite_bump);
        But_Delete_bump= findViewById(R.id.update_delete_bump);
        RadioType1=findViewById(R.id.update_cushion2);
        RadioType2= findViewById(R.id.update_table2);
        RadioType3= findViewById(R.id.update_hump2);
        RadioSize1= findViewById(R.id.update_small);
        RadioSize2= findViewById(R.id.update_mid2);
        RadioSize3= findViewById(R.id.update_large2);

        RadioGroupTypeUpdate= findViewById(R.id.update_type);
        RadioGroupSizeUpdate= findViewById(R.id.update_size);
        dataBympUpdate = FirebaseDatabase.getInstance().getReference("SpeedBump").child("test");

        // show the retrieve data
        TypeValue= getIntent().getExtras().getString("type");
        SizeValue =getIntent().getExtras().getString("size");

        if (TypeValue.equals(Cushion) ) {

            RadioType1.setChecked(true);
        }else if (TypeValue.equals(Table) ){
            RadioType2.setChecked(true);
        }else{
            RadioType3.setChecked(true);

        }
        if (SizeValue.equals(Small) ) {
            RadioSize1.setChecked(true);
        }else if (SizeValue.equals(Midium) ){
            RadioSize2.setChecked(true);
        }else{
            RadioSize3.setChecked(true);

        }       //end  show the retrieve data
        // Delete Speed Bump
        But_Delete_bump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBympUpdate.removeValue();
            }
        });

    }// end method  ;

    public String checkType(){
        int radioID = RadioGroupTypeUpdate.getCheckedRadioButtonId();
        RadioType = findViewById(radioID);
        String CheckType = (String) RadioType.getText();
        return CheckType ;
    }

    public String checkSize(){
        int radioID = RadioGroupSizeUpdate.getCheckedRadioButtonId();
        RadioSize = findViewById(radioID);
        String CheckSize = (String) RadioSize.getText();
        return CheckSize ;
    }

    public void Update_Bump(View view) {// Update Speed Bump
        String TypeText,SizeText;
        TypeText=checkType();
        SizeText=checkSize();

        if(!TypeText.isEmpty() & !SizeText.isEmpty() ) {

// add in database

            String id = dataBympUpdate.push().getKey();

            SpeedBump bump = new SpeedBump ( latitude, longitude, TypeText, SizeText);
            dataBympUpdate.setValue(bump);

            Toast t = Toast.makeText(this, " The Update was successful", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();
        }else {
            Toast t = Toast.makeText(this, " The Update was failed", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();


        }
        Intent log = new Intent(this,map.class);
        startActivity(log);


    }// Update Speed Bump
















    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }


}


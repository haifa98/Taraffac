package com.example.taraffac;

import android.app.Activity;
//import android.content.Intent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class report extends Activity {
    AutoCompleteTextView autoCompleteTextView;
    private Button button;
    RadioButton rd1,rd2,rd3,rd4,rd_reason;
    RadioGroup radioGroup_reason;
    DatabaseReference sb;
    String coord;
   // Intent data = getIntent();
    double lat, longitude;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        button = findViewById(R.id.reportbtn);
        rd1= findViewById(R.id.rd1);
        rd2= findViewById(R.id.rd2);
        rd3= findViewById(R.id.rd3);
        rd4= findViewById(R.id.rd4);
       final SpeedBump bump= getIntent().getParcelableExtra("bump");

        radioGroup_reason= findViewById(R.id.report_reason);
        //interface dropdon list
        autoCompleteTextView=findViewById(R.id.autoCompleteText);
        String [] option= {"Egypt" , "England" , "France" , "aa" ,"bb" , "tt" , "bv" , "dz", "cc" , "ty" , "pp"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.option_item, option);
        //to make default value
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString() , false);
        autoCompleteTextView.setAdapter(arrayAdapter);


        /////////////////////

        //sb = FirebaseDatabase.getInstance().getReference().child("SpeedBump");
       // TypeValue= getIntent().getExtras().getString("type");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = Double.parseDouble(getIntent().getStringExtra("latitude"));
                longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
               // lat= bump.latitude;
               // longitude= bump.longitude;

                coord = ""+lat+ ","+longitude;

                senEmail();
            }
        });


      /*  but_close_re = findViewById(R.id.but_close_re);
        but_close_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_report = findViewById(R.id.btn_report);
       /* btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), report_msg.class);
                startActivity(i);
            }
        });*/


    }

    private void senEmail() {
        String mEmail = "mgoodh.18@gmail.com";
        String mSubject = "Complaint about speed bump";
        String mReason = (String) rd_reason.getText();
        String mContent= "We are sending this e-mail to report the speed bump located at this coordinates "+coord +"\n"+"and the reason for reporting is the following:  "+mReason+"\n"+"Thank you.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail,mSubject ,mContent );

        javaMailAPI.execute();
    }

    public void checkReason(View view) {
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
       // String reason = (String) rd_reason.getText();
    }

    public void show_report(View v){
        Toast to =  Toast.makeText(this, " The Report Was Sent Successfully"  , Toast.LENGTH_SHORT);
        to.setGravity(Gravity.TOP,0, 90);
        to.show();
    }

}
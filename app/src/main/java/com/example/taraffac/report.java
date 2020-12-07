package com.example.taraffac;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class report extends Activity {
    AutoCompleteTextView autoCompleteTextView;
    private Button button;
    RadioButton rd1,rd2,rd3,rd4,rd_reason,rd5;
    RadioGroup radioGroup_reason;
    String coord;
    public String Alriyadh="Riyadh";
    public String Makkah="Makkah";
    public String Almadinah="Almadinah";
    public String Eastern_Region="Eastern Region";
    public String Alqassim="Alqassim";
    public String Jeddah="Jeddah";
    public String Albaha="Albaha";
    public String Jazan="Jazan";
    public String Tabuk="Tabuk";
    public String Hail="Hail";
    public String Alahsa="Alahsa";
    public String Altaif="Altaif";
    public String Najran="Najran";
    public String Asir="Asir";
    public String Northern_Border="Northern Border";
    public String LocationEmail;
    double lat, longitude;
    public String selection;
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
        rd5=findViewById(R.id.rd5);
        autoCompleteTextView=findViewById(R.id.autoCompleteText);
        radioGroup_reason= findViewById(R.id.report_reason);
       final SpeedBump bump= getIntent().getParcelableExtra("bump");
        //interface drop down list
        // Create array Contains the locations in the drop down list
        String [] option= {"Riyadh","Makkah","Almadinah","Eastern Region","Asir" ,"Alqassim" , "Jeddah" , "Albaha" , "Jazan" ,"Tabuk" , "Hail" , "Alahsa" , "Altaif", "Najran" , "Northern Border"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.option_item, option);
        //To make default value in the drop down list
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString() , false);
        // view Contains the locations in the drop down list
        autoCompleteTextView.setAdapter(arrayAdapter);

        //  find the  item in a AutoCompleteTextView filled with Array and stored in variable
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){//start method
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                //Store the Item in a AutoCompleteTextView filled
                selection = (String) parent.getItemAtPosition(position);
                // Check the item and send email
                if(selection.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Select The Location", Toast.LENGTH_SHORT).show();

                } else if (selection.equals(Alriyadh)) {
                    LocationEmail = "mgoodh.18@gmail.com";
                }else if(selection.equals(Makkah)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(selection.equals(Almadinah)) {
                    LocationEmail = "mgoodh.18@gmail.com";

            }  else if(selection.equals(Eastern_Region)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Asir)) {
                    LocationEmail = "mgoodh.18@gmail.com";

                } else if(selection.equals(Alqassim)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Jeddah)) {
                    LocationEmail = "mgoodh.18@gmail.com";
                }else if(selection.equals(Albaha)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Jazan)) {
                    LocationEmail = "mgoodh.18@gmail.com";
                }else if(selection.equals(Tabuk)){
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Hail)) {
                    LocationEmail = "mgoodh.18@gmail.com";
                }else if(selection.equals(Alahsa)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Altaif)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";
                }else if(selection.equals(Najran)) {
                    LocationEmail = "amjad.nasser.al@gmail.com";

                } else {
                    LocationEmail = "amjad.nasser.al@gmail.com";

                }
            }

        });//end method



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lat = getIntent().getExtras().getDouble("latitude");
                longitude = getIntent().getExtras().getDouble("longitude");


                coord = ""+lat+ ","+longitude;

                senEmail();
                onBackPressed();

            }
        });
       // Toast to =  Toast.makeText(this, " The Report Was Sent Successfully"  , Toast.LENGTH_SHORT);

    }

      // Send Email  to JavaMailAPI class
    private void senEmail() {
        String mEmail = LocationEmail;
        String mSubject = "Complaint about speed bump";
        String mReason = (String) rd_reason.getText();
        String mContent= "We are sending this e-mail to report the speed bump located at this coordinates "+coord +"\n"+"and the reason for reporting is the following:  "+mReason+"\n"+"Thank you.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail,mSubject ,mContent );

        javaMailAPI.execute();
    }

    public void checkReason(View view) {
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
    }


    public void show_report(View v){
        Toast to =  Toast.makeText(this, " The Report Was Sent Successfully"  , Toast.LENGTH_SHORT);
        to.setGravity(Gravity.TOP,0, 90);
        to.show();
    }

}
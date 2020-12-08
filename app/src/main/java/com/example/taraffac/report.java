package com.example.taraffac;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class report extends Activity {
    AutoCompleteTextView autoCompleteTextView;
    private Button button , returm_map;
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
    String userType;
    static final String TAG = "TTS";

    TextToSpeech mTts;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        button = findViewById(R.id.reportbtn);
        returm_map = findViewById(R.id.returnMap);
        rd1= findViewById(R.id.rd1);
        rd2= findViewById(R.id.rd2);
        rd3= findViewById(R.id.rd3);
        rd4= findViewById(R.id.rd4);
        rd5=findViewById(R.id.rd5);
        autoCompleteTextView=findViewById(R.id.autoCompleteText);
        radioGroup_reason= findViewById(R.id.report_reason);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userType = extras.getString("userType"); }
        //interface drop down list
        // Create array Contains the locations in the drop down list
        String [] option= {"Riyadh","Makkah","Almadinah","Eastern Region","Asir" ,"Alqassim" , "Jeddah" , "Albaha" , "Jazan" ,"Tabuk" , "Hail" , "Alahsa" , "Altaif", "Najran" , "Northern Border"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.option_item, option);
        //To make default value in the drop down list
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString() , true);
        // view Contains the locations in the drop down list
        autoCompleteTextView.setAdapter(arrayAdapter);
        //  find the  item in a AutoCompleteTextView filled with Array and stored in variable
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){//start method
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                //Store the Item in a AutoCompleteTextView filled
                selection = (String) parent.getItemAtPosition(position);
                // retrieve values from map

           //     userType = getIntent().getExtras().getString("userType");
                Toast.makeText(report.this, userType, Toast.LENGTH_SHORT).show();

                // Check the item and send email
                if (selection.equals(Alriyadh)) {
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
                    LocationEmail = "amjad.nasser.al@gmail.com"; } }

        });//end method

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Execute(); }});
       // Toast to =  Toast.makeText(this, " The Report Was Sent Successfully"  , Toast.LENGTH_SHORT);
        returm_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_map3(); }});

        if (userType!= null) {
            if(userType.equals("Voice command")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    read();
                }
            } }
    }

    public void Execute(){
        lat = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        coord = ""+lat+ ","+longitude;
        senEmail();
        Toast.makeText(this, " The Report Was Sent Successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

      // Send Email  to JavaMailAPI class
    private void senEmail() {
        String mReason = ckeck_reason();

        String mEmail = LocationEmail;
        String mSubject = "Complaint about speed bump";

        String mContent= "We are sending this e-mail to report the speed bump located at this coordinates "+coord +"\n"+"and the reason for reporting is the following:  "+mReason+"\n"+"Thank you.";

        if(mEmail == null ){
            JavaMailAPI javaMailAPI = new JavaMailAPI(this, "amjad.nasser.al@gmail.com",mSubject ,mContent );

            javaMailAPI.execute();
        }else {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail,mSubject ,mContent );

        javaMailAPI.execute();}
    }

    public void checkReason(View view) {
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
    }
    public String ckeck_reason(){
        String t=(String) rd4.getText(); ;
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
        if(rd_reason!=null) { t = (String) rd_reason.getText(); }
        return t ;
    }

    public void back_map3(){
        onBackPressed();
      //  Intent go_map1= new Intent(this,map.class);
    //   startActivity(go_map1);
    }


    // this method convert text to speech
    void speak(String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.v(TAG, "Speak new API");
            Bundle bundle = new Bundle();
            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
            mTts.speak(s, TextToSpeech.QUEUE_FLUSH, bundle, null);
        } else {
            Log.v(TAG, "Speak old API");
            HashMap<String, String> param = new HashMap<>();
            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
            mTts.speak(s, TextToSpeech.QUEUE_FLUSH, param);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to shutdown tts!
        if (mTts != null) {
            Log.v(TAG,"onDestroy: shutdown TTS");
            mTts.stop();
            mTts.shutdown();
        }
    }

    // the result from user voice become as array list and this method split the values for each element in arraylist
    public static String[] spliteArray(ArrayList<String> array) {
        String[] tmp= new String[20];
        for(String line : array) {
            tmp = line.split("\\s+"); //split on one or more spaces
        }
        return tmp;
    }

    // this is the main method for voice command
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void read(){

        final String emailid1;
        emailid1 = "select your area "+ "select the reason by number  " ;
               // "and if your  city is not riyadh please choose your city ";

        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "This language is not supported", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v("TTS","onInit succeeded");
                        speak(emailid1); }
                } else { Toast.makeText(getApplicationContext(), "Initialization failed", Toast.LENGTH_SHORT).show(); }}
        });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {getSpeechInput(); }}, 6000); //  1000 = 1 sec
    }

    // speech to text
    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }
    // this function get the result from the above function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            back_map3();
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result == null) {
                        back_map3();
                    }
                    assert result != null;
                    String[] newA = spliteArray(result);
                    String a = testtype(newA);
                    if(a.equals("cancel")){
                        back_map3();
                    }else {
                    Execute(); }

                    break;} }}
    // this method check radio buttons
    public String testtype(String[] x){
        String state = null;
        for (String n: x) {
            if (n.equals("cancel")) {
                state="cancel";

            }
            if (n.equals("1")) {
              //  ((RadioButton) radioGroup_reason.getChildAt(5)).setChecked(true);
                rd5.setChecked(true);
            }
            if (n.equals("2")) {
                rd1.setChecked(true);
              //  ((RadioButton) radioGroup_reason.getChildAt(0)).setChecked(true);
            }
            if (n.equals("3")) {
                rd2.setChecked(true);
              //  ((RadioButton) radioGroup_reason.getChildAt(1)).setChecked(true);
            }
            if (n.equals("4")) {
                rd3.setChecked(true);
             //   ((RadioButton) radioGroup_reason.getChildAt(2)).setChecked(true);
            }
            if (n.equals("5")) {
                rd4.setChecked(true);
               // ((RadioButton) radioGroup_reason.getChildAt(3)).setChecked(true);
            }
            // Check the item and send email
            if (n.equals(Alriyadh)) {
                LocationEmail = "mgoodh.18@gmail.com";
            }else if(n.equals(Makkah)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Almadinah)) {
                LocationEmail = "mgoodh.18@gmail.com";
            }  else if(n.equals(Eastern_Region)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Asir)) {
                LocationEmail = "mgoodh.18@gmail.com";
            } else if(n.equals(Alqassim)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Jeddah)) {
                LocationEmail = "mgoodh.18@gmail.com";
            }else if(n.equals(Albaha)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Jazan)) {//
                LocationEmail = "mgoodh.18@gmail.com ";
            }else if(n.equals(Tabuk)){
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals("city")){
                LocationEmail = "haifa98125@gmail.com";
            }else if(n.equals(Hail)) {
                LocationEmail = "mgoodh.18@gmail.com";
            }else if(n.equals(Alahsa)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Altaif)) {
                LocationEmail = "amjad.nasser.al@gmail.com";
            }else if(n.equals(Najran)) {
                LocationEmail = "amjad.nasser.al@gmail.com";

            } else {
                LocationEmail = "amjad.nasser.al@gmail.com"; }

            }
    return state;}


}
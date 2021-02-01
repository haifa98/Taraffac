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
        // retrieve user adding type from map
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

                // Check the city and send email
                if (selection.equals(Alriyadh)) {
                    LocationEmail =   "haifa98125@gmail.com";
                   // LocationEmail = "eSupport@alriyadh.gov.sa";
                }else if(selection.equals(Makkah)) {
                    LocationEmail = "info@holymakkah.gov.sa";
            }else if(selection.equals(Almadinah)) {
                    LocationEmail = "webmasher@amana-md.gov.sa";

            }  else if(selection.equals(Eastern_Region)) {
                    LocationEmail = "HELP@EAMANA.GOV.SA";
                }else if(selection.equals(Asir)) {
                    LocationEmail = "as@ars.gov.sa";

                } else if(selection.equals(Alqassim)) {
                    LocationEmail = "info@qassim.gov.sa";
                }else if(selection.equals(Jeddah)) {
                    LocationEmail = "csc1@jeddah.gov.sa";
                }else if(selection.equals(Albaha)) {
                    LocationEmail = "baha@mob.gov.sa";
                }else if(selection.equals(Jazan)) {
                    LocationEmail = "info@jazan.sa";
                }else if(selection.equals(Tabuk)){
                    LocationEmail = "info@tabukmgov.sa";
                }else if(selection.equals(Hail)) {
                    LocationEmail = "mayor@amanathail.gov.sa";
                }else if(selection.equals(Alahsa)) {
                    LocationEmail = "info@alhasa.gov.sa";
                }else if(selection.equals(Altaif)) {
                    LocationEmail = "admin@taifcity.gov.sa";
                }else if(selection.equals(Najran)) {
                    LocationEmail = "Link@najran.gov.sa";

                } else {
                    LocationEmail = "haifa98125@gmail.com"; //"it@arar-mu.gov.sa";
                } }

        });//end method
// if user press on SEND button, the Execute() will be called
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Execute(); }});
        returm_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_map3(); }});
// check the adding type, if the adding type is Voice command function read() will be called
        if (userType!= null) {
            if(userType.equals("Voice command")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    read();
                }
            } }
    }

    public void Execute(){
        //get lat & long from map
        lat = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        coord = ""+lat+ ","+longitude;
        // go th method senEmail() to get the reason and get the city
        senEmail();
        Toast.makeText(this, " The Report Was Sent Successfully", Toast.LENGTH_SHORT).show();
        Intent go_map1= new Intent(this,map.class);
        go_map1.putExtra("state", true);
        startActivity(go_map1);
    }
      // Send Email  to JavaMailAPI class
    private void senEmail() {
        // retrieve reason value
        String mReason = ckeck_reason();
        String mEmail = LocationEmail;
        String mSubject = "Complaint about speed bump";

        String mContent= "We are sending this e-mail to report the speed bump located at this coordinates "+coord +"\n"+"and the reason for reporting is the following:  "+mReason+"\n"+"Thank you.";
      // if user didn't choose a city, this will be the default email
        if(mEmail == null ){
            JavaMailAPI javaMailAPI = new JavaMailAPI(this, "haifa98125@gmail.com",mSubject ,mContent );
            javaMailAPI.execute(); // go to class javaMailAPI to send the email
        }else {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail,mSubject ,mContent );
        javaMailAPI.execute();} // go to class javaMailAPI to send the email
    }// check the reason by buttons
    public void checkReason(View view) {
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
    }// check the reason by buttons
    public String ckeck_reason(){
        String t=(String) rd4.getText();// set a default value
        int radioID = radioGroup_reason.getCheckedRadioButtonId();
        rd_reason = findViewById(radioID);
        if(rd_reason!=null) { t = (String) rd_reason.getText(); }
        return t ;
    }

    public void back_map3(){
        Intent go_map1= new Intent(this,map.class);
        go_map1.putExtra("state", true);
        startActivity(go_map1);
    }
    // this method convert text to speech
    void speak(String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // check the android version
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
    @Override  // shutdown the text to speech
    protected void onDestroy() {
        super.onDestroy();
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
        // "read" method is the main method for voice command
        // first will enable the text to speech then send the text to method "speak" to read the text for the user
        // then call method "getSpeechInput" to start speech recognition , after the user speech method "onActivityresult"
        // this method will get the user speech.
        final String emailid1;
        emailid1 = "select the reason by number  "
               + "and if your city is not riyadh please choose your city ";

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
                        // send emailid1 to method speak to convert it to speech
                        speak(emailid1); }
                } else { Toast.makeText(getApplicationContext(), "Initialization failed", Toast.LENGTH_SHORT).show(); }}
        });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
// start speech recognition after 6 sec
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {getSpeechInput(); }}, 6000); //  1000 = 1 sec
    }

    // speech to text
    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
// start the speech recognition
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
        if(data==null){  // if user didn't say anything, the user will be returned to map
            back_map3();
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result == null) { // if user didn't say anything, the user will be returned to map
                        back_map3();
                    }
                    assert result != null;
                    String[] newA = spliteArray(result);// split the result to words
                    // send words to check what user choose
                    String a = theResult(newA);
                    if(a.equals("cancel")){ // if user say cancel, will be returned to map
                        back_map3(); }else {
                    Execute(); }

                    break;} }}
    // this method check radio buttons
    public String theResult(String[] x){
        String state = "report";
        for (String n: x) {
            if (n.equals("cancel")) {
                state="cancel";
            }
            if (n.equals("1")) {
                rd5.setChecked(true);
            }
            if (n.equals("2")) {
                rd1.setChecked(true);
            }
            if (n.equals("3")) {
                rd2.setChecked(true);
            }
            if (n.equals("4")) {
                rd3.setChecked(true);
            }
            // Check the item and send email
            if (n.equals(Alriyadh)) {
                LocationEmail = "haifa98125@gmail.com";
               // LocationEmail = "eSupport@alriyadh.gov.sa";
            }else if(n.equals(Makkah)) {
                LocationEmail = "info@holymakkah.gov.sa";
            }else if(n.equals(Almadinah)) {
                LocationEmail = "webmasher@amana-md.gov.sa";
            }  else if(n.equals(Eastern_Region)) {
                LocationEmail = "HELP@EAMANA.GOV.SA";
            }else if(n.equals(Asir)) {
                LocationEmail = "as@ars.gov.sa";
            } else if(n.equals(Alqassim)) {
                LocationEmail = "info@qassim.gov.sa";
            }else if(n.equals(Jeddah)) {
                LocationEmail = "csc1@jeddah.gov.sa";
            }else if(n.equals(Albaha)) {
                LocationEmail = "baha@mob.gov.sa";
            }else if(n.equals(Jazan)) {//
                LocationEmail = "info@jazan.sa";
            }else if(n.equals(Tabuk)){
                LocationEmail = "info@tabukmgov.sa";
            }else if(n.equals(Hail)) {
                LocationEmail = "mayor@amanathail.gov.sa";
            }else if(n.equals(Alahsa)) {
                LocationEmail = "info@alhasa.gov.sa";
            }else if(n.equals(Altaif)) {
                LocationEmail = "admin@taifcity.gov.sa";
            }else if(n.equals(Najran)) {
                LocationEmail = "Link@najran.gov.sa";

            } else {
                LocationEmail = "it@arar-mu.gov.sa"; }

            }
    return state;}


}
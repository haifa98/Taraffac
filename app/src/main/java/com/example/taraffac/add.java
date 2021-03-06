package com.example.taraffac;

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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class add extends AppCompatActivity {

    Button add;
    Button save;
    Button cancel;
    double latitude=0;
    double longitude=0;
    RadioGroup type;
    RadioButton type1;
    RadioGroup size;
    RadioButton size1;
    DatabaseReference dataBymp;
    String sub;
    String userType;
    static final String TAG = "TTS";
    TextToSpeech mTts;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        add = (Button)findViewById(R.id.add_save);
        save = (Button)findViewById(R.id.add_save);
        cancel= (Button)findViewById(R.id.add_cancel);
        type = (RadioGroup) findViewById(R.id.add_type);
        size = (RadioGroup) findViewById(R.id.add_size);

// get user location from map class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userType = extras.getString("userType");
             latitude = extras.getDouble("Latitude");
             longitude = extras.getDouble("Longitude");
        }
        // conect the DB
        dataBymp = FirebaseDatabase.getInstance().getReference("SpeedBump");
        // set the default type & size of the bump
        ((RadioButton) type.getChildAt(0)).setChecked(true);
        ((RadioButton) size.getChildAt(1)).setChecked(true);
        // save() will be call if the user press in SAVE
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
// check the adding type, if the adding type is Voice command function read() will be called
if(userType.toLowerCase().contains("Voice".toLowerCase())){ read(); }
    }
// this method convert text to speech
    void speak(String s){
        // check the android version
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
    @Override // shutdown the text to speech
    protected void onDestroy() {
        super.onDestroy();
        if (mTts != null) {
            Log.v(TAG,"onDestroy: shutdown TTS");
            mTts.stop();
            mTts.shutdown(); } }

// the result from user voice become as array list and this method split the values for each element in arraylist
public static String[] spliteArray(ArrayList<String> array) {
    String[] tmp= new String[20];
    for(String line : array) {
        tmp = line.split("\\s+"); //split on one or more spaces
    }
    return tmp; }

// this is the main method for voice command
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public void read(){
        // "read" method is the main method for voice command
    // first will enable the text to speech then send the text to method "speak" to read the text for the user
    // then call method "getSpeechInput" to start speech recognition , after the user speech method "onActivityresult"
    // this method will get the user speech.

    final String emailid1 =  "what is the type of the bump ? is it cushion, table or hump ?  " +
            "and what is the size of the bump ? is it small, medium or large ?,, or if you want to cancel say cancel";

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
// start speech recognition after 11 sec
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        @Override
        public void run() {getSpeechInput(); }}, 11000); //  1000 = 1 sec
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
        if(data==null){ // if user say add and didn't choose the type & size the bump will be saved
            save();
        }
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result == null) {
                        // if user say add and didn't choose the type & size the bump will be saved
                        save(); }
                    assert result != null;
                    String[] newA = spliteArray(result); // split the result to words
                    // send words to check what user choose
                    String x = theResult(newA);
                    if (x.equals("cancel")) { // if user say cancel, will be returned to map
                        Intent go_map1= new Intent(this,map.class);
                        go_map1.putExtra("state", true);
                        startActivity(go_map1);

                    } else{
                            save(); // save in DB
                        }
                    }

                    break;} }
   // this method check radio buttons by user voice command
   public String theResult(String[] x){

       for (String n: x) {
           if (n.equals("cancel")) {
               return "cancel";
           }
           if (n.equals("cushion")) {
               ((RadioButton) type.getChildAt(2)).setChecked(true);
           }
           if (n.equals("table")) {
               ((RadioButton) type.getChildAt(1)).setChecked(true);
           }
           if (n.equals("hump")) {
               ((RadioButton) type.getChildAt(0)).setChecked(true);
           }
           if (n.equals("small")) {
               ((RadioButton) size.getChildAt(0)).setChecked(true);
           }
           if (n.equals("medium")) {
               ((RadioButton) size.getChildAt(1)).setChecked(true);
           }
           if (n.equals("large")) {
               ((RadioButton) size.getChildAt(2)).setChecked(true);
           }}
    return "add";}

// cancel add and go to homepage
    public void go_map(View v){
        Intent go_map1= new Intent(this,map.class);
        go_map1.putExtra("state", true);
        startActivity(go_map1);

    }
    // get type & size from the layout and store it in variable  as string
    public String checkType(){
        String t=null;
        int radioID = type.getCheckedRadioButtonId();
        type1 = findViewById(radioID);
        if(type1!=null) { t = (String) type1.getText(); }
        return t ; }

    public String checkSize(){
        String s =null ;
        int radioID = size.getCheckedRadioButtonId();
        size1 = findViewById(radioID);
        if(size1!=null){ s = (String) size1.getText();}
        return s ; }


    public void save(){
        // get type and size values
        String type2 = checkType();
        String size2 = checkSize();
// if lat and long updated by user location, the bump will be saved in DB
        if( latitude != 0 && longitude !=  0 ) {
            saveInDB(type2,size2);
        }else {
            if(latitude==0 || longitude == 0) {
                // if lat and long not update by user location then the bump will not be saved in DB
                Toast t = Toast.makeText(this, " The Adding was failed", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP, 0, 90);
                t.show(); } }
    }
        public void saveInDB(String type, String size){
            String id = dataBymp.push().getKey();

            SpeedBump bump = new SpeedBump ( latitude, longitude, type, size,0);

            // create sub parent for bump - replace '.' with '-' because '.' is not allowed in id firebase
            String sub1 = new DecimalFormat("00.00").format(latitude);
            String sub2=  new DecimalFormat("00.00").format(longitude);
            sub = sub1.replace('.','-')+"_"+sub2.replace('.','-');
            dataBymp.child(sub).child(id).setValue(bump);

            Toast t = Toast.makeText(this, " The Adding was successful", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();
// return to map page
            Intent go_map1= new Intent(this,map.class);
            go_map1.putExtra("state", true);
            startActivity(go_map1);

        }
    public void back_map1(View view) {
// back to map
        Intent go_map1= new Intent(this,map.class);
        go_map1.putExtra("state", true);
        startActivity(go_map1);
    }
}
package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class add extends AppCompatActivity {
    Button profile;
    Button log;
    Button add;
    Button save;
    Button cancel;
    double latitude;
    double longitude;
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

        profile = (Button)findViewById(R.id.but_Ppofile3);
        log = (Button)findViewById(R.id.but_log_out3);
        add = (Button)findViewById(R.id.add_save);
        save = (Button)findViewById(R.id.add_save);
        cancel= (Button)findViewById(R.id.add_cancel);
        type = (RadioGroup) findViewById(R.id.add_type);
        size = (RadioGroup) findViewById(R.id.add_size);
// get latlng from map class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userType = extras.getString("userType");
             latitude = extras.getDouble("Latitude");
             longitude = extras.getDouble("Longitude");
        }
        dataBymp = FirebaseDatabase.getInstance().getReference("SpeedBump");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
//if(userType=="Voice command"){
read(); }

    // text to speech

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

// voice command
public static String[] spliteArray(ArrayList<String> array) {
    String[] tmp= new String[20];
    for(String line : array) {
        tmp = line.split("\\s+"); //split on one or more spaces
    }
    return tmp;
}



@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public void read(){

    final String emailid1;
    emailid1 =  "what is the type of the bump ? is it cushion, table or hump ?  " +
            "and what is the size of the bump ? is it small, medium or large ?";

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
        public void run() {getSpeechInput(); }}, 9000); //  1000 = 1 sec
    }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    assert result != null;
                    String[] newA = spliteArray(result);
                    testtype(newA);
                   // save();

                    break;} }}

    public void testtype(String[] x){
        for (String n: x){
        if (n.equals("cushion")) { ((RadioButton)type.getChildAt(0)).setChecked(true);}
        if (n.equals("table")) { ((RadioButton)type.getChildAt(1)).setChecked(true);}
        if (n.equals("hump")) {((RadioButton)type.getChildAt(2)).setChecked(true);}
        if (n.equals("small")) { size.check(R.id.add_small);}
         if (n.equals("medium")) { size.check(R.id.add_mid);}
         if (n.equals("large")) { size.check(R.id.add_large);} }}

// cancel add and go to homepage
    public void go_map(View v){
        Intent go_map1= new Intent(this,map.class);
        go_map1.putExtra("state", true);
        startActivity(go_map1);

    }
    // get type & size from the layout and store it in variable  as string
    public String checkType(){
        int radioID = type.getCheckedRadioButtonId();
        type1 = findViewById(radioID);
        String t = (String) type1.getText();
        return t ; }

    public String checkSize(){
        int radioID = size.getCheckedRadioButtonId();
        size1 = findViewById(radioID);
        String s = (String) size1.getText();
        return s ; }

    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }


    public void save(){
        String type2 = checkType();
        String size2 = checkSize();
if(!type2.isEmpty() & !size2.isEmpty() &  longitude > 0 & latitude > 0 ) {

// add in database
    String id = dataBymp.push().getKey();

    // change format to dd.ddd
    double newlat = Double.parseDouble(new DecimalFormat("00.000").format(latitude));

    double newlng = Double.parseDouble(new DecimalFormat("00.000").format(longitude));

    SpeedBump bump = new SpeedBump ( newlat, newlng, type2, size2);
    // create sub child for bump - replace '.' with '-' because '.' is not allowed id firebase

    String sub1 = new DecimalFormat("00.00").format(latitude);
    String sub2=  new DecimalFormat("00.00").format(longitude);

    sub = sub1.replace('.','-')+"_"+sub2.replace('.','-');
    dataBymp.child(sub).child(id).setValue(bump);

    Toast t = Toast.makeText(this, " The Adding was successful", Toast.LENGTH_SHORT);
    t.setGravity(Gravity.TOP, 0, 90);
    t.show();

}else{

    Toast t = Toast.makeText(this, " The Adding was failed", Toast.LENGTH_SHORT);
    t.setGravity(Gravity.TOP, 0, 90);
    t.show();


}
        Intent log = new Intent(this,map.class);
        log.putExtra("state", true);
        startActivity(log); }



}
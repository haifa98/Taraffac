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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class edit_speed_bump extends AppCompatActivity {
    Button  But_Update_bump, But_Delete_bump;
    String TypeValue,SizeValue;
    RadioButton RadioType1, RadioType2,RadioType3,RadioSize1,RadioSize2,RadioSize3, RadioType,RadioSize;
    RadioGroup RadioGroupTypeUpdate,RadioGroupSizeUpdate;
    public String Cushion="Cushion";
    public String Table="Table";
    public String Small="Small";

    public String Midium="Medium";
    DatabaseReference dataBympUpdate;
    double latitude;
    double longitude;
    String bump_id,bump_loc ;
    int deleteCount;
    String userType;
    static final String TAG = "TTS";
    TextToSpeech mTts;
    String state1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_speed_bump);

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

        dataBympUpdate = FirebaseDatabase.getInstance().getReference("SpeedBump");//.child(bump_id);

        // show the retrieve data
        TypeValue= getIntent().getExtras().getString("type");
        SizeValue =getIntent().getExtras().getString("size");
        bump_id =getIntent().getExtras().getString("key");
        bump_loc =getIntent().getExtras().getString("loc");
        latitude =getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");
        deleteCount=getIntent().getExtras().getInt("deleteCount");
        userType = getIntent().getExtras().getString("userType");

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
        // voice
        if(userType.toLowerCase().contains("Voice".toLowerCase())){ read(); }


        // Delete Speed Bump
        But_Delete_bump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { delete();

            }
        });
        But_Update_bump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_Bump();
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
public void delete(){
        if(deleteCount>=3){
    dataBympUpdate.child(bump_loc).child(bump_id).removeValue();
            Toast.makeText(this, " The speed bump was deleted successfully ", Toast.LENGTH_SHORT).show();
}else{
    String TypeText,SizeText;
    TypeText=checkType();
    SizeText=checkSize();
    deleteCount++;
    SpeedBump bump = new SpeedBump ( latitude, longitude, TypeText, SizeText,deleteCount);
    dataBympUpdate.child(bump_loc).child(bump_id).setValue(bump);
            Toast.makeText(this, " The speed bump will be deleted soon", Toast.LENGTH_SHORT).show();
}
    onBackPressed();
    }
    public void Update_Bump() {// Update Speed Bump
        String TypeText,SizeText;
        TypeText=checkType();
        SizeText=checkSize();

        if(!TypeText.isEmpty() & !SizeText.isEmpty() ) {

// add in database

            SpeedBump bump = new SpeedBump ( latitude, longitude, TypeText, SizeText,deleteCount);
            dataBympUpdate.child(bump_loc).child(bump_id).setValue(bump);

            Toast t = Toast.makeText(this, " The Update was successful", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();
        }else {
            Toast t = Toast.makeText(this, " The Update was failed", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();


        }


        onBackPressed();
    }// Update Speed Bump


    public void back_map2(View view) {
        Intent go_map1= new Intent(this,map.class);
        startActivity(go_map1);
    }


    public void back_ntofiy(View view) {
        onBackPressed();

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

    // this is the main methon for voice command
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void read(){

        final String emailid1;
        emailid1 =  "if you want to edit select the type and size   " +
                "Or if you want to delete say Delete ?" + " or is you want to cancel say cancel";

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
            public void run() {getSpeechInput(); }}, 7000); //  1000 = 1 sec
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
            onBackPressed();
            Toast t = Toast.makeText(this, " The Update was failed", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, 0, 90);
            t.show();
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result == null) {
                        Intent go_map1= new Intent(this,map.class);
                        startActivity(go_map1);
                        Toast t = Toast.makeText(this, " The Update was failed", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.TOP, 0, 90);
                        t.show();
                    }
                    assert result != null;
                    String[] newA = spliteArray(result);
                    state1 = testtype(newA);
                    if (state1.equals("delete")) {
                        delete();
                    }else if (state1.equals("cancel")){
                        Intent go_map1= new Intent(this,map.class);
                        startActivity(go_map1);
                }else {
                    Update_Bump(); }

                    break;} }}


    // this method check radio buttons
    public String testtype(String[] x){
        String state =null;

        for (String n: x) {
            if (n.equals("cushion")) {
                ((RadioButton) RadioGroupTypeUpdate.getChildAt(0)).setChecked(true);
                state ="edit";
            }
            if (n.equals("table")) {
                ((RadioButton) RadioGroupTypeUpdate.getChildAt(1)).setChecked(true);
                state ="edit";
            }
            if (n.equals("hump")) {
                ((RadioButton) RadioGroupTypeUpdate.getChildAt(2)).setChecked(true);
                state ="edit";
            }
            if (n.equals("small")) {
                ((RadioButton) RadioGroupSizeUpdate.getChildAt(0)).setChecked(true);
                state ="edit";
            }
            if (n.equals("medium")) {
                ((RadioButton) RadioGroupSizeUpdate.getChildAt(1)).setChecked(true);
                state ="edit";
            }
            if (n.equals("large")) {
                ((RadioButton) RadioGroupSizeUpdate.getChildAt(2)).setChecked(true);
                state ="edit";
            }
        if (n.equals("delete")){
            state="delete";
        }
        if (n.equals("cancel")){
            state="cancel";
        }
        }

    return state;}


}


package com.example.taraffac;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class notify extends AppCompatActivity {
    Button profile;
    Button log;
    Button btn_reportopt,btn_edit;
    AlertDialog.Builder builder;
    SpeedBump sb;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        profile = findViewById(R.id.but_Ppofile2);
        log = findViewById(R.id.but_log_out2);
        btn_reportopt = findViewById(R.id.btn_reportopt);
        btn_edit = findViewById(R.id.btn_editopt);

        //notification code
        builder = new AlertDialog.Builder(notify.this);
        builder.setCancelable(true);

        builder = new AlertDialog.Builder(notify.this);
        builder.setCancelable(true);

        // Setting Negative "Cancel" Button
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //SpeedBump editedsb = sb;
                go_to_edit(this);
            }
        });
        // Setting Positive "Yes" Button
        builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                go_to_report(this);
            }
        });

        builder.setTitle("SLOW DOWN SPEED BUMP AHEAD");
        builder.setMessage("Type: " + sb.getType() + "/n" + "Size: " + sb.getSize());
        builder.show();
    }


    public void go_to_profile(View v){
        Intent profile = new Intent(this,profile.class);
        startActivity(profile);
    }
    public void log_out(View v){
        Intent log = new Intent(this,login.class);
        startActivity(log);
    }
    public void go_to_edit(DialogInterface.OnClickListener view) {
        Intent go_register = new Intent(this, edit_speed_bump.class);
        startActivity(go_register);
    }
    public void go_to_report(DialogInterface.OnClickListener view) {
        Intent go_register = new Intent(this, report.class);
        startActivity(go_register);
    }


}



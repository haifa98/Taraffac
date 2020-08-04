package com.example.taraffac;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class report extends Activity {
    Button but_close_re;
    Button btn_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        but_close_re = findViewById(R.id.but_close_re);
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

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }
    public void show_report(View v){
        Toast to =  Toast.makeText(this, " The Report was sent successfully"  , Toast.LENGTH_SHORT);
        to.setGravity(Gravity.TOP,0, 90);
        to.show();
    }
}
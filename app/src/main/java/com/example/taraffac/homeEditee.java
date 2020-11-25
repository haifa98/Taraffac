package com.example.taraffac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homeEditee extends AppCompatActivity {
    TextView textTypeEdit,textSizeEdit;
    Button EditBump,ReportBump, But_Show;
    DatabaseReference dataBymp;
    int deleteOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_editee);
        But_Show= findViewById(R.id.show);
        textTypeEdit= findViewById(R.id.textEditType);
        textSizeEdit=findViewById(R.id.textEditSize);
        EditBump = findViewById(R.id.but_edit_bump);
        ReportBump= findViewById(R.id.but_reportBump);
        //retrieve speed bump from firebase
        dataBymp = FirebaseDatabase.getInstance().getReference("SpeedBump").child("test");
        But_Show.setOnClickListener(new View.OnClickListener() { // start setOnClickListener
            @Override
            public void onClick(View v) {
                dataBymp.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String type=snapshot.child("type").getValue().toString();
                        String size =snapshot.child("size").getValue().toString();
                        deleteOption=(int)snapshot.child("deleteOption").getValue();
                        textTypeEdit.setText(type);
                     textSizeEdit.setText(size);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });// end setOnClickListener
        //extract data and transfer to anothar activity
        EditBump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditBump = new Intent(v.getContext(),edit_speed_bump.class);
              //  Intent DeleteCounter= new Intent(v.getContext(),edit_speed_bump.class);

                EditBump.putExtra("type",textTypeEdit.getText().toString());
                EditBump.putExtra("size",textSizeEdit.getText().toString());
                //EditBump.putExtra("deleteOption");

                startActivity(EditBump);

            }
        });

    }
}
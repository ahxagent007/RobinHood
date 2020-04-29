package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dexian.robinhood.R;

public class Rescue extends AppCompatActivity {

    Button btn_addRescue, btn_rescueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);

        btn_addRescue = findViewById(R.id.btn_addRescue);
        btn_rescueList = findViewById(R.id.btn_rescueList);

        btn_addRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddRescue.class));
            }
        });
        btn_rescueList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RescueList.class));
            }
        });
    }
}

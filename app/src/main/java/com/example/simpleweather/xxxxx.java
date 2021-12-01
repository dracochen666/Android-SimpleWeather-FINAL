package com.example.simpleweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class xxxxx extends AppCompatActivity {
    Button btn_inquire;
    EditText et_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationentry);

        btn_inquire = findViewById(R.id.btn_back);
        et_location = findViewById(R.id.et_enterlocation);
        btn_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = et_location.getText().toString();
                Intent intent_back = new Intent();
                intent_back.putExtra("city",location);
                setResult(2,intent_back);
                finish();
            }
        });

    }
}
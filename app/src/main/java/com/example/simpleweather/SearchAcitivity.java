package com.example.simpleweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchAcitivity extends AppCompatActivity {
    Button btn_inquire;
    EditText et_location;
//    public static final String CITY_NAME = "City";//键
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationentry);

        btn_inquire = findViewById(R.id.btn_back);
        et_location = findViewById(R.id.et_enterlocation);
        btn_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String location = et_location.getText().toString();
                Intent intent_back = new Intent(SearchAcitivity.this,MainActivity.class);
                intent_back.putExtra("city",et_location.getText().toString());
                setResult(2,intent_back);
                finish();
            }
        });
    }
}
package com.example.sm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class activity_test extends AppCompatActivity {
    TextView txt_test;
    int c_restaurant, c_shopping, c_cafe, c_bar, c_etc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        txt_test = findViewById(R.id.txt_test);

        Intent intent = getIntent();
        c_restaurant = intent.getIntExtra( "cost_rest", 0);
        c_shopping = intent.getIntExtra("cost_shop",0);
        c_cafe = intent.getIntExtra("cost_cafe",0);
        c_bar = intent.getIntExtra("cost_bar",0);
        c_etc = intent.getIntExtra("cost_etc",0);

        txt_test.setText("식당 :" + c_restaurant+"\n"+ "쇼핑 :" + c_shopping+"\n"+ "카페 :" + c_cafe+"\n" +"주점 :" + c_bar+"\n" +"기타 :" + c_etc+"\n");



    }
}
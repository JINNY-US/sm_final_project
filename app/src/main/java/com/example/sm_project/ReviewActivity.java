package com.example.sm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


    }
}

package com.example.sm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThanksActivity extends AppCompatActivity {
    private static final String TAG = "Thanks_Activity";

    TextView thanksText;
    ImageView tnanksImage;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        tnanksImage = findViewById(R.id.thanksImage);
        thanksText = findViewById(R.id.thanksText);
        btn_back = findViewById(R.id.backBtn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), MypageActivity.class));
                finish();
            }
        });
    }

    //뒤로가기 누르면 Mypage 화면으로 넘어감
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ThanksActivity.this, MypageActivity.class);
        startActivity(intent);
        finish();
    }
}

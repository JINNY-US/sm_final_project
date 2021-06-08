package com.example.sm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CostSetPopupActivity extends Activity {
    TextView txt_Location, txt_Total_Price, txt_Detailed_Costs;
    String city, town;
    int total_cost, cost_rest, cost_shop, cost_cafe, cost_bar, cost_etc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_costset_popup);

        txt_Location = findViewById(R.id.txt_popup_city_town);
        txt_Total_Price = findViewById(R.id.txt_popup_total_cost);
        txt_Detailed_Costs = findViewById(R.id.txt_popup_costs);

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

        total_cost = sharedPreferences.getInt("total_cost", 0);
        cost_rest = sharedPreferences.getInt("cost_rest", 0);
        cost_cafe = sharedPreferences.getInt("cost_cafe", 0);
        cost_shop = sharedPreferences.getInt("cost_shop", 0);
        cost_bar = sharedPreferences.getInt("cost_bar", 0);
        cost_etc = sharedPreferences.getInt("cost_etc", 0);

        city = sharedPreferences.getString("set_city", "");
        town = sharedPreferences.getString("set_town", "");

        txt_Location.setText(city + " " + town);
        txt_Total_Price.setText(total_cost+"원");
        txt_Detailed_Costs.setText("식당 : "+cost_rest+"원\n"+"쇼핑 : "+cost_shop+"원\n"+"카페 : "+cost_cafe+"원\n"+"주점 : "+cost_bar+"원\n"+"기타 : "+cost_etc+"원\n");

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
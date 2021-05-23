package com.example.sm_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CostSettingActivity extends AppCompatActivity {
    Spinner spinner, spn_rest, spn_shop, spn_cafe, spn_bar, spn_etc;
    Button cost_save_btn;
    TextView txt_rest, txt_shop, txt_cafe, txt_bar, txt_etc;
    EditText Total_cost;
    CardView cardView_mode, cardView_self_setting;

    int t_cost;
    int per_rest, per_shop, per_cafe, per_bar, per_etc = 0;
    //모드선택인지 직접 설정인지 알기위한 flag
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_setting);

        //카드뷰 선언
        cardView_mode = findViewById(R.id.cardview_mode);
        cardView_self_setting = findViewById(R.id.cardView_self_setting);

        //스피너 선언
        spinner = findViewById(R.id.spinner);
        spn_rest = findViewById(R.id.spn_per_rest);
        spn_shop = findViewById(R.id.spn_per_shop);
        spn_cafe = findViewById(R.id.spn_per_cafe);
        spn_bar = findViewById(R.id.spn_per_bar);
        spn_etc = findViewById(R.id.spn_per_etc);

        //전체가격 입력 받는 editText 창 선언
        Total_cost = findViewById(R.id.txt_totalcost);

        //버튼 선언
        cost_save_btn = findViewById(R.id.cost_save_btn);

        //텍스트뷰 선언
        txt_rest = findViewById(R.id.txt_per_rest);
        txt_shop = findViewById(R.id.txt_per_shop);
        txt_cafe = findViewById(R.id.txt_per_cafe);
        txt_bar = findViewById(R.id.txt_per_bar);
        txt_etc = findViewById(R.id.txt_per_etc);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selItem = (String) spinner.getSelectedItem();


                switch (selItem) {
                    case "식당 위주":
                        flag = 0;

                        //두개의 카드뷰를 선택한 모드에 따라 전환 시키는 코드
                        cardView_mode.setVisibility(View.VISIBLE);
                        cardView_self_setting.setVisibility(View.GONE);

                        //유저에게 보여주기 위한 txt 출력
                        txt_rest.setText("5");
                        txt_shop.setText("0");
                        txt_cafe.setText("2");
                        txt_bar.setText("0");
                        txt_etc.setText("3");
                        //내부에서 계산하기용 int값
                        per_rest = 5;
                        per_shop = 0;
                        per_cafe = 2;
                        per_bar = 0;
                        per_etc = 3;

                        break;
                    case "쇼핑 위주":
                        flag = 0;
                        cardView_mode.setVisibility(View.VISIBLE);
                        cardView_self_setting.setVisibility(View.GONE);
                        txt_rest.setText("3");
                        txt_shop.setText("5");
                        txt_cafe.setText("0");
                        txt_bar.setText("0");
                        txt_etc.setText("2");
                        per_rest = 3;
                        per_shop = 5;
                        per_cafe = 0;
                        per_bar = 0;
                        per_etc = 2;

                        break;
                    case "카페 위주":
                        flag = 0;
                        cardView_mode.setVisibility(View.VISIBLE);
                        cardView_self_setting.setVisibility(View.GONE);
                        txt_rest.setText("3");
                        txt_shop.setText("0");
                        txt_cafe.setText("5");
                        txt_bar.setText("0");
                        txt_etc.setText("2");
                        per_rest = 3;
                        per_shop = 0;
                        per_cafe = 5;
                        per_bar = 0;
                        per_etc = 2;
                        break;
                    case "주점 위주":
                        flag = 0;
                        cardView_mode.setVisibility(View.VISIBLE);
                        cardView_self_setting.setVisibility(View.GONE);
                        txt_rest.setText("0");
                        txt_shop.setText("0");
                        txt_cafe.setText("0");
                        txt_bar.setText("5");
                        txt_etc.setText("5");
                        per_rest = 0;
                        per_shop = 0;
                        per_cafe = 0;
                        per_bar = 5;
                        per_etc = 5;

                        break;
                    case "기타(노래방, 이색카페 등) 위주":
                        flag = 0;
                        cardView_mode.setVisibility(View.VISIBLE);
                        cardView_self_setting.setVisibility(View.GONE);
                        txt_rest.setText("3");
                        txt_shop.setText("0");
                        txt_cafe.setText("2");
                        txt_bar.setText("0");
                        txt_etc.setText("5");
                        per_rest = 3;
                        per_shop = 0;
                        per_cafe = 2;
                        per_bar = 0;
                        per_etc = 5;
                        break;

                    case "직접 지정":
                        flag = 1;
                        cardView_mode.setVisibility(View.GONE);
                        cardView_self_setting.setVisibility(View.VISIBLE);

                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), R.string.Toast_mode_not_select, Toast.LENGTH_SHORT).show();
            }
        });

        cost_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cost_rest, cost_shop, cost_cafe, cost_bar, cost_etc;
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);

                try {
                    t_cost = Integer.parseInt(Total_cost.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.Toast_total_cost_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (t_cost < 1000) {
                    Toast.makeText(getApplicationContext(), R.string.Toast_not_enough_money, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (flag == 1) {
                    int per_rest_setting = Integer.parseInt((String) spn_rest.getSelectedItem());
                    int per_shop_setting = Integer.parseInt((String) spn_shop.getSelectedItem());
                    int per_cafe_setting = Integer.parseInt((String) spn_cafe.getSelectedItem());
                    int per_bar_setting = Integer.parseInt((String) spn_bar.getSelectedItem());
                    int per_etc_setting = Integer.parseInt((String) spn_etc.getSelectedItem());

                    if (per_rest_setting + per_shop_setting + per_cafe_setting + per_bar_setting + per_etc_setting == 10){
                        cost_rest = (int)(t_cost * 0.1 * per_rest_setting);
                        cost_shop = (int)(t_cost * 0.1 * per_shop_setting);
                        cost_cafe = (int)(t_cost * 0.1 * per_cafe_setting);
                        cost_bar = (int)(t_cost * 0.1 * per_bar_setting);
                        cost_etc = (int)(t_cost * 0.1 * per_etc_setting);

                    }else{
                        Toast.makeText(getApplicationContext(),R.string.Toast_not_10,Toast.LENGTH_SHORT).show();
                        return;
                    }


                } else {

                    cost_rest = (int)(t_cost * 0.1 * per_rest);
                    cost_shop = (int)(t_cost * 0.1 * per_shop);
                    cost_cafe = (int)(t_cost * 0.1 * per_cafe);
                    cost_bar = (int)(t_cost * 0.1 * per_bar);
                    cost_etc = (int)(t_cost * 0.1 * per_etc);

                }
                intent.putExtra("cost_rest", cost_rest);
                intent.putExtra("cost_shop", cost_shop);
                intent.putExtra("cost_cafe", cost_cafe);
                intent.putExtra("cost_bar", cost_bar);
                intent.putExtra("cost_etc", cost_etc);
                startActivity(intent);
                finish();

            }
        });


    }


}


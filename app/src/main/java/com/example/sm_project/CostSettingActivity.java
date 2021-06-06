package com.example.sm_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CostSettingActivity extends AppCompatActivity {
    Spinner spinner, spn_rest, spn_shop, spn_cafe, spn_bar, spn_etc; // 가격 설정 스피너
    Spinner spn_set_city, spn_set_town; //지역 설절 스피너
    Button cost_save_btn;
    TextView txt_rest, txt_shop, txt_cafe, txt_bar, txt_etc;
    EditText Total_cost;
    CardView cardView_mode, cardView_self_setting;
    ArrayAdapter<CharSequence> adspin1, adspin2;
    private ArrayList<String> LocationArr;
    private ArrayList<Integer> CostArr;
    String set_city, set_town;

    String userType;
    boolean userType_bool;

    SharedPreferences.Editor editor;

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
        spn_set_city = findViewById(R.id.spinner_set_city);
        spn_set_town = findViewById(R.id.spinner_set_town);

        //지역구 스피너 변화를 위한 adapter 설정
        adspin1 = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_set_city.setAdapter(adspin1);

        //intent 하기 위한 배열들의 객체 선언
        CostArr = new ArrayList<Integer>();
        LocationArr = new ArrayList<String>();

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

        SharedPreferences sharedPreferences= getSharedPreferences("sFile", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userType = sharedPreferences.getString("userType", "");
        userType_bool = Boolean.parseBoolean(userType);

        spn_set_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selItem = (String) spn_set_city.getSelectedItem();

                setSpinnerItemList(selItem); //하위 지역구 array_list 결정하는 함수 호출
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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


        //확인 버튼 클릭시 최종 결정된 장소, 비용 intent로 전송
        cost_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cost_rest, cost_shop, cost_cafe, cost_bar, cost_etc;
                Intent intent;
                //userType을 받아와서 type에 맞게 메인화면 불러오기
                if (userType_bool) {
                    intent = new Intent(getApplication(), MainActivity.class);
                }
                else{
                    intent = new Intent(getApplication(), SellerMainActivity.class);
                }


                set_city = spn_set_city.getSelectedItem().toString();
                set_town = spn_set_town.getSelectedItem().toString();

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

                    if (per_rest_setting + per_shop_setting + per_cafe_setting + per_bar_setting + per_etc_setting == 10) {
                        cost_rest = (int) (t_cost * 0.1 * per_rest_setting);
                        cost_shop = (int) (t_cost * 0.1 * per_shop_setting);
                        cost_cafe = (int) (t_cost * 0.1 * per_cafe_setting);
                        cost_bar = (int) (t_cost * 0.1 * per_bar_setting);
                        cost_etc = (int) (t_cost * 0.1 * per_etc_setting);

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.Toast_not_10, Toast.LENGTH_SHORT).show();
                        return;
                    }


                } else {

                    cost_rest = (int) (t_cost * 0.1 * per_rest);
                    cost_shop = (int) (t_cost * 0.1 * per_shop);
                    cost_cafe = (int) (t_cost * 0.1 * per_cafe);
                    cost_bar = (int) (t_cost * 0.1 * per_bar);
                    cost_etc = (int) (t_cost * 0.1 * per_etc);

                }

                //int형 배열 CostArr에 각 항목별 금액 추가
                CostArr.add(0, cost_rest);
                CostArr.add(1, cost_shop);
                CostArr.add(2, cost_cafe);
                CostArr.add(3, cost_bar);
                CostArr.add(4, cost_etc);

                //intent로 전송
                intent.putIntegerArrayListExtra("CostArr", CostArr);

                //String형 배열 LocationArr에 설정 지역구 추가
                LocationArr.add(0, set_city);
                LocationArr.add(1, set_town);

                //intent로 전송
                intent.putStringArrayListExtra("LocationArr", LocationArr);

                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), MypageActivity.class));
        finish();
    }

    private void setSpinnerItemList(String selItem) {
        switch (selItem) {
            case "서울특별시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Seoul, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));  //기존에 선택되있던 하위 지역구 선택값이 있으면 그걸 받아옴
                break;

            case "부산광역시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Busan, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;

            case "대구광역시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Daegu, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "인천광역시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Incheon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                // spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "대전광역시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Daejeon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "울산광역시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Ulsan, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "세종특별자치시":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Sejong, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경기도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "강원도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Gangwon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "충청북도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Chungbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "충청남도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Chungnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "전라북도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "전라남도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Jeonnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경상북도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경상남도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spn_set_town, town));
                break;
            case "제주특별자치도":
                adspin2 = ArrayAdapter.createFromResource(CostSettingActivity.this, R.array.Jeju, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_set_town.setAdapter(adspin2);
                //spn_set_town.setSelection(getIndex(spn_set_town, town));
                break;
            default:
                break;

        }
    }

}


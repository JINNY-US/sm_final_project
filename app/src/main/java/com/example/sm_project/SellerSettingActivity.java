package com.example.sm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;


public class SellerSettingActivity extends AppCompatActivity {
    Spinner spinner_store, spinner_city, spinner_town;
    ArrayAdapter<CharSequence> stspin, adspin1, adspin2;
    CardView cardView_tag;
    EditText txt_name, txt_intro, txt_addr, txt_time;
    String town, uid, name_, email, password, init_set, usertype, tag;
    Button button_first;
    TextView txt_tag_num, txt_init_set;
    int tag_num;
    String init_set_string;
    Boolean init_set_bool;
    String SP_name, SP_addr, SP_intro, SP_time, SP_type, SP_city, SP_town;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference mDatabase;
    FirebaseUser user;

    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_setting);


        Intent intent = getIntent();

        tag_num = intent.getIntExtra("int_position_size", 0);
        tag = intent.getStringExtra("position_nums");

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //객체 초기화
        txt_init_set = findViewById(R.id.txt_init_set);

        //최초 셋팅일 경우 안내문자가 보이도록 설정
      /*  if (userType) {
            txt_init_set.setVisibility(View.GONE);
        } else {
            txt_init_set.setVisibility(View.VISIBLE);
        } */


        spinner_city = findViewById(R.id.spinner_city);
        spinner_town = findViewById(R.id.spinner_town);
        spinner_store = findViewById(R.id.spinner_store_type);

        stspin = ArrayAdapter.createFromResource(this, R.array.store_type, android.R.layout.simple_spinner_dropdown_item);
        stspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_store.setAdapter(stspin);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adspin1);

        txt_tag_num = findViewById(R.id.txt_tag_num);
        txt_tag_num.setText("현재 " + tag_num + "개 선택됨");

        cardView_tag = findViewById(R.id.cardview_Tag);

        txt_name = findViewById(R.id.txt_shop_name);
        txt_addr = findViewById(R.id.txt_shop_addr);
        txt_time = findViewById(R.id.txt_shop_time);
        txt_intro = findViewById(R.id.txt_shop_intro);
        button_first = findViewById(R.id.button_first);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기

        uid = user.getUid();

        //SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);

        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String name = sharedPreferences.getString("name_key", "");
        String addr = sharedPreferences.getString("addr_key", "");
        String intro = sharedPreferences.getString("intro_key", "");
        String time = sharedPreferences.getString("time_key", "");
        String type = sharedPreferences.getString("type_key", "");
        String city = sharedPreferences.getString("city_key", "");
        town = sharedPreferences.getString("town_key", "");
        txt_name.setText(name);
        txt_intro.setText(intro);
        txt_addr.setText(addr);
        txt_time.setText(time);
        spinner_store.setSelection(getIndex(spinner_store, type));
        spinner_city.setSelection(getIndex(spinner_city, city));


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selItem = (String) spinner_city.getSelectedItem();

                setSpinnerItemList(selItem); //하위 지역구 array_list 결정하는 함수 호출
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (uid != null) {
                    name_ = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    password = snapshot.child("password").getValue(String.class);
                    usertype = snapshot.child("userType").getValue(String.class);
                    init_set = snapshot.child("init_set").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txt_name.getText().toString().equals("") && !txt_addr.getText().toString().equals("") && !spinner_store.getSelectedItem().toString().equals("") && !spinner_city.getSelectedItem().toString().equals("") && !spinner_town.getSelectedItem().toString().equals("")) {

                    String name = txt_name.getText().toString().trim();
                    String type_string = spinner_store.getSelectedItem().toString();
                    int type_int = spinner_store.getSelectedItemPosition();
                    String city_string = spinner_city.getSelectedItem().toString();
                    int city_int = spinner_city.getSelectedItemPosition();
                    String town_string = spinner_town.getSelectedItem().toString();
                    int town_int = spinner_town.getSelectedItemPosition();
                    String addr = txt_addr.getText().toString().trim();
                    String time = "";
                    String intro = "";

                    init_set = "true";

                    if (!txt_time.getText().toString().equals("")) {
                        time = txt_time.getText().toString().trim();
                    }

                    if (!txt_intro.getText().toString().equals("")) {
                        intro = txt_intro.getText().toString().trim();
                    }

                    HashMap<String, Object> storeUpdates = new HashMap<>();
                    HashMap<String, Object> userInitUpdates = new HashMap<>();

                    StoreInfo storeInfo = new StoreInfo(uid, name, type_string, type_int, city_string, city_int, town_string, town_int, addr, time, intro, tag);
                    Map<String, Object> storeValue = storeInfo.toMap();

                    User_Init_Setting user_init_setting = new User_Init_Setting(uid, name_, email, usertype, password, init_set);
                    Map<String, Object> UserInitValue = user_init_setting.toMap();

                    storeUpdates.put("/StoreInfo/" + uid, storeValue);
                    userInitUpdates.put("/Users/" + uid, UserInitValue);
                    mDatabase.updateChildren(storeUpdates);
                    mDatabase.updateChildren(userInitUpdates);

                    SharedPre_remove();

                    Toast.makeText(SellerSettingActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SellerSettingActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SellerSettingActivity.this, "작성하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cardView_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TagAddActivity.class);
                intent.putExtra("position_nums", tag);
                startActivity(intent);
                finish();
            }
        });

    }

    private void SharedPre_remove() {
        editor.remove(SP_name);
        editor.remove(SP_addr);
        editor.remove(SP_intro);
        editor.remove(SP_time);
        editor.remove(SP_type);
        editor.remove(SP_city);
        editor.remove(SP_town);

        editor.commit();
    }

    private void setSpinnerItemList(String selItem) {
        switch (selItem) {
            case "서울특별시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Seoul, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));  //기존에 선택되있던 하위 지역구 선택값이 있으면 그걸 받아옴
                break;

            case "부산광역시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Busan, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;

            case "대구광역시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Daegu, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "인천광역시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Incheon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "대전광역시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Daejeon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "울산광역시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Ulsan, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "세종특별자치시":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Sejong, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경기도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "강원도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Gangwon, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "충청북도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Chungbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "충청남도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Chungnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "전라북도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "전라남도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Jeonnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경상북도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "경상남도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            case "제주특별자치도":
                adspin2 = ArrayAdapter.createFromResource(SellerSettingActivity.this, R.array.Jeju, android.R.layout.simple_spinner_dropdown_item);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_town.setAdapter(adspin2);
                spinner_town.setSelection(getIndex(spinner_town, town));
                break;
            default:
                break;

        }
    }


    //spinner에서 이전에 선택되있던 것과 동일한 선택 결과를 받아오게 함
    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onStop() {
        super.onStop();

        SP_name = txt_name.getText().toString(); // 사용자가 입력한 저장할 데이터
        SP_addr = txt_addr.getText().toString();
        SP_intro = txt_intro.getText().toString();
        SP_time = txt_time.getText().toString();
        SP_type = spinner_store.getSelectedItem().toString(); //선택한 시피너의 결과값을 String 타입으로 저장
        SP_city = spinner_city.getSelectedItem().toString();
        SP_town = spinner_town.getSelectedItem().toString();
        editor.putString("name_key", SP_name);
        editor.putString("addr_key", SP_addr);
        editor.putString("intro_key", SP_intro);
        editor.putString("time_key", SP_time);
        editor.putString("type_key", SP_type);
        editor.putString("city_key", SP_city);
        editor.putString("town_key", SP_town);


        editor.commit(); // 임시저장 커밋


    }



    public class User_Init_Setting {
        private String uid;
        private String name;
        private String email;
        private String usertype;
        private String password;
        private String init_set;


        public User_Init_Setting() {
        }


        public User_Init_Setting(String uid, String name_, String email, String usertype, String password, String init_set) {
            this.name = name_;
            this.uid = uid;
            this.email = email;
            this.password = password;
            this.usertype = usertype;
            this.init_set = init_set;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result_ = new HashMap<>();
            result_.put("name", name);
            result_.put("uid", uid);
            result_.put("email", email);
            result_.put("usertype", usertype);
            result_.put("password", password);
            result_.put("init_set", init_set);

            return result_;
        }

    }


    public class StoreInfo {
        private String uid;
        private String name;
        private String type_string;
        private int type_int;
        private String city_string;
        private int city_int;
        private String town_string;
        private int town_int;
        private String addr;
        private String time;
        private String intro;
        private String tag;

        public StoreInfo() {
        }

        public StoreInfo(String uid, String name, String type_string, int type_int, String city_string, int city_int, String town_string, int town_int,
                         String addr, String time, String intro, String tag) {
            this.uid = uid;
            this.name = name;
            this.type_string = type_string;
            this.type_int = type_int;
            this.city_string = city_string;
            this.city_int = city_int;
            this.town_string = town_string;
            this.town_int = town_int;
            this.addr = addr;
            this.time = time;
            this.intro = intro;
            this.tag = tag;

        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", uid);
            result.put("name", name);
            result.put("type_string", type_string);
            result.put("type_int", type_int);
            result.put("city_string", city_string);
            result.put("city_int", city_int);
            result.put("town_string", town_string);
            result.put("town_int", town_int);
            result.put("addr", addr);
            result.put("time", time);
            result.put("intro", intro);
            result.put("tag", tag);

            return result;
        }
    }

}
package com.example.sm_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class TagAddActivity extends Activity {
    Button btn_submit;
    ListView listView_tag;
    String set_position_nums = "";
    String get_position_nums;
    String[] num_array;
    Boolean i;
    int position_size;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_add);

        btn_submit = findViewById(R.id.btn_tag_submit);

        Intent intent = getIntent();
        get_position_nums = intent.getStringExtra("position_nums");
        i = intent.getBooleanExtra("i_bool", false);


        listView_tag = (ListView) findViewById(R.id.listView_tag);
        //listview와 array 연동
        ArrayAdapter<CharSequence> adapterOfListViewTag = ArrayAdapter.createFromResource(
                this,
                R.array.tag_list,
                android.R.layout.simple_list_item_multiple_choice
        );
        listView_tag.setAdapter(adapterOfListViewTag);
        listView_tag.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // 중복 선택 가능

        if (get_position_nums != null) {
            num_array = get_position_nums.split(",");
            for (int i = 0; i < num_array.length; i++) {
                int a = Integer.parseInt(num_array[i]);
                listView_tag.setItemChecked(a, true);
            }
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SellerSettingActivity.class);
                SparseBooleanArray positionBoolArr = listView_tag.getCheckedItemPositions();
//
                position_size = positionBoolArr.size();

                //체크된 item 들의 position값을 position_num이라는 문자열에 저장
                for (int i = 0; i < 40; i++) {
                    if (positionBoolArr.get(i)) {
                        set_position_nums += i + ",";
                    } else {

                    }
                }
                set_position_nums = set_position_nums.trim();//앞뒤 공백 제거
                //SellerSettingActivity로 전달
                intent.putExtra("int_position_size", position_size);
                intent.putExtra("position_nums", set_position_nums);
                intent.putExtra("i_bool", i);

                startActivity(intent);
                finish();
            }

        });

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "현재 화면은 뒤로가기가 불가능 합니다.\n태그를 선택 후 저장버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
    }

}
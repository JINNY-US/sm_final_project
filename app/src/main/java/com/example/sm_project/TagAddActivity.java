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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class TagAddActivity extends Activity {
    Button btn_submit;
    ListView listView_tag;
    String position_nums = "";
    String test_string = "0, 1, 5, 7, 20, 31,";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_add);

        btn_submit = findViewById(R.id.btn_tag_submit);

       int[] intArr = extractIntegersFromText(test_string);

        Log.d("디버그", String.valueOf(intArr));


        listView_tag = (ListView) findViewById(R.id.listView_tag);
        //listview와 array 연동
        ArrayAdapter<CharSequence> adapterOfListViewTag = ArrayAdapter.createFromResource(
                this,
                R.array.tag_list,
                android.R.layout.simple_list_item_multiple_choice
        );
        listView_tag.setAdapter(adapterOfListViewTag);
        listView_tag.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // 중복 선택 가능




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellerSettingActivity.class);
                SparseBooleanArray positionBoolArr = listView_tag.getCheckedItemPositions();
                int position_size = positionBoolArr.size();

                //태그를 10개까지만 체크 할 수 있게 제한을 검
                if (position_size > 10) {
                    Toast.makeText(getApplicationContext(), "태그는 10개까지만 선택가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //체크된 item 들의 position값을 position_num이라는 문자열에 저장
                    for(int i = 0; i<40;i++){
                        if(positionBoolArr.get(i)){
                            position_nums += " "+i+",";
                        }else{

                        }
                    }
                    position_nums = position_nums.trim();//앞뒤 공백 제거
                    //SellerSettingActivity로 전달
                    intent.putExtra("int_position_size", position_size);
                    intent.putExtra("position_nums", position_nums);
                    startActivity(intent);
                    finish();
                }

            }
        });



    }

    // 문자열 배열로 배열 시도, 진행중, 아직 작동 안된
    private static int[] extractIntegersFromText( final String source ) {

        String[] items = source.split(",");

        int[] results = new int[ items.length ];

        int i = 0;

        for ( String textValue : items ) {
            results[i] = Integer.parseInt( textValue );
            i++;
        }

        return results ;
    }

}
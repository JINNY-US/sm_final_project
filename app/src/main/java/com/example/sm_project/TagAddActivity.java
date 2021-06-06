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


public class  TagAddActivity extends Activity {
    Button btn_submit;
    ListView listView_tag;
    String set_position_nums = "";
    String get_position_nums;
    String[] num_array;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_add);

        btn_submit = findViewById(R.id.btn_tag_submit);

        Intent intent = getIntent();
        get_position_nums = intent.getStringExtra("position_nums");



        listView_tag = (ListView) findViewById(R.id.listView_tag);
        //listview와 array 연동
        ArrayAdapter<CharSequence> adapterOfListViewTag = ArrayAdapter.createFromResource(
                this,
                R.array.tag_list,
                android.R.layout.simple_list_item_multiple_choice
        );
        listView_tag.setAdapter(adapterOfListViewTag);
        listView_tag.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // 중복 선택 가능

        if(get_position_nums != null) {
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
//                for(int i = 0; i > 40; i++){
//                    if(positionBoolArr.get(i) == false){
//                        positionBoolArr.delete(i);
//                    }else{
//
//                    }
//                }
                int position_size = positionBoolArr.size();

                Log.d("디버그", Integer.toString(position_size));

                //태그를 10개까지만 체크 할 수 있게 제한을 검 -> 삭제 예정
                if (position_size > 10) {
                    Toast.makeText(getApplicationContext(), "태그는 10개까지만 선택가능합니다.", Toast.LENGTH_SHORT).show();
                    positionBoolArr.clear();
                } else {
                    //체크된 item 들의 position값을 position_num이라는 문자열에 저장
                    for(int i = 0; i<40;i++){
                        if(positionBoolArr.get(i)){
                            set_position_nums += i+",";
                        }else{

                        }
                    }
                    set_position_nums = set_position_nums.trim();//앞뒤 공백 제거
                    //SellerSettingActivity로 전달
                    intent.putExtra("int_position_size",position_size);
                    intent.putExtra("position_nums", set_position_nums);

                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return; //튐김
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Intent intent = new Intent(TagAddActivity.this, SellerSettingActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
            startActivity(intent);  //인텐트 이동
            finish();
            toast.cancel();
        }

    }

}
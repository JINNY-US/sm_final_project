package com.example.sm_project;

import android.content.Intent;
import android.os.Bundle;

import com.example.sm_project.adapter.FragmentAdapter;
import com.example.sm_project.fragment.fragment_review_list;
import com.example.sm_project.fragment.fragment_search;
import com.example.sm_project.fragment.fragment_seller_intro;
import com.example.sm_project.fragment.fragment_seller_product_list;
import com.example.sm_project.fragment.fragment_shop_list;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class SellerMainActivity extends AppCompatActivity {

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

    private ArrayList<String> LocationArr;
    private ArrayList<Integer> CostArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivMenu=findViewById(R.id.iv_menu);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);

        tabLayout=findViewById(R.id.layout_tabs);
        viewPager=findViewById(R.id.view_pager);
        adapter=new FragmentAdapter(getSupportFragmentManager(),1);
        viewPager.setOffscreenPageLimit(3);

        CostArr = new ArrayList<Integer>();
        LocationArr = new ArrayList<String>();

        Intent intent = getIntent();
        CostArr = intent.getIntegerArrayListExtra("CostArr");
        LocationArr = intent.getStringArrayListExtra("LocationArr");


        //매니저에 프레그먼트 추가
        adapter.addFragment(new fragment_seller_intro()); //
        adapter.addFragment(new fragment_seller_product_list());
        adapter.addFragment(new fragment_review_list());
        adapter.addFragment(new fragment_search());

        //뷰페이저와 어댑터 연결
        viewPager.setAdapter(adapter);

        //탭과 뷰페이저 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("내 가게 정보");
        tabLayout.getTabAt(1).setText("메뉴 정보");
        tabLayout.getTabAt(2).setText("리뷰");
        tabLayout.getTabAt(3).setText("검색");


        // 우측 상단의 설정버튼 클릭 이벤트 리스너
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

   /* @Override
    protected void onStop() {
        super.onStop();
        adapter.saveState();
    } */


}

//public class SellerMainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_seller_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplication(), ProductAddActivity.class));
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
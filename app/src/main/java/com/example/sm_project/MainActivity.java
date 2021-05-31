package com.example.sm_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sm_project.fragment.FragmentAdapter;
import com.example.sm_project.fragment.fragment_review_list;
import com.example.sm_project.fragment.fragment_search;
import com.example.sm_project.fragment.fragment_shop_list;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";


    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

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


        setSupportActionBar(toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), MypageActivity.class));
                finish();
            }
        });

//매니저에 프레그먼트 추가
        adapter.addFragment(new fragment_shop_list());
        adapter.addFragment(new fragment_review_list());
        adapter.addFragment(new fragment_search());

        //뷰페이저와 어댑터 연결
        viewPager.setAdapter(adapter);

        //탭과 뷰페이저 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("추천 가게");
        tabLayout.getTabAt(1).setText("리뷰");
        tabLayout.getTabAt(2).setText("검색");
    }
}

/*
package com.example.sm_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    Button go_mypage, sellerSetting, sellerIntro, go_productadd;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        */
/*go_mypage = findViewById(R.id.go_mypage);
        sellerSetting = findViewById(R.id.sellerSetting);
        sellerIntro = findViewById(R.id.sellerIntro);
        go_productadd = findViewById(R.id.go_productadd);*//*


        */
/*go_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), MypageActivity.class));
                finish();
            }
        });

        sellerSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SellerSettingActivity.class));
                finish();
            }
        });

        sellerIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), RequestActivity.class));
                finish();
            }
        });

        go_productadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), ProductAddActivity.class));
            }
        });*//*

    }

    public void signOut(View view) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());
        alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();

        //대화창 클릭 시 뒷 배경 어두워지는 것 막기
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //대화창 제목 설정
        alert.setTitle("로그아웃");

        //대화창 아이콘 설정
        alert.setIcon(R.drawable.exclamation);

        //대화창 배경 색 설정
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(180, 180, 180)));
        alert.show();

    }
}*/

package com.example.sm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sm_project.adapter.FragmentAdapter;
import com.example.sm_project.fragment.fragment_main;
import com.example.sm_project.fragment.fragment_review_list;
import com.example.sm_project.fragment.fragment_search;
import com.example.sm_project.fragment.fragment_seller_product_list;
import com.example.sm_project.fragment.fragment_shop_list;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";

    //전면 광고 넣으려면 변경해야 할 것
    /*
    1. 구글 admob 등록 (계정 만들어두었음!!!!!)
    2. manifest (권한 허용, ID 등록)
    3. 앱 수준의 gradle (라이브러리 등록)
    4. 광고를 넣으려는 액티비티.java 파일 (라이브러리 불러오고 넣으려는 광고에 맞게 함수 설정)
    5. 구글 스토어에 게시하기 전(= 어플 완성하고 올리기 전), MainActivity에 써둔 테스트용 ID를 실제 사용하는 ID로 바꿔두기
     >>>> InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", ...
     테스트용 ID를 넣고 테스트함
     */

    private InterstitialAd mInterstitialAd; //광고
    //private AdView mAdView;    //배너광고!!!!!!!

    //실제 ID
    //ca-app-pub-4117375013439833~5937823748

    //테스트 ID
    //ca-app-pub-3940256099942544/1033173712

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter, adapter_seller;

    private ArrayList<String> LocationArr;
    private ArrayList<Integer> CostArr;

    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;

    boolean mbool;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //광고 불러오기!!!!!!!!!!!!!!!!!!!!!!!
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createPersonalizeAd();
            }
        });
        //광고 대기 중!!!!!!!!!!!!!!!!!!!!!!!!

        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);

        tabLayout = findViewById(R.id.layout_tabs);
        viewPager = findViewById(R.id.view_pager);

        adapter = new FragmentAdapter(getSupportFragmentManager(), 1);
        viewPager.setOffscreenPageLimit(2);

        CostArr = new ArrayList<Integer>();
        LocationArr = new ArrayList<String>();

        Intent intent = getIntent();
        CostArr = intent.getIntegerArrayListExtra("CostArr");
        LocationArr = intent.getStringArrayListExtra("LocationArr");

        backPressCloseHandler = new BackPressCloseHandler(this);

        //매니저에 프레그먼트 추가
        adapter.addFragment(new fragment_main());
        adapter.addFragment(new fragment_review_list());
        adapter.addFragment(new fragment_search());

        //뷰페이저와 어댑터 연결
        viewPager.setAdapter(adapter);

        //탭과 뷰페이저 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("추천 가게");
        tabLayout.getTabAt(1).setText("리뷰");
        tabLayout.getTabAt(2).setText("검색");

        //우측 상단에 마이페이지로 이동하는 버튼을 누르면 전면 광고가 뜨도록 설정
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    Intent intent = new Intent(MainActivity.this, ThanksActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    //광고 생성!!!!!!!!@!!!!
    private void createPersonalizeAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }

    private void createInterstitialAd (AdRequest adRequest) {

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        Intent intent = new Intent(MainActivity.this, ThanksActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }
    // 광고 끝!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    /* @Override
    protected void onStop() {
        super.onStop();
        adapter.saveState();
    } */
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

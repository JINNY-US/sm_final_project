package com.example.sm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity {

    Button go_to_setting, go_to_cost_setting, go_to_logout, go_to_seller_setting, go_to_product_delete;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

   // public static boolean userType;

    String userType;
    boolean userType_bool;

    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar tb = findViewById(R.id.mypage_toolbar);
        setSupportActionBar(tb);

        go_to_setting = findViewById(R.id.go_to_setting);
        go_to_cost_setting = findViewById(R.id.go_to_cost_setting);
        go_to_logout = findViewById(R.id.go_to_logout);
        go_to_seller_setting = findViewById(R.id.go_to_seller_setting);
        go_to_product_delete = findViewById(R.id.go_to_product_delete);

        SharedPreferences sharedPreferences= getSharedPreferences("sFile", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userType = sharedPreferences.getString("userType", "");
        userType_bool = Boolean.parseBoolean(userType);

        if (userType_bool) {
            go_to_cost_setting.setVisibility(View.VISIBLE);
            go_to_seller_setting.setVisibility(View.GONE);
            go_to_product_delete.setVisibility(View.GONE);
        }
        else{
            go_to_cost_setting.setVisibility(View.GONE);
            go_to_seller_setting.setVisibility(View.VISIBLE);
            go_to_product_delete.setVisibility(View.VISIBLE);
        }



        //사용자 설정 변경 (이름, 비밀번호 등)
        go_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SettingActivity.class));
                finish();
            }
        });

        //비용 변경 (일반 사용자)
        go_to_cost_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CostSettingActivity.class));
                finish();
            }
        });


        //판매자 설정 변경 (가게 이름, 주소 등)
        go_to_seller_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SellerSettingActivity.class));
                finish();
            }
        });

        go_to_product_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "미완성- 추가 예정", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //로그아웃 함수
    public void signOut(View view) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());
        alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
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

    //뒤로가기 시 메인페이지로 이동
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent;
        if (userType_bool) {
            intent = new Intent(getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else{
            intent = new Intent(getApplication(), SellerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }

}
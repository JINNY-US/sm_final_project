package com.example.sm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
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

    Button go_to_setting, go_to_likepage, go_to_cost_setting, go_to_logout, go_to_seller_setting;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //private FirebaseAuth firebaseAuth;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar tb = findViewById(R.id.mypage_toolbar);
        setSupportActionBar(tb);



        go_to_setting = findViewById(R.id.go_to_setting);
        go_to_likepage = findViewById(R.id.go_to_likepage);
        go_to_cost_setting = findViewById(R.id.go_to_cost_setting);
        go_to_logout = findViewById(R.id.go_to_logout);
        go_to_seller_setting = findViewById(R.id.go_to_seller_setting);

        /*class mypage implements Runnable {
            @Override
            public void run() {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기
                String uid = user != null ? user.getUid() : null;

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users").child(uid).child("userType").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean userType = Boolean.parseBoolean(snapshot.getValue(String.class));
                        if (userType) {
                            *//*Toast.makeText(MypageActivity.this, "일반인으로 로그인했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                            startActivity(intent);*//*
                            finish();
                        } else {
                            go_to_seller_setting.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        }*/

        go_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SettingActivity.class));
                finish();
            }
        });


        go_to_cost_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CostSettingActivity.class));
                finish();
            }
        });

        go_to_likepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LikepageActivity.class));
                finish();
            }
        });

        go_to_seller_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SellerSettingActivity.class));
                finish();
            }
        });

    }

    public void signOut(View view) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());
        alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

}
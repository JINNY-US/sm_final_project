package com.example.sm_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    Button setting_cancle_btn, setting_change_btn;
    TextView setting_id_before;
    EditText editTextTextPersonName4, setting_pass, setting_pass_check;
    String userType, uid;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    DatabaseReference mDatabase;

    FirebaseUser user;

    @Override
    public void onBackPressed() {
        Back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        setting_cancle_btn = findViewById(R.id.setting_cancel_btn);
        setting_change_btn = findViewById(R.id.setting_change_btn);
        setting_id_before = findViewById(R.id.setting_email);
            editTextTextPersonName4 = findViewById(R.id.setting_name);
        setting_pass = findViewById(R.id.setting_pass);
        setting_pass_check = findViewById(R.id.setting_pass_check);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기
        uid = user != null ? user.getUid() : null;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(uid != null){
                    setting_id_before.setText(snapshot.child("email").getValue(String.class));
                    editTextTextPersonName4.setText(snapshot.child("name").getValue(String.class));
                    userType = snapshot.child("userType").getValue(String.class);
                }else {
                    Toast.makeText(SettingActivity.this, "회원정보를 불러올수가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        setting_cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

        setting_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextTextPersonName4.getText().toString().equals("") && !setting_pass.getText().toString().equals("") && !setting_pass_check.getText().toString().equals("")) {
                    String email = setting_id_before.getText().toString().trim();
                    String password = setting_pass.getText().toString().trim();
                    String password_check = setting_pass_check.getText().toString().trim();
                    String name = editTextTextPersonName4.getText().toString().trim();

                    if(password.equals(password_check)) {
                        HashMap<String, Object> childUpdates = new HashMap<>();

                        UserInfo userInfo = new UserInfo(email, password, uid, name, userType);
                        Map<String, Object> userValue = userInfo.toMap();
                        user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기

                        if(user != null) {
                            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingActivity.this, "수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        childUpdates.put("/Users/" + uid, userValue);
                        mDatabase.updateChildren(childUpdates);

                        startActivity(new Intent(getApplication(), MypageActivity.class));
                        finish();
                    }else{
                        Toast.makeText(SettingActivity.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, "작성하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class UserInfo {
        public String email;
        public String password;
        public String uid;
        public String name;
        public String userType;

        public UserInfo(){ }

        public UserInfo(String email ,String password, String uid, String name, String userType) {
            this.email = email;
            this.password = password;
            this.uid = uid;
            this.name = name;
            this.userType = userType;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("password", password);
            result.put("uid", uid);
            result.put("name", name);
            result.put("userType", userType);

            return result;
        }


    }

    public void Back() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("이전 화면으로 돌아갈 경우 변경 사항이 저장되지 않습니다. 돌아가시겠습니까?").setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
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
        alert.setTitle("Notice");

        //대화창 아이콘 설정
        alert.setIcon(R.drawable.exclamation);

        //대화창 배경 색 설정
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(180, 180, 180)));
        alert.show();

    }



}

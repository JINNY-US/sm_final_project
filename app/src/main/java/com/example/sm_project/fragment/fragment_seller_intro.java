package com.example.sm_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sm_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class fragment_seller_intro extends Fragment {
    String uid;
    TextView shop_name, city, town, shop_addr, shop_time;
    Button button_first;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference mDatabase;
    FirebaseUser user;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seller_intro, container, false);

        //객체 초기화
        shop_name = rootView.findViewById(R.id.shop_name);
        city = rootView.findViewById(R.id.city);
        town = rootView.findViewById(R.id.town);
        shop_addr = rootView.findViewById(R.id.shop_addr);
        shop_time = rootView.findViewById(R.id.shop_time);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기 <<<<< 수정 해야됨 >>>>>
        uid = user != null ? user.getUid() : null; // 로그인한 유저의 uid가 아니라 클릭한 가게의 uid를 넘겨받게 메뉴리스트에서 넘어 올것 같은뎀,,

        context = container.getContext();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("StoreInfo").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(uid != null){
                    shop_name.setText(snapshot.child("name").getValue(String.class));
                    city.setText(snapshot.child("city_string").getValue(String.class));
                    town.setText(snapshot.child("town_string").getValue(String.class));
                    shop_addr.setText(snapshot.child("addr").getValue(String.class));
                    shop_time.setText(snapshot.child("time").getValue(String.class));
                }else {
                    Toast.makeText(context, "회원정보를 불러올수가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return rootView;
    }
}
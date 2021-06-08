package com.example.sm_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sm_project.adapter.ListViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopPopupActivity extends Activity {
    TextView shop_name, shop_addr, shop_time, shop_intro, shop_tag;

    ListView listview;
    ListViewAdapter mAdapter;

    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef;

    private String name, addr, time, intro, uid, tag;

    List<DataSnapshot> menuArrayList = new ArrayList<DataSnapshot>();

    Drawable imagDraw;
    Handler mHandler;

    String[] tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop_popup);

        shop_name = findViewById(R.id.txt_popup_name);
        shop_addr = findViewById(R.id.txt_popup_addr);
        shop_time = findViewById(R.id.txt_popup_time);
        shop_intro = findViewById(R.id.txt_popup_intro);
        shop_tag = findViewById(R.id.txt_popup_tag);

        Resources res = getResources();
        tags = res.getStringArray(R.array.tag_list);

        //리스트뷰 참조
        listview = findViewById(R.id.listview_popup);
        //Adapter 생성
        mAdapter = new ListViewAdapter();
        //리스트뷰 Adapter 달기
        listview.setAdapter(mAdapter);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mHandler = new Handler();

        //데이터 가져오기
        Intent intent = getIntent();
        uid = intent.getStringExtra("uidStr");

        checkAnswerStoreInfo(new ShopPopupActivity.Callback() {
            @Override
            public void success(boolean storeBool) {
                if(storeBool) {
                    checkAnswerProductInfo(new ShopPopupActivity.Callback() {
                        @Override
                        public void success(boolean productBool) {
                            if (menuArrayList.size() != 0) {
                                mAdapter.clear();
                                for (int i = 0; i < menuArrayList.size(); i++) {
                                    //String img = menuArrayList.get(i).child("image").getValue(String.class);
                                    if (TextUtils.isEmpty(menuArrayList.get(i).child("image").getValue(String.class))) {
                                        mAdapter.addItem(getDrawable(R.drawable.ic_baseline_image_24),
                                                menuArrayList.get(i).child("name").getValue(String.class), menuArrayList.get(i).child("price").getValue(Long.class).toString(), menuArrayList.get(i).child("uid").getValue(String.class));
                                        mAdapter.notifyDataSetChanged();
                                    }else {
                                        int num = i;
                                        drawableFromUri(new ShopPopupActivity.Callback(){
                                            @Override
                                            public void success(boolean bool) {
                                                if(bool==true) {
                                                    if (imagDraw != null) {
                                                        Log.e("image", "contain");
                                                        mAdapter.addItem(imagDraw, menuArrayList.get(num).child("name").getValue(String.class), Long.toString(menuArrayList.get(num).child("price").getValue(Long.class)), menuArrayList.get(num).child("uid").getValue(String.class));
                                                        mAdapter.notifyDataSetChanged();
                                                    } else {
                                                        Log.e("image", "null");
                                                        mAdapter.addItem(getDrawable(R.drawable.ic_baseline_image_24),
                                                                menuArrayList.get(num).child("name").getValue(String.class), menuArrayList.get(num).child("price").getValue(Long.class).toString(), menuArrayList.get(num).child("uid").getValue(String.class));
                                                        mAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }, menuArrayList.get(i).child("image").getValue(String.class));
                                    }
                                }
                                Toast.makeText(ShopPopupActivity.this, name + "의 가게정보입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShopPopupActivity.this, "등록된 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    public interface Callback{
        void success(boolean bool);
    }

    private void tagTransString(String tag) {
        String txt_popup_tag = "";
        String[] num_array;
        if (tag != null) {
            num_array = tag.split(",");
            for (int i = 0; i < num_array.length; i++) {
                int a = Integer.parseInt(num_array[i]);
                txt_popup_tag = txt_popup_tag + "#"+ tags[i]+ " ";
            }
            shop_tag.setText(txt_popup_tag);
        }
    }

    private void checkAnswerStoreInfo(@NonNull ShopPopupActivity.Callback finishedCallback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("StoreInfo").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (uid != null) {
                    name = snapshot.child("name").getValue(String.class);
                    addr = snapshot.child("addr").getValue(String.class);
                    time = snapshot.child("time").getValue(String.class);
                    intro = snapshot.child("intro").getValue(String.class);
                    tagTransString(tag);
                    shop_name.setText(name);
                    shop_addr.setText(addr);
                    shop_time.setText(time);
                    shop_intro.setText(intro);
                    finishedCallback.success(true);
                }else{
                    shop_name.setText("가게 정보를 받아오지 못 했습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkAnswerProductInfo(@NonNull ShopPopupActivity.Callback finishedCallback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("ProductInfo").orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    menuArrayList.add(data);
                }
                finishedCallback.success(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void drawableFromUri (@NonNull ShopPopupActivity.Callback finishedCallback, String imag) {
        String addr = "Products/" + imag;
        storageRef.child(addr).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("draw", "success");
                Glide.with(ShopPopupActivity.this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imagDraw = new BitmapDrawable(resource);
                    }
                });
                finishedCallback.success(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("draw", "fail");
                Toast.makeText(ShopPopupActivity.this, "해당 상품의 이미지를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
//        Intent intent = new Intent();
//        intent.putExtra("result", "Close Popup");
//        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
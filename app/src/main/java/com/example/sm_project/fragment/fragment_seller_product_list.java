package com.example.sm_project.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sm_project.ProductAddActivity;
import com.example.sm_project.R;
import com.example.sm_project.ShopPopupActivity;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class fragment_seller_product_list extends Fragment {
    String uid, callBack;
    List<DataSnapshot> menuArrayList = new ArrayList<DataSnapshot>();
    Drawable imagDraw;

    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageRef;

    ListView listview;
    ListViewAdapter mAdapter;

    private Context context;
    Handler mHandler;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seller_product_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser(); //로그인한 유저의 정보 가져오기
        uid = user != null ? user.getUid() : null;

        //리스트뷰 참조
        listview = rootView.findViewById(R.id.listview_product);
        //Adapter 생성
        mAdapter = new ListViewAdapter();
        //리스트뷰 Adapter 달기
        listview.setAdapter(mAdapter);

        context = container.getContext();
        mHandler = new Handler();

        checkAnswerSubmission(new Callback() {
            @Override
            public void success(boolean bool) {
                if (bool){
                    if (menuArrayList.size() != 0) {
                        mAdapter.clear();
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            String img = menuArrayList.get(i).child("image").getValue(String.class);
                            if (TextUtils.isEmpty(img)) {
                                mAdapter.addItem(context.getDrawable(R.drawable.ic_baseline_image_24),
                                        menuArrayList.get(i).child("name").getValue(String.class), menuArrayList.get(i).child("price").getValue(Long.class).toString(), menuArrayList.get(i).child("uid").getValue(String.class));
                                //Toast.makeText(context, "등록된 상품 리스트입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                int num = i;
                                drawableFromUri(new fragment_seller_product_list.Callback() {
                                    @Override
                                    public void success(boolean bool) {
                                        if(imagDraw != null) {
                                            mAdapter.addItem(imagDraw, menuArrayList.get(num).child("name").getValue(String.class), Long.toString(menuArrayList.get(num).child("price").getValue(Long.class)), menuArrayList.get(num).child("uid").getValue(String.class));
                                        }else{mAdapter.addItem(context.getDrawable(R.drawable.ic_baseline_image_24),
                                                menuArrayList.get(num).child("name").getValue(String.class), menuArrayList.get(num).child("price").getValue(Long.class).toString(), menuArrayList.get(num).child("uid").getValue(String.class));
                                        }
                                    }
                                }, menuArrayList.get(i).child("image").getValue(String.class));
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "등록된 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rootView.findViewById(R.id.ActionButton_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductAddActivity.class));
            }
        });

        return rootView;
    }

    public interface Callback{
        void success(boolean bool);
    }

    private void checkAnswerSubmission(@NonNull Callback finishedCallback) {
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


    public void drawableFromUri (@NonNull fragment_seller_product_list.Callback finishedCallback, String imag) {
        String addr = "Products/" + imag;
        storageRef.child(addr).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("draw", "success");
                Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
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
                Toast.makeText(context, "해당 상품의 이미지를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.example.sm_project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.sm_project.activity.WritePostActivity;
import com.example.sm_project.adapter.HomeAdapter;
import com.example.sm_project.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class fragment_shop_list extends Fragment {

    Button go_mypage, sellerSetting, sellerIntro, go_productadd;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private HomeAdapter homeAdapter;
    private ArrayList<PostInfo> postList;
    private boolean updating;
    private boolean topScrolled;

    public fragment_shop_list() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop_list, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), postList);
        homeAdapter.setOnPostListener(onPostListener);

        //getView().findViewById(R.id.go_mypage).setOnClickListener(View.OnClickListener);

        return v;

    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
        homeAdapter.playerStop();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_mypage:
                    myStartActivity(MypageActivity.class);
                    break;
                case R.id.login_btn:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.sellerIntro:
                    myStartActivity(MypageActivity.class);
                    break;
                case R.id.sellerSetting:
                    myStartActivity(MypageActivity.class);
                    break;
                case R.id.go_productadd:
                    myStartActivity(MypageActivity.class);
                    break;

            }
        }
    };

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            postList.remove(postInfo);
            homeAdapter.notifyDataSetChanged();

            Log.e("로그: ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그: ","수정 성공");
        }
    };


    private void postsUpdate(final boolean clear) {
        updating = true;
        Date date = postList.size() == 0 || clear ? new Date() : postList.get(postList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                postList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(new PostInfo(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>) document.getData().get("contents"),
                                        (ArrayList<String>) document.getData().get("formats"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId()));
                            }
                            homeAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }


    /*public void signOut(View view) {
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

    }*/
}



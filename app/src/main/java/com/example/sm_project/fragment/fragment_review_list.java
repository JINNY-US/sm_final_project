package com.example.sm_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Context;

import android.widget.ListView;
import android.widget.Toast;

import com.example.sm_project.R;
import com.example.sm_project.activity.WritePostActivity;
import com.example.sm_project.adapter.HomeAdapter;
import com.example.sm_project.adapter.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragment_review_list extends Fragment {

    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private HomeAdapter homeAdapter;
    List<DataSnapshot> userList = new ArrayList<DataSnapshot>();
    List<DataSnapshot> userContentList = new ArrayList<DataSnapshot>();
    List<DataSnapshot> contentList = new ArrayList<DataSnapshot>();
    private boolean updating;
    private boolean topScrolled;

    List<String> userKey = new ArrayList<String>();
    List<String> contentKey = new ArrayList<String>();

    ListView listview;
    ListViewAdapter mAdapter;

    DatabaseReference mDatabase;

    private Context context;

    public fragment_review_list() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review_list, container, false);

        Handler mHandler = new Handler();

        //리스트뷰 참조
        listview = rootView.findViewById(R.id.ListView_review);
        //Adapter 생성
        mAdapter = new ListViewAdapter();
        //리스트뷰 Adapter 달기
        listview.setAdapter(mAdapter);

        context = container.getContext();
        mHandler = new Handler();

        rootView.findViewById(R.id.ActionButton_review).setOnClickListener(onClickListener);

        checkAnswerUser(new fragment_review_list.Callback(){
            @Override
            public void success(boolean bool) {
                if(bool) {
                    if (userList.size() != 0) {
                        mAdapter.clear();
                        for (int i = 0; i<userList.size(); i++) {
                            int num = i;
                            checkAnswerContent(new fragment_review_list.Callback() {
                                @Override
                                public void success(boolean bool) {
                                    if(bool) {
                                        int n = num;
                                        if (contentKey.get(n) != null) {
                                            mAdapter.addItem(context.getDrawable(R.drawable.ic_baseline_image_24),
                                                    userKey.get(n), contentKey.get(n), "");
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }, userKey.get(i));
                        }}

                }
            }
        });


        return rootView;
    }

    public interface Callback{
        void success(boolean bool);
    }

    private void checkAnswerUser(@NonNull fragment_review_list.Callback finishedCallback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    userList.add(data);
                    userKey.add(data.getKey());
                }
                finishedCallback.success(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkAnswerContent(@NonNull fragment_review_list.Callback finishedCallback, String user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Board").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userContentList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    userContentList.add(data);
                    contentKey.add(data.getKey());
                }
                finishedCallback.success(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), WritePostActivity.class));
        }
    };


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
        //homeAdapter.playerStop();
    }

}
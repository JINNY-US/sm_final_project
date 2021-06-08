package com.example.sm_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sm_project.CostSetPopupActivity;
import com.example.sm_project.ListViewItem;
import com.example.sm_project.R;
import com.example.sm_project.ShopPopupActivity;
import com.example.sm_project.adapter.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_main extends Fragment {
    private DatabaseReference databaseReference, reference;

    String loc1;
    Bundle bundle;
    ListView listview;
    ListViewAdapter adapter;
    private Context context;

    ArrayList<DataSnapshot> restaurantList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> cafeList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> barList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> storeList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> etcList = new ArrayList<DataSnapshot>();
    ArrayList<ArrayList<DataSnapshot>> allStoreList = new ArrayList<ArrayList<DataSnapshot>>();

    ArrayList<DataSnapshot> restaurantPriceList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> cafePriceList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> barPriceList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> storePriceList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> etcPriceList = new ArrayList<DataSnapshot>();
    ArrayList<ArrayList<DataSnapshot>> allPriceList = new ArrayList<ArrayList<DataSnapshot>>();

    ArrayList<DataSnapshot> compareList = new ArrayList<DataSnapshot>();
    ArrayList<DataSnapshot> resultList = new ArrayList<DataSnapshot>();
    ArrayList<String> locGetExtra = new ArrayList<String>();
    ArrayList<Integer> priceGetExtra = new ArrayList<Integer>();

    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        bundle = getArguments();
        context = container.getContext();

        Handler mHandler = new Handler();

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(R.id.storeList);
        listview.setAdapter(adapter);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        priceGetExtra.add(sharedPreferences.getInt("cost_rest", 0));
        priceGetExtra.add(sharedPreferences.getInt("cost_cafe", 0));
        priceGetExtra.add(sharedPreferences.getInt("cost_shop", 0));
        priceGetExtra.add(sharedPreferences.getInt("cost_bar", 0));
        priceGetExtra.add(sharedPreferences.getInt("cost_etc", 0));

        locGetExtra.add(sharedPreferences.getString("set_city", ""));
        locGetExtra.add(sharedPreferences.getString("set_town", ""));

        loc1 = locGetExtra.get(0);
        checkAnswerTypeList(new fragment_main.Callback() {
            @Override
            public void success(boolean typeBool) {
                if (typeBool) {
                    Log.e("Time Stamp", "typeList add success");

                    for (int i =0; i < priceGetExtra.size();i++) {
                        if (!priceGetExtra.get(i).equals(0)){
                            switch (i){
                                case 0: restaurantPriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } } }, "식당", priceGetExtra.get(0) );
                                    break;
                                case 1: cafePriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } } }, "카페", priceGetExtra.get(1) );
                                    break;
                                case 2: barPriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "주점", priceGetExtra.get(2) );
                                    break;
                                case 3: storePriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "각종가게", priceGetExtra.get(3) );
                                    break;
                                case 4: etcPriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "기타", priceGetExtra.get(4) );
                                    break;
                                default: break;
                            }
                        }
                    }

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            Log.e("Time Stamp", "priceList add success");
                            allStoreList.add(restaurantList);
                            allStoreList.add(cafeList);
                            allStoreList.add(barList);
                            allStoreList.add(storeList);
                            allStoreList.add(etcList);

                            allPriceList.add(restaurantPriceList);
                            allPriceList.add(cafePriceList);
                            allPriceList.add(barPriceList);
                            allPriceList.add(storePriceList);
                            allPriceList.add(etcPriceList);

                            compareList.clear();
                            String toast = "";
                            //<<<<<<<<<<<<<<<<<<<없다는 토스트 뽑아줄 스트링 클리어해주기>>>>>>>>>>>>>
                            for (int i = 0; i < 5; i++) {
                                if (!priceGetExtra.get(i).equals(0)) {
                                    if (allStoreList.get(i).size() != 0 && allPriceList.get(i).size() != 0) {
                                        for (int j = 0; j < allStoreList.get(i).size(); j++) {
                                            for (int k = 0; k < allPriceList.get(i).size(); k++) {
                                                if (allStoreList.get(i).get(j).child("uid").getValue(String.class).equals(allPriceList.get(i).get(k).child("uid").getValue(String.class))) {
                                                    compareList.add(allStoreList.get(i).get(j));
                                                }
                                            }
                                        }
                                    }else{
                                        switch (i){
                                            case 0 : toast.concat(" 식당"); break;
                                            case 1 : toast.concat(" 카페"); break;
                                            case 2 : toast.concat(" 주점"); break;
                                            case 3 : toast.concat(" 각종가게"); break;
                                            case 4 : toast.concat(" 기타"); break;
                                        }
                                    }

                                    if (!toast.equals("")){
                                        Toast.makeText(context, "해당하는" + toast +"이(가) 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            Log.e("Time Stamp", "compare success result ing");
                            // 결과 리스트 들 중복 없이 보여주기
                            if (compareList.size() != 0) {
                                adapter.clear();
                                resultList.clear();
                                for (DataSnapshot item : compareList) {
                                    if (!resultList.contains(item)) {
                                        resultList.add(item);
                                        adapter.addItem(ContextCompat.getDrawable(context, R.drawable.baseline_account_circle_black_24dp),
                                                item.child("name").getValue(String.class), item.child("intro").getValue(String.class), item.child("uid").getValue(String.class));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "여기 어때?", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "해당하는 가게가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 5000);
                }
            }
        });

        //listview 클릭 이벤트 핸들러 정의
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get item
                ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(i);
                String uidStr = item.getUid();

                Intent intent = new Intent(getContext(), CostSetPopupActivity.class);
                intent.putExtra("uidStr", uidStr);
                startActivityForResult(intent, 1);

            }
        });

        return rootView;
    }

    public interface Callback{
        void success(boolean bool);
    }

    private void checkAnswerTypeList(@NonNull fragment_main.Callback finishedCallback) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("StoreInfo").orderByChild("city_string").equalTo(locGetExtra.get(0)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null) {
                    restaurantList.clear();
                    cafeList.clear();
                    barList.clear();
                    storeList.clear();
                    etcList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.child("town_string").getValue(String.class).equals(locGetExtra.get(1))) {
                            switch (data.child("type_string").getValue(String.class)) {
                                case "식당":
                                    restaurantList.add(data);
                                    break;
                                case "카페":
                                    cafeList.add(data);
                                    break;
                                case "주점":
                                    barList.add(data);
                                    break;
                                case "각종 가게(옷, 신발, 화장품, 잡화 등)":
                                    storeList.add(data);
                                    break;
                                case "기타(노래방, 이색 카페 등)":
                                    etcList.add(data);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    Log.e("Time Stamp", "typeList add ing");
                    finishedCallback.success(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkAnswerPriceList (@NonNull fragment_main.Callback finishedCallback, @NonNull String type, @NonNull int price) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("ProductInfo").orderByChild("price").endAt(price).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null) {
                    Log.e("Time Stamp", "priceList add ing");

                    for (DataSnapshot data : snapshot.getChildren()) {
                        switch (type) {
                            case "식당":
                                restaurantPriceList.add(data);
                                break;
                            case "카페":
                                cafePriceList.add(data);
                                break;
                            case "주점":
                                barPriceList.add(data);
                                break;
                            case "각종가게":
                                storePriceList.add(data);
                                break;
                            case "기타":
                                etcPriceList.add(data);
                                break;
                            default:
                                break;
                        }
                    }
                    finishedCallback.success(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
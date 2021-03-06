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

        // Adapter ??????
        adapter = new ListViewAdapter();

        // ???????????? ?????? ??? Adapter??????
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
                                        public void success(boolean bool) { if (bool) { } } }, "??????", priceGetExtra.get(0) );
                                    break;
                                case 1: cafePriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } } }, "??????", priceGetExtra.get(1) );
                                    break;
                                case 2: barPriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "??????", priceGetExtra.get(2) );
                                    break;
                                case 3: storePriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "????????????", priceGetExtra.get(3) );
                                    break;
                                case 4: etcPriceList.clear();
                                    checkAnswerPriceList(new fragment_main.Callback() {
                                        @Override
                                        public void success(boolean bool) { if (bool) { } }}, "??????", priceGetExtra.get(4) );
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
                            //<<<<<<<<<<<<<<<<<<<????????? ????????? ????????? ????????? ??????????????????>>>>>>>>>>>>>
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
                                            case 0 : toast.concat(" ??????"); break;
                                            case 1 : toast.concat(" ??????"); break;
                                            case 2 : toast.concat(" ??????"); break;
                                            case 3 : toast.concat(" ????????????"); break;
                                            case 4 : toast.concat(" ??????"); break;
                                        }
                                    }

                                    if (!toast.equals("")){
                                        Toast.makeText(context, "????????????" + toast +"???(???) ????????????.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            Log.e("Time Stamp", "compare success result ing");
                            // ?????? ????????? ??? ?????? ?????? ????????????
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
                                Toast.makeText(context, "?????? ???????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 5000);
                }
            }
        });

        //listview ?????? ????????? ????????? ??????
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
                                case "??????":
                                    restaurantList.add(data);
                                    break;
                                case "??????":
                                    cafeList.add(data);
                                    break;
                                case "??????":
                                    barList.add(data);
                                    break;
                                case "?????? ??????(???, ??????, ?????????, ?????? ???)":
                                    storeList.add(data);
                                    break;
                                case "??????(?????????, ?????? ?????? ???)":
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
                            case "??????":
                                restaurantPriceList.add(data);
                                break;
                            case "??????":
                                cafePriceList.add(data);
                                break;
                            case "??????":
                                barPriceList.add(data);
                                break;
                            case "????????????":
                                storePriceList.add(data);
                                break;
                            case "??????":
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
package com.example.sm_project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sm_project.R;
import com.example.sm_project.adapter.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_search extends Fragment {

    SearchView searchStore;
    List<DataSnapshot> searchArrayList = new ArrayList<DataSnapshot>();
    List<DataSnapshot> cityArrayList = new ArrayList<DataSnapshot>();
    List<DataSnapshot> townArrayList = new ArrayList<DataSnapshot>();
    Spinner spn_city_search, spn_town_search;
    ArrayAdapter<CharSequence> ctspin, twspin;
    Boolean c = false, t = false;

    ListView listview;
    ListViewAdapter mAdapter;

    DatabaseReference mDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        Handler mHandler = new Handler();

        searchStore = v.findViewById(R.id.searchStore);
        spn_city_search = v.findViewById(R.id.spn_city_search);
        //spn_city_search.clearFocus();
        spn_town_search = v.findViewById(R.id.spn_town_search);
        //spn_town_search.clearFocus();

        ctspin = ArrayAdapter.createFromResource(container.getContext(), R.array.city, android.R.layout.simple_spinner_dropdown_item);
        ctspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_city_search.setAdapter(ctspin);

        //리스트뷰 참조
        listview = v.findViewById(R.id.listView_search);
        //Adapter 생성
        mAdapter = new ListViewAdapter();
        //리스트뷰 Adapter 달기
        listview.setAdapter(mAdapter);

        if(savedInstanceState != null){
            spn_city_search.setSelection(getIndex(spn_city_search, savedInstanceState.getString("city")));
        }


        spn_city_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c = true;

                String selItem = (String) spn_city_search.getSelectedItem();


                switch (selItem) {
                    case "서울특별시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Seoul, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;

                    case "부산광역시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Busan, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;

                    case "대구광역시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Daegu, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "인천광역시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Incheon, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "대전광역시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Daejeon, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "울산광역시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Ulsan, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "세종특별자치시":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Sejong, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "경기도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "강원도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Gangwon, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "충청북도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Chungbuk, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "충청남도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Chungnam, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "전라북도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "전라남도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Jeonnam, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "경상북도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "경상남도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    case "제주특별자치도":
                        twspin = ArrayAdapter.createFromResource(container.getContext(), R.array.Jeju, android.R.layout.simple_spinner_dropdown_item);
                        twspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_town_search.setAdapter(twspin);
                        break;
                    default:
                        break;

                }

                fragment_search.getStoreInfo getStoreInfo = new fragment_search.getStoreInfo("city_string", adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                c = false;
            }
        });


        spn_town_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (c) {
                    t = true;
                    fragment_search.getStoreInfo getStoreInfo = new fragment_search.getStoreInfo("town_string", adapterView.getSelectedItem().toString());
                    //데이터 불러오는게 느려서 그런것같아 다 정상작동하는데 등록된 가게가 없다는 루트로 가는 것 보니
                    //townArray에 넣기 전에 밑에 거를 실행하나 봄

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (townArrayList.size() != 0) {
                                mAdapter.clear();
                                for (int j = 0; j < townArrayList.size(); j++) {
                                    mAdapter.addItem(ContextCompat.getDrawable(container.getContext(),R.drawable.baseline_account_circle_black_24dp),
                                            townArrayList.get(j).child("name").getValue(String.class), townArrayList.get(j).child("intro").getValue(String.class));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(container.getContext(), "등록된 가게가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                } else {
                    spn_town_search.clearFocus();
                    Toast.makeText(container.getContext(), "상위 지역구를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                t = false;
            }
        });


        searchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String name) {
                if (t) {
                    if (!name.equals("")) {
                        fragment_search.getStoreInfo getStoreInfo = new fragment_search.getStoreInfo("name", name);

                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (searchArrayList.size() != 0) {
                                    mAdapter.clear();
                                    for (int i = 0; i < searchArrayList.size(); i++) {
                                        // 아이템 추가
                                          mAdapter.addItem(ContextCompat.getDrawable(container.getContext(), R.drawable.baseline_account_circle_black_24dp),
                                               searchArrayList.get(i).child("name").getValue(String.class), searchArrayList.get(i).child("intro").getValue(String.class));
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(container.getContext(), "검색되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(container.getContext(), "해당되는 가게가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 1000);
                    } else {
                        Toast.makeText(container.getContext(), "검색할 단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(container.getContext(), "하위 지역구를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return v;
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
    }





    public class getStoreInfo {
        public getStoreInfo(String key, String val) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("StoreInfo").orderByChild(key).startAt(val).endAt(val + "\uf8ff").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (key == "city_string") {
                        cityArrayList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            cityArrayList.add(data);
                            //introArrayList.add(data.child("intro").getValue(String.class));
                        }
                    } else if (key == "town_string") {
                        townArrayList.clear();
                        for (DataSnapshot c_data : cityArrayList) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if (c_data.child("uid").getValue(String.class).equals(data.child("uid").getValue(String.class))) {
                                    townArrayList.add(c_data);
                                }
                            }
                        }
                    } else if (key == "name") {
                        searchArrayList.clear();
                        for (DataSnapshot t_data : townArrayList) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if (t_data.child("uid").getValue(String.class).equals(data.child("uid").getValue(String.class))) {
                                    searchArrayList.add(t_data);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}
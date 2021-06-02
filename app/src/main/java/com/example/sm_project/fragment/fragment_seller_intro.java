package com.example.sm_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sm_project.R;
import com.google.protobuf.LazyStringArrayList;

public class fragment_seller_intro extends Fragment {
    Spinner sp_city, sp_town;
    ArrayAdapter<CharSequence> adapter;
    boolean mInitSpinner;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_intro, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp_city = (Spinner)getView().findViewById(R.id.spinner_city);
        sp_town = (Spinner)getView().findViewById(R.id.spinner_town);

//        adapter = ArrayAdapter. createFromResource( this,
//                R.array.city , R.layout.simple_spinner_item );

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(fragment_seller_intro.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}
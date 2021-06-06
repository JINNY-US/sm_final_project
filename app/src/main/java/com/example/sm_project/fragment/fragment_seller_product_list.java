package com.example.sm_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sm_project.ProductAddActivity;
import com.example.sm_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class fragment_seller_product_list extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seller_product_list, container, false);

        view.findViewById(R.id.ActionButton_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductAddActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.ActionButton_product).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ProductAddActivity.class));
//            }
//        });


   // }
}
package com.example.sm_project;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends androidx.fragment.app.Fragment {
    @Override
    public android.view.View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            android.os.Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    public void onViewCreated(@androidx.annotation.NonNull android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
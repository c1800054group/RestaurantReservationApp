package com.example.peggytsai.restaurantreservationapp.Check;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;

public class CheckWaiterFragment extends Fragment {

    private BottomNavigationView navigationView;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_waiter, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_CheckWaiter);

        navigationView = getActivity().findViewById(R.id.Navigation);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigate_menu_waiter);

        return view;
    }
}
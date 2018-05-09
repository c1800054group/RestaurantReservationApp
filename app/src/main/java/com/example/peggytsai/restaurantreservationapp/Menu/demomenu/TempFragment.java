package com.example.peggytsai.restaurantreservationapp.Menu.demomenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentShow;
import com.example.peggytsai.restaurantreservationapp.R;


public class TempFragment extends Fragment {

    private TextView tt_toolbar;
    private TextView tt_action;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_show_menu, container, false);



        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        tt_action = view.findViewById(R.id.tvMemberLogOut);

        tt_toolbar.setText("訂位/點餐");
        tt_action.setText("點餐");
        tt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.switchFragment(new CartFragmentShow(), getActivity(), true);

            }
        });




        return view;
    }





}

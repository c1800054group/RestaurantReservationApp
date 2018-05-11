package com.example.peggytsai.restaurantreservationapp.Cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;

public class CartFragmentConfirmText extends Fragment {

    private TextView tt_toolbar;
    private TextView btCartText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("訂單完成");

        View view = inflater.inflate(R.layout.fragment_cart_confirm_text, container, false);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btCartText = view.findViewById(R.id.btCartText);

        tt_toolbar.setText("內用/外帶/外送");
        btCartText.setText("繼續訂單");
        btCartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                getFragmentManager().beginTransaction().replace(R.id.main_activty, new CartFragmentConfirmText()).commit();
            }
        });

        return view;


    }

    public void KeyDown() {
//        getFragmentManager().beginTransaction().replace(R.id.main_activty, new CartFragmentConfirmText()).commit();
        Common.switchFragment(new CartFragmentConfirmText(), getActivity(), true);
    }

}

package com.example.peggytsai.restaurantreservationapp.Cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuAdapter;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

public class CartFragmentSub extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private Button bt_cart_main;
    private Button bt_cart_sub;
    private Button bt_cart_submit;   //git test

    private TextView tt_toolbar;
    private TextView tt_action;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("菜單2");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        if(Common.MENU_list.size()!=0){
            menus_list = Common.MENU_list.get(1);
        }else {
            menus_list = new ArrayList<>();//空值
        }

        recyclerView = view.findViewById(R.id.recyceleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(new MenuAdapter(menus_list, getContext()));   //連接器 需要  資料 跟 容器(view)

        findbutton(view);



        return view;


    }



    private void findbutton(View view) {
//        bt_cart_main = view.findViewById(R.id.bt_cart_main);
//        bt_cart_sub = view.findViewById(R.id.bt_cart_sub);

//        bt_cart_main.setOnClickListener(this);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        tt_action = view.findViewById(R.id.tvMemberLogOut);

//        tt_toolbar.setText("內用/外帶/外送");
//        tt_action.setText("結帳");
//        tt_action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Use_fragment useFragment = new Use_fragment();
//                useFragment.use_fragment(new CartFragmentAdd(), getFragmentManager());
//            }
//        });

    }

    @Override
    public void onClick(View v) {

//        Use_fragment useFragment = new Use_fragment();

//        switch (v.getId()) {
//            case R.id.bt_cart_main:
//
//                useFragment.use_fragment(new CartFragmentMain(), getFragmentManager());
//
//                break;
//            case R.id.bt_cart_sub:
//
//                break;
//            case R.id.bt_cart_submit:
//                useFragment.use_fragment(new CartFragmentAdd(), getFragmentManager());
//                break;
//
//        }

    }

}

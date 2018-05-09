package com.example.peggytsai.restaurantreservationapp.Cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuAdapter;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;



public class CartFragmentAdd extends Fragment {

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private TextView tt_toolbar;
    private TextView btMenuAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("菜單追加");
        View view = inflater.inflate(R.layout.fragment_cart_add, container, false);

        if(Common.MENU_list.size()!=0){
            menus_list = Common.MENU_list.get(2);
        }else {
            menus_list = new ArrayList<>();//空值
        }

        recyclerView = view.findViewById(R.id.recyceleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(new MenuAdapter(menus_list, getContext()));

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btMenuAdd = view.findViewById(R.id.btMenuAdd);

        tt_toolbar.setText("優惠加購");
        btMenuAdd.setText("下一步");
        btMenuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.switchFragment(new CartFragmentConfirm(), getActivity(), true);

            }
        });


        return view;


    }






}

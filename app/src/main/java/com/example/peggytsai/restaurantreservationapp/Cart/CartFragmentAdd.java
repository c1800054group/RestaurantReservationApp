package com.example.peggytsai.restaurantreservationapp.Cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Cart.menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Cart.menu.MenuAdapter;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;



public class CartFragmentAdd extends Fragment {

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private TextView tt_toolbar;
    private TextView btMenuAdd;


    private SwipeRefreshLayout swipeRefreshLayout;
    private LocalBroadcastManager broadcastManager;
    private IntentFilter stockFilter;


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


        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        flash();

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btMenuAdd = view.findViewById(R.id.btMenuAdd);


        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        stockFilter = new IntentFilter("stock");    //ChatWebSocketClient
        StockReceiver stockReceiver = new StockReceiver(view);
        broadcastManager.registerReceiver(stockReceiver, stockFilter);

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


    private class StockReceiver extends BroadcastReceiver {
        private View view;
        public StockReceiver(View view) {
            this.view = view;
        }
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");
            if(  message.equals("notifyDataSetChanged")  ){

                veiw_set(context);

            }

        }
    }


    private void veiw_set(Context context){
        menus_list = Common.MENU_list.get(2);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));//MainActivity.this
        recyclerView.setAdapter(new MenuAdapter(menus_list, context));
    }

    private void flash() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true); //開啟刷新

                menus_list = Common.MENU_list.get(2);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
                recyclerView.setAdapter(new MenuAdapter(menus_list, getContext()));

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}

package com.example.peggytsai.restaurantreservationapp.Cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuAdapter;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.List;


public class CartFragmentMain extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<Menu> menus_list;

    private Button bt_cart_main;
    private Button bt_cart_sub;
    private Button bt_cart_submit;
    private TextView tt_toolbar;
    private TextView tt_action;


    private final static String TAG = "Mainfragment"; //log用

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        getActivity().setTitle("菜單1");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);



        if(Common.MENU_list.size()!=0){
            menus_list = Common.MENU_list.get(0);
            recyclerView = view.findViewById(R.id.recyceleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(  new MenuAdapter(menus_list, getContext()));
        }else {
            list_connect(view);
        }

        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true); //開啟刷新
                recyclerView.setAdapter(  new MenuAdapter(menus_list, getContext()));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        findbutton1(view);

        return view;

    }

    private void list_connect(View view) {

        if (Common.networkConnected(getActivity())) {//檢查網路連線

            String url = Common.URL + "/MenuServlet";
            try {

                Common.MENU_list = new MenuGetAllTask().execute(url).get();
                menus_list = Common.MENU_list.get(0);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (menus_list == null || menus_list.isEmpty()) {
                Common.showToast(getActivity(), "text_NoCategoriesFound");
                //連不到 帶入本機 項目 但無法下單 僅能觀看餐點項目 並要求更新 新版本的清單列表
//                item_list2 = getitem();

            } else {
                //連線到 將清單 儲存至頁面供 該頁面上所有ftagment 使用

                //UI 顯示
                recyclerView = view.findViewById(R.id.recyceleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter( new MenuAdapter(menus_list, getContext()));
            }
        } else {
            Common.showToast(getActivity(), "text_NoNetwork");
//            item_list2 = getitem();
        }
        //都做

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


//        if (MenuGetImageTask != null) {
//            MenuGetImageTask.cancel(true);
//            MenuGetImageTask = null;
//        }

    }



    @Override
    public void onClick(View v) {


//        Use_fragment useFragment = new Use_fragment();

        switch (v.getId()) {

//            case R.id.bt_cart_main:
//
//                break;
//            case R.id.bt_cart_sub:
//
//                useFragment.use_fragment(new CartFragmentSub(), getFragmentManager());
//                //做事
//
//                break;
//            case R.id.bt_cart_submit:
//                useFragment.use_fragment(new CartFragmentAdd(), getFragmentManager());
//                break;

        }


    }


    private void findbutton1(View view) {
//        bt_cart_main = view.findViewById(R.id.bt_cart_main);
//        bt_cart_sub = view.findViewById(R.id.bt_cart_sub);

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

//        bt_cart_main.setOnClickListener(this);
//        bt_cart_sub.setOnClickListener(this);

    }


}

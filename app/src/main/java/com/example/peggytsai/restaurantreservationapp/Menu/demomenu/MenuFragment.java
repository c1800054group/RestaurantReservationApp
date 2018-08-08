package com.example.peggytsai.restaurantreservationapp.Menu.demomenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Page;
import com.example.peggytsai.restaurantreservationapp.Menu.Socket;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

//MenuFragment
public class MenuFragment extends Fragment {

    private TextView tt_toolbar;
    private View view;
    private TextView btMenuShowMenu;
    private MyPagerAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LocalBroadcastManager broadcastManager;
    private IntentFilter stockFilter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_show_menu, container, false);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        tt_toolbar.setText("菜單");
        adapter = new MyPagerAdapter(getChildFragmentManager(), new MenuFragmentMain(), new MenuFragmentSub());

        btMenuShowMenu = view.findViewById(R.id.btMenuShowMenu);
        btMenuShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.switchFragment(new OrderFragment(), getActivity(), true);

            }
        });


        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        stockFilter = new IntentFilter("stock");    //ChatWebSocketClient
        StockReceiver stockReceiver = new StockReceiver(view);
        broadcastManager.registerReceiver(stockReceiver, stockFilter);

        flash();
        veiw_set();

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


                veiw_set();

//                Common.showToast(getActivity(),message);
            }

        }
    }

    private void flash() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true); //開啟刷新

                veiw_set();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void veiw_set() {

//        if (Common.networkConnected(getActivity())) {//檢查網路連線
//
//            String url = Common.URL + "/MenuServlet";
//            try {
//
//                Common.MENU_list = new MenuGetAllTask().execute(url).get();
//
//            } catch (Exception e) {
//
//            }
//
//        } else {
//            Common.showToast(getActivity(), "text_NoNetwork");
//        }


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenuLayout);
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String memberName = String.valueOf(  pref.getInt("memberID",0)    );

        Socket.connectServer(getActivity(),memberName);
//        Common.showToast(getActivity(),memberName);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager, Fragment a, Fragment b) {
            super(fragmentManager);



            pageList = new ArrayList<>();
            pageList.add(new Page(a, "主餐"));
            pageList.add(new Page(b, "附餐"));

        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }




}

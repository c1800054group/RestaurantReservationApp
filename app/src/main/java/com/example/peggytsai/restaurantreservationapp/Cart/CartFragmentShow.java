package com.example.peggytsai.restaurantreservationapp.Cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Page;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;


public class CartFragmentShow extends Fragment {

    private TextView tt_toolbar;
    private TextView btMenuShowMenu;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_show_menu, container, false);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        btMenuShowMenu = view.findViewById(R.id.btMenuShowMenu);
        btMenuShowMenu.setText("下一步");
        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);

        //Common.FragmentSwitch    //為了分別顯示 已改變 標題
        switch (1){

            case 1:
                tt_toolbar.setText("內用");
                break;
            case 2:
                tt_toolbar.setText("外帶");
                break;
            case 3:
                tt_toolbar.setText("外送");
                break;

            default:
                break;
        }
        btMenuShowMenu.setText("下一步");



        btMenuShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.switchFragment(new CartFragmentAdd(), getActivity(), true);
            }
        });





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new CartFragmentMain(), "主餐"));
            pageList.add(new Page(new CartFragmentSub(), "附餐"));

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

package com.example.peggytsai.restaurantreservationapp.Menu.demomenu;

import android.graphics.Color;
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
import android.widget.Button;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Page;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

//MenuFragment
public class MenuFragment extends Fragment {

    private TextView tt_toolbar;
    private View view;
    private TextView btMenuShowMenu;
    private MyPagerAdapter adapter;

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


        veiw_set();

        return view;
    }

    private void veiw_set() {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
        viewPager.setAdapter(adapter);  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenuLayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onStart() {
        super.onStart();


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

package com.example.peggytsai.restaurantreservationapp.Check;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;

public class CheckWaiterTabFragment extends Fragment{
    public static ViewPager vpService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiter,container,false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_CheckWaiter);

        TabLayout tlService = view.findViewById(R.id.tlService);
        tlService.addTab(tlService.newTab().setText("未送餐"));
        tlService.addTab(tlService.newTab().setText("已送餐"));
        tlService.setTabGravity(TabLayout.GRAVITY_FILL);

        vpService = view.findViewById(R.id.vpService);
        CheckWaiterTabPagerAdapter checkWaiterTabPagerAdapter = new CheckWaiterTabPagerAdapter
                (getActivity().getSupportFragmentManager(),tlService.getTabCount());
        vpService.setAdapter(checkWaiterTabPagerAdapter);

        vpService.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlService));
        tlService.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpService.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;
    }
}

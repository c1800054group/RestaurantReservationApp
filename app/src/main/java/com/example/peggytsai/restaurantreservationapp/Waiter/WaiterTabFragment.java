package com.example.peggytsai.restaurantreservationapp.Waiter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;

public class WaiterTabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiter, container, false);


        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_ServiceWaiter);

        TabLayout tlService = view.findViewById(R.id.tlService);
        tlService.addTab(tlService.newTab().setText("未服務"));
        tlService.addTab(tlService.newTab().setText("已服務"));
        tlService.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager vpService = view.findViewById(R.id.vpService);
        WaiterTabPagerAdapter waiterTabPagerAdapter = new WaiterTabPagerAdapter
                (getActivity().getSupportFragmentManager(),tlService.getTabCount());

//        vpService.setOffscreenPageLimit(1);
        vpService.setAdapter(waiterTabPagerAdapter);

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

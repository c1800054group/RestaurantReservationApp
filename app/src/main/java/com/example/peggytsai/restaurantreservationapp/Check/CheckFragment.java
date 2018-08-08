package com.example.peggytsai.restaurantreservationapp.Check;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;

public class CheckFragment extends Fragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private BottomNavigationView navigation;
    private int tabCount = 0;
    private TextView tv11Check,tv12Check;
//    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
//    private int mOffscreenPageLimit = DEFAULT_OFFSCREEN_PAGES;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_check);

//        viewPager =  view.findViewById(R.id.viewPager);
//        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

        tabCount = tabLayout.getTabCount();
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
                        return checkOrderFragment;
                    case 1:
                        CheckRecordFragment checkRecordFragment  = new CheckRecordFragment();
                        return checkRecordFragment;
                    default:
                        return null;
                }
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return tabCount;
            }
        });

        viewPager.getAdapter().notifyDataSetChanged();



        return view;
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout裡的TabItem被選中的时候觸發
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //viewPager滑动之後顯示觸發
        tabLayout.getTabAt(position).select();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

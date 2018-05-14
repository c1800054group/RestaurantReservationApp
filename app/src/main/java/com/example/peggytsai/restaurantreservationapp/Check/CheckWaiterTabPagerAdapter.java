package com.example.peggytsai.restaurantreservationapp.Check;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceAlreadyManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceManagerFragment;

public class CheckWaiterTabPagerAdapter extends FragmentStatePagerAdapter{
    int numOfTabs;

    public CheckWaiterTabPagerAdapter(FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                CheckWaiterFragment checkWaiterFragment = new CheckWaiterFragment();
                return checkWaiterFragment;
            case 1:
                CheckWaiterAlreadyFragment checkWaiterAlreadyFragment = new CheckWaiterAlreadyFragment();
                return checkWaiterAlreadyFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

}

package com.example.peggytsai.restaurantreservationapp.Waiter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class WaiterTabPagerAdapter extends FragmentStatePagerAdapter{
    int numOfTabs;

    public WaiterTabPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ServiceManagerFragment serviceManagerFragment = new ServiceManagerFragment();
                return serviceManagerFragment;
            case 1:
                ServiceAlreadyManagerFragment serviceAlreadyManagerFragment = new ServiceAlreadyManagerFragment();
                return serviceAlreadyManagerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return WaiterTabPagerAdapter.POSITION_NONE;
    }

    //    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //得到暫存的fragment
//        Fragment fragment= (Fragment) super.instantiateItem(container, position);
//        String fragmentTag=fragment.getTag();
//
//
//            return super.instantiateItem(container, position);
//    }
}

package com.example.peggytsai.restaurantreservationapp.Cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Page;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.Other.QrCodeFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CartFragmentShow extends Fragment {

    private TextView tt_toolbar;
    private TextView btMenuShowMenu;
    private View view;

    private String TableNamer="0";
    private SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_show_menu, container, false);

        if(Common.FragmentSwitch == 1){

            Common.FragmentSwitch = 0;

            /* 若在Activity內需要呼叫IntentIntegrator(Activity)建構式建立IntentIntegrator物件；
        * 而在Fragment內需要呼叫IntentIntegrator.forSupportFragment(Fragment)建立物件，
        * 掃瞄完畢時，Fragment.onActivityResult()才會被呼叫 */
            // IntentIntegrator integrator = new IntentIntegrator(this);
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(CartFragmentShow.this);
            // Set to true to enable saving the barcode image and sending its path in the result Intent.
            integrator.setBarcodeImageEnabled(true);
            // Set to false to disable beep on scan.
            integrator.setBeepEnabled(false);
            // Use the specified camera ID.
            integrator.setCameraId(0);
            // By default, the orientation is locked. Set to false to not lock.
            integrator.setOrientationLocked(false);
            // Set a prompt to display on the capture screen.
            integrator.setPrompt("Scan a QR Code");
            // Initiates a scan
            integrator.initiateScan();

        }


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenuLayout);
        tabLayout.setupWithViewPager(viewPager);


        btMenuShowMenu = view.findViewById(R.id.btMenuShowMenu);
        btMenuShowMenu.setText("下一步");
        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        tt_toolbar.setText("帶外點餐");


        //Common.FragmentSwitch    //為了分別顯示 已改變 標題

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

    public void KeyDown() {

        Common.switchFragment(new OrderFragment(), getActivity(), false);
//        Common.CART.clear();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            TableNamer = intentResult.getContents();
            Log.d("aaaaaaaaaa",TableNamer);

            switch (TableNamer){

                case "外帶":
                    tt_toolbar.setText("外帶");
                    TableNamer="5";    //到時候看 tableID長怎樣 再改相對的數字
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    tt_toolbar.setText("內用"+TableNamer+"桌");
                    break;

                default:
                    tt_toolbar.setText("帶外點餐");
                    break;
            }

            pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            pref.edit().putString("桌號",TableNamer).apply();

            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
            viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));  //直接返回 嵌套的子fragment
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

        } else {
//            tvTableNamer.setText("Result Not Found");
            Fragment orderFragment = new OrderFragment();
            Common.switchFragment(orderFragment,getActivity(),false);
        }
    }









}

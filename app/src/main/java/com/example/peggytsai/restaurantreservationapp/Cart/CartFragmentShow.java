package com.example.peggytsai.restaurantreservationapp.Cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Cart.menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Cart.menu.MenuAdapter;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Socket;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CartFragmentShow extends Fragment {

    private TextView tt_toolbar;
    private TextView btMenuShowMenu;
    private View view;

    private String TableNamer="0";
    private SharedPreferences pref;

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final static String TAG = "CartFragmentShow"; //log用
    private ViewPager viewPager;

    private LocalBroadcastManager broadcastManager;
    private  IntentFilter stockFilter;

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
//                recyclerView.getAdapter().notifyDataSetChanged();
//                Socket.SocketClient.send("notifyDataSetChanged");  //發給 MenuAdapter
            }
        });


        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        flash();
        veiw_set();


        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        stockFilter = new IntentFilter("stock");    //ChatWebSocketClient
        StockReceiver stockReceiver = new StockReceiver(view);
        broadcastManager.registerReceiver(stockReceiver, stockFilter);

        return view;
    }

    private void flash() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true); //開啟刷新

                int viewPager_switch = viewPager.getCurrentItem();
                veiw_set();
                viewPager.setCurrentItem(viewPager_switch);

                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                flash();
                int viewPager_switch = viewPager.getCurrentItem();
                veiw_set();
                viewPager.setCurrentItem(viewPager_switch);

//                Common.showToast(getActivity(),message);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String memberName = String.valueOf(  pref.getInt("memberID",0)    );

        Socket.connectServer(getActivity(),memberName);
//        Common.showToast(getActivity(),memberName);
    }
    public void KeyDown() {
        Common.switchFragment(new OrderFragment(), getActivity(), false);
//        Common.CART.clear();
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
            viewPager.setAdapter(new SamplePagerAdapter());  //直接返回 嵌套的子fragment
//            viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));  //直接返回 嵌套的子fragment
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenuLayout);
            tabLayout.setupWithViewPager(viewPager);

        } else {
            Fragment orderFragment = new OrderFragment();
            Common.switchFragment(orderFragment,getActivity(),false);
        }
    }



    private void veiw_set() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
        viewPager.setAdapter(new SamplePagerAdapter());  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabMenuLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String s1 = "";
            if (position == 0) {
                s1 = "主餐";
            } else if (position == 1){
                s1 = "附餐";
            } else if (position == 2){
                s1 = "加購";
            }
            return s1;
        }

        @Override
        public Object instantiateItem(ViewGroup container,  int position) {
            final View view = getLayoutInflater().inflate(R.layout.fragment_cart,
                    container, false);
            container.addView(view);

            if (position == 0) {
                list_connect(view, position);
            } else if (position == 1){
                list_connect(view, position);
            } else if (position == 2){
                list_connect(view, position);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private void list_connect(View view, int position) {

        if (Common.networkConnected(getActivity())) {//檢查網路連線

            String url = Common.URL + "/MenuServlet";
            try {

                Common.MENU_list = new MenuGetAllTask().execute(url).get();

                if (position == 0) {
                    menus_list = Common.MENU_list.get(0);

                } else if (position == 1) {
                    menus_list = Common.MENU_list.get(1);

                } else if (position == 2) {
                    menus_list = Common.MENU_list.get(2);
                }

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
                recyclerView.setAdapter(new MenuAdapter(menus_list, getContext()));
            }
        } else {
            Common.showToast(getActivity(), "text_NoNetwork");

        }

    }



}

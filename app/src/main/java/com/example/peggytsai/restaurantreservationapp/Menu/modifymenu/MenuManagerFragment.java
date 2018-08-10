package com.example.peggytsai.restaurantreservationapp.Menu.modifymenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Cart.menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.Menu.demomenu.MenuFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;

import java.util.List;


public class MenuManagerFragment extends Fragment {

    private TextView tt_toolbar,btMenuNew;
    private View view;

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final static String TAG = "MenuManagerFragment"; //log用

    private ViewPager viewPager;
    private LocalBroadcastManager broadcastManager;
    private  IntentFilter stockFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_modify, container, false);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btMenuNew = view.findViewById(R.id.btMenuNew);

        tt_toolbar.setText(R.string.text_MenuManager);
        btMenuNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.switchFragment(new MenuModifyFragmentInsert(), getActivity(), true);
            }
        });

        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
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


        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        stockFilter = new IntentFilter("stock");    //ChatWebSocketClient
        StockReceiver stockReceiver = new StockReceiver(view);
        broadcastManager.registerReceiver(stockReceiver, stockFilter);


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

                int viewPager_switch = viewPager.getCurrentItem();
                veiw_set();
                viewPager.setCurrentItem(viewPager_switch);

            }
        }
    }

    private void veiw_set() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(  new SamplePagerAdapter() );  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {

//            sw_position=position;
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
                recyclerView.setAdapter(new ShowAdapter(menus_list, getContext()));
            }
        } else {
            Common.showToast(getActivity(), "text_NoNetwork");

        }

    }

    public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder> {
        private List<Menu> item_list;
        private Context context;
        private int imageSize;

        public ShowAdapter(List<Menu> item_list, Context context) {   //要加入 項目20~30的變數  (項目數量)
            this.item_list = item_list;
            this.context = context;

            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4; //取的螢幕寬度 抓1/4大小

        }

        @Override
        public int getItemCount() {
            return item_list.size();
        }

        @Override
        public ShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemview = layoutInflater.inflate(R.layout.fragment_menu_item, parent, false);  // false 為間接依存 直接不可用
            return new ShowAdapter.MyViewHolder(itemview);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView image_menu_view;

            private TextView text_menu_name, menu_money;


            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                image_menu_view = itemview.findViewById(R.id.image_menu_view);
                text_menu_name = itemview.findViewById(R.id.text_menu_name);
                menu_money = itemview.findViewById(R.id.menu_money);

            }
        }

        @Override
        public void onBindViewHolder(final ShowAdapter.MyViewHolder holder, final int position) {
            final Menu menu = item_list.get(position);

            String url = Common.URL + "/MenuServlet";
            int id = menu.getId();
            new MenuGetImageTask(holder.image_menu_view).execute(url, id, imageSize);

            holder.text_menu_name.setText(menu.getName());
            holder.menu_money.setText("$" + String.valueOf(menu.getPrice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();    //set bundle  使用方式是把所有的東西都包成物件 (物件中 有list中有物件)
                    bundle.putSerializable("MENU", menu);

                    Fragment update = new MenuModifyFragmentUpdate();
                    update.setArguments(bundle);

                    Common.switchFragment(update, getActivity(), true);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (Common.networkConnected(getActivity())) {//檢查網路連線

                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("action", "menuDelete");
                        jsonObject.addProperty("menu_id", menu.getId());



                        menus_list.remove(menu);//  本地 暫時存入的LIST

                        recyclerView.getAdapter().notifyDataSetChanged();

                    }
                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }
    }
}

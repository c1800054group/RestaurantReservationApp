package com.example.peggytsai.restaurantreservationapp.Manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;


import java.util.List;

public class FoodManagerFragment extends Fragment {
    private final static String TAG = "Food";
    private BottomNavigationView navigationView;
    private TextView btMenuShowMenu;
    private RecyclerView recyclerView;

    private List<Menu> menus_list;
    private MyTask MenuUpdataTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_show_menu, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_FoodManager);

        navigationView = getActivity().findViewById(R.id.Navigation);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigate_menu_manager);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_all);
//        viewPager.setAdapter(  new MyPagerAdapter(getChildFragmentManager())  );  //直接返回 嵌套的子fragment
        viewPager.setAdapter(new SamplePagerAdapter());  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabFoodLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
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
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.fragment_cart,
                    container, false);
            container.addView(view);

            if (position == 0) {

                list_connect(view, position);
            } else if (position == 1){
                list_connect(view, position);
            } else if (position == 2){
                list_connect(view, position);
            }

            recyclerView = view.findViewById(R.id.recyceleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ShowAdapter(menus_list, getContext()));

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
            View itemview = layoutInflater.inflate(R.layout.fragment_menu_quantity_item, parent, false);  // false 為間接依存 直接不可用
            return new ShowAdapter.MyViewHolder(itemview);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView image_quantity_view;

            private TextView text_quantity_name, quantity;

            private LinearLayout lv_set_quantity;

            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                image_quantity_view = itemview.findViewById(R.id.image_quantity_view);

                text_quantity_name = itemview.findViewById(R.id.text_quantity_name);
                quantity = itemview.findViewById(R.id.quantity);
                lv_set_quantity = itemview.findViewById(R.id.lv_set_quantity);

            }
        }

        @Override
        public void onBindViewHolder(final ShowAdapter.MyViewHolder holder, final int position) {
            final Menu menu = item_list.get(position);

            String url = Common.URL + "/MenuServlet";
            int id = menu.getId();

            new MenuGetImageTask(holder.image_quantity_view).execute(url, id, imageSize);
            holder.text_quantity_name.setText(menu.getName());
            holder.quantity.setText(String.valueOf(menu.getStock()));

            holder.lv_set_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final String[] setQuantit = {"1","2","3","4","5","6","7","8","9","10","11","12",
                            "13","14","15","16","17","18","19","20"};

                    AlertDialog.Builder dialog_list = new AlertDialog.Builder(getActivity());
                    dialog_list.setTitle("請選擇補貨數量");
                    dialog_list.setItems(setQuantit, new DialogInterface.OnClickListener(){
                        @Override

                        //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                        public void onClick(DialogInterface dialog, int which) {
                            int quantity_add = Integer.parseInt(setQuantit[which]);
                            int quantity_total = quantity_add + menu.getStock();

                            if (!String.valueOf(quantity_add).isEmpty()) {

                                if (Common.networkConnected(getActivity())) {//檢查網路連線

                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("action", "menuUpdata_stock");
                                    jsonObject.addProperty("menu_stock", String.valueOf(quantity_total));
                                    jsonObject.addProperty("menu_id", String.valueOf(menu.getId()));

                                    MenuUpdataTask = new MyTask(Common.URL + "/MenuServlet", jsonObject.toString());
                                    MenuUpdataTask.execute();

                                    Common.showToast(getActivity(), menu.getName() + " 庫儲貨更新為: " + String.valueOf(quantity_total));
                                    holder.quantity.setText(String.valueOf(quantity_total));
                                } else {
                                    Common.showToast(getContext(), "text_NoNetwork");
                                }

                            }

                        }
                    });
                    dialog_list.show();

                }
            });






        }

        private void showQuantity() {


        }

    }//ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder>{

}

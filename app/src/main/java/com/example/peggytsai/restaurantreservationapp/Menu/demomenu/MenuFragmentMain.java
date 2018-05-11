package com.example.peggytsai.restaurantreservationapp.Menu.demomenu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.List;


public class MenuFragmentMain extends Fragment {

    private RecyclerView recyclerView;
    private List<Menu> menus_list;


    private final static String TAG = "Mainfragment"; //log用

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        getActivity().setTitle("菜單1");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        if(Common.MENU_list.size()!=0){
            menus_list = Common.MENU_list.get(0);
            recyclerView = view.findViewById(R.id.recyceleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(  new ShowAdapter(menus_list, getContext()));
        }else {
            list_connect(view);
        }

        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true); //開啟刷新
                recyclerView.setAdapter(  new ShowAdapter(menus_list, getContext()));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        findbutton1(view);

        return view;

    }

    private void list_connect(View view) {

        if (Common.networkConnected(getActivity())) {//檢查網路連線

            String url = Common.URL + "/MenuServlet";
            try {

                Common.MENU_list = new MenuGetAllTask().execute(url).get();
                menus_list = Common.MENU_list.get(0);

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
                recyclerView.setAdapter( new ShowAdapter(menus_list, getContext()));
            }
        } else {
            Common.showToast(getActivity(), "text_NoNetwork");
//            item_list2 = getitem();
        }
        //都做

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


//        if (MenuGetImageTask != null) {
//            MenuGetImageTask.cancel(true);
//            MenuGetImageTask = null;
//        }

    }

    private void findbutton1(View view) {
//        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
//        tt_action = view.findViewById(R.id.tvMemberLogOut);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemview = layoutInflater.inflate(R.layout.fragment_menu_item, parent, false);  // false 為間接依存 直接不可用
            return new MyViewHolder(itemview);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            private TextView tt_name;
            private TextView tt_money;


            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                imageView = itemview.findViewById(R.id.image_view);
//            img_m = itemview.findViewById(R.id.img_minus);
//            img_p = itemview.findViewById(R.id.img_plus);
                tt_name = itemview.findViewById(R.id.text_name);
                tt_money = itemview.findViewById(R.id.money);
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Menu menu = item_list.get(position);

            String url = Common.URL + "/MenuServlet";
            int id = menu.getId();
            new MenuGetImageTask(holder.imageView).execute(url, id, imageSize);

            holder.tt_name.setText(menu.getName());
            holder.tt_money.setText("$"+ String.valueOf(menu.getPrice()));



        }




    }//ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder>{


}

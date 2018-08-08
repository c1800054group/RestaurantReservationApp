package com.example.peggytsai.restaurantreservationapp.Menu.demomenu;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

public class MenuFragmentSub extends Fragment {

    private RecyclerView recyclerView;
    private List<Menu> menus_list;
    private Button bt_cart_main;
    private Button bt_cart_sub;
    private Button bt_cart_submit;   //git test

    private TextView tt_toolbar;
    private TextView tt_action;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("菜單2");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        if(Common.MENU_list.size()!=0){
            menus_list = Common.MENU_list.get(1);
        }else {
            menus_list = new ArrayList<>();//空值
        }

        recyclerView = view.findViewById(R.id.recyceleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(new ShowAdapter(menus_list, getContext()));   //連接器 需要  資料 跟 容器(view)

        findbutton(view);



        return view;


    }



    private void findbutton(View view) {


        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        tt_action = view.findViewById(R.id.tvMemberLogOut);


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
                imageView = itemview.findViewById(R.id.image_menu_view);
                tt_name = itemview.findViewById(R.id.text_menu_name);
                tt_money = itemview.findViewById(R.id.menu_money);
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

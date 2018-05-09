package com.example.peggytsai.restaurantreservationapp.Manager;

import android.content.Context;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetAllTask;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.Menu.modifymenu.MenuModifyFragmentUpdate;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
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



        btMenuShowMenu = view.findViewById(R.id.btMenuShowMenu);
        btMenuShowMenu.setText("返回");
        btMenuShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                navigationView = getActivity().findViewById(R.id.Navigation);
//                navigationView.getMenu().clear();
//                navigationView.inflateMenu(R.menu.navigate_menu_manager);

            }
        });


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//        viewPager.setAdapter(  new MyPagerAdapter(getChildFragmentManager())  );  //直接返回 嵌套的子fragment
        viewPager.setAdapter(  new SamplePagerAdapter() );  //直接返回 嵌套的子fragment

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    private void list_connect(View view,int position) {

        if (Common.networkConnected(getActivity())) {//檢查網路連線

            String url = Common.URL + "/MenuServlet";
            try {

                Common.MENU_list = new MenuGetAllTask().execute(url).get();

                if(position==0){
                    menus_list = Common.MENU_list.get(0);
                }else{
                    menus_list = Common.MENU_list.get(1);
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
                recyclerView.setAdapter( new ShowAdapter(menus_list, getContext()));
            }
        } else {
            Common.showToast(getActivity(), "text_NoNetwork");
//            item_list2 = getitem();
        }
        //都做

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
            String s1="";
            if(position==0){
                s1="主餐";
            }else{
                s1="附餐";
            }

            return s1 + (position + 1);

        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.fragment_cart,
                    container, false);
            container.addView(view);
//            TextView title = (TextView) view.findViewById(R.id.item_title);
//            title.setText(String.valueOf(position + 1));
            if(position==0){

                list_connect(view,position);
//                menus_list=getlist();
            }else{
                list_connect(view,position);
//                menus_list=getlist2();
            }


            recyclerView = view.findViewById(R.id.recyceleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter( new ShowAdapter(menus_list, getContext()));




            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

//    private List<Menu> getlist() {
//
//        List<Menu> list = new ArrayList<>();
//        list.add(new Menu("jim","200",1));
//        list.add(new Menu("jim1","100",1));
//        list.add(new Menu("jim2","300",1));
//
//
//        return list;
//    }
//    private List<Menu> getlist2() {
//
//        List<Menu> list = new ArrayList<>();
//        list.add(new Menu("hun","2000",1));
//        list.add(new Menu("hun1","1000",1));
//        list.add(new Menu("hun2","3000",1));
//
//
//        return list;
//    }



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
            private ImageView imageView;

            private TextView tt_name;
            private TextView tt_quantity;
            private EditText ee_set_quantity;


            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                imageView = itemview.findViewById(R.id.image_view);

                tt_name = itemview.findViewById(R.id.text_name);
                tt_quantity = itemview.findViewById(R.id.quantity);
                ee_set_quantity = itemview.findViewById(R.id.set_quantity);

            }
        }

        @Override
        public void onBindViewHolder(final ShowAdapter.MyViewHolder holder, final int position) {
            final Menu menu = item_list.get(position);

            String url = Common.URL + "/MenuServlet";
            int id = menu.getId();

            new MenuGetImageTask(holder.imageView).execute(url, id, imageSize);
            holder.tt_name.setText(menu.getName());
            holder.tt_quantity.setText(String.valueOf(menu.getStock()));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( ! holder.ee_set_quantity.getText().toString().trim().isEmpty()){



                        if (Common.networkConnected(getActivity())) {//檢查網路連線

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "menuUpdata_stock");
                            jsonObject.addProperty("menu_stock", holder.ee_set_quantity.getText().toString().trim());
                            jsonObject.addProperty("menu_id", String.valueOf(menu.getId()));

                            MenuUpdataTask = new MyTask(Common.URL+"/MenuServlet", jsonObject.toString());
                            MenuUpdataTask.execute();

                            Common.showToast(getActivity(),"庫儲貨更新為: "+holder.ee_set_quantity.getText().toString().trim());
                            holder.tt_quantity.setText(holder.ee_set_quantity.getText().toString().trim());
                        } else {
                            Common.showToast(getContext(), "text_NoNetwork");
                        }



                    }


                }
            });




        }




    }//ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder>{


}

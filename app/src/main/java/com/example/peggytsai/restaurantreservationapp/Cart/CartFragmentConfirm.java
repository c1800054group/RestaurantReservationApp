package com.example.peggytsai.restaurantreservationapp.Cart;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.Menu.OrderMenu;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CartFragmentConfirm extends Fragment {

    private RecyclerView recyclerView;

    private List<Menu> menus_list = new ArrayList<>();

    private TextView text_total;
    private TextView count;
    private TextView minus;
    private String money="";
    private SharedPreferences pref;

    private final static String TAG = "MainActivity"; //log用
    private MyTask upcartTask;

    private TextView tt_toolbar;
    private TextView btCartText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_confirm, container, false);

        show(view);  //顯示 統計的資料

        recyclerView = view.findViewById(R.id.rv_CartConfirm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(  new MenuAddAdapter(menus_list, getContext()));



//        pref = getActivity().getSharedPreferences("preference",getActivity().MODE_PRIVATE);
//        pref.edit()
//                .putString("Subtotal_main", money)
//                .apply();

        return view;


    }

    private void show(View view) {

        count = view.findViewById(R.id.count);//項目數
        minus = view.findViewById(R.id.minus);//折價

        count.setText(String.valueOf(Common.CART.size()));


        text_total = view.findViewById(R.id.total);        //計算並顯示
        int total=0;
        for(OrderMenu orderMenu :Common.CART ){

            total += (  orderMenu.getQuantity() *  Integer.valueOf( orderMenu.getPrice() )   );

            Menu menu = orderMenu.getMenu();
            menus_list.add(menu);
        }

        text_total.setText(money= String.valueOf(total) );

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btCartText = view.findViewById(R.id.btCartConfirm);

        tt_toolbar.setText("確認餐點");
        btCartText.setText("結帳");
        btCartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.CART.size()>0){

//                    getFragmentManager().beginTransaction().replace(R.id.main_activty, new CartFragmentConfirmText()).commit();
                    Common.switchFragment(new CartFragmentConfirmText(), getActivity(), true);

                    if (Common.networkConnected(getActivity()) && Common.CART.size()>0) {//檢查網路連線

                        pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        int memberID = pref.getInt("memberID",0);  //  會員id
                        String person = pref.getString("人數","");
                        String data = pref.getString("日期時間","");

                        if(person=="" || data=="" || person == null || data == null){
                            //非預定 是內用or外帶
                        }

                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("action", "orderInsert");
                        jsonObject.addProperty("cart", new Gson().toJson(  Common.CART  ));
                        jsonObject.addProperty("total_money", money);
//                    jsonObject.addProperty("note", "");

                        upcartTask = new MyTask(Common.URL+"/OrderServlet", jsonObject.toString());
                        upcartTask.execute();
                    } else {
                        Common.showToast(getContext(), "text_NoNetwork");
                    }


                }else{

                    Common.showToast(getContext(), "nothing select");

                }




            }
        });




    }



    public class MenuAddAdapter extends RecyclerView.Adapter<MenuAddAdapter.MyViewHolder> {
        private List<Menu> item_list;
        private Context context;
        private int imageSize;


        public MenuAddAdapter(List<Menu> item_list, Context context) {   //要加入 項目20~30的變數  (項目數量)
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
            View itemview = layoutInflater.inflate(R.layout.fragment_cart_item, parent, false);  // false 為間接依存 直接不可用
            //Inflater 載入一推view

            return new MyViewHolder(itemview);
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private ImageView img_m;
            private ImageView img_p;

            private TextView tt_name;
            private TextView tt_money;
            private TextView tt_count;


            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                imageView = itemview.findViewById(R.id.image_view);
                img_m = itemview.findViewById(R.id.img_minus);
                img_p = itemview.findViewById(R.id.img_plus);


                tt_name = itemview.findViewById(R.id.text_name);
                tt_money = itemview.findViewById(R.id.money);
                tt_count = itemview.findViewById(R.id.count);


            }


        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Menu menu = item_list.get(position);

            String url = Common.URL + "/MenuServlet";
            int id = menu.getId();
//            int imageSize = 250;
            new MenuGetImageTask(holder.imageView).execute(url, id, imageSize);


            holder.tt_name.setText(menu.getName());
            holder.tt_money.setText("$"+String.valueOf(menu.getPrice()));


            OrderMenu orderMenu = findorderMenu(menu);


            holder.tt_count.setText(String.valueOf(   orderMenu.getQuantity()  ));


            View.OnClickListener view_listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderMenu orderMenu = new OrderMenu(menu, 0);
                    int index = Common.CART.indexOf(orderMenu);
                    if (index != -1) {
                        orderMenu = Common.CART.get(index);   //找到就替換
                    }


                    switch (v.getId()) {
                        case R.id.img_minus:
                            //項目--

                            if(orderMenu.getQuantity()!=0){

                                orderMenu.setQuantity(orderMenu.getQuantity()-1);
                                if( orderMenu.getQuantity()==0){
                                    Common.CART.remove(orderMenu);
//                                    menus_list.remove(menu);
//                                        notifyDataSetChanged();
                                    item_list.remove(menu);
                                    notifyDataSetChanged();
                                }
                                holder.tt_count.setText(String.valueOf(   orderMenu.getQuantity()  )); //所以使用區域變數 替代顯示的內容
                            }

                            break;

                        case R.id.img_plus:
                            //項目++


                            if(orderMenu.getQuantity()<9){

                                orderMenu.setQuantity(orderMenu.getQuantity() + 1);
                                holder.tt_count.setText(String.valueOf( orderMenu.getQuantity() ));
                                if(index == -1){
                                    Common.CART.add(orderMenu);
                                }
                            }

                            break;
//                        case R.id.bt_cart_submit:
//                            break;

                    }

                    count.setText(String.valueOf(Common.CART.size()));
                    int total=0;
                    for(OrderMenu orderMenu1 :Common.CART ){

                        total += (  orderMenu1.getQuantity() *  Integer.valueOf( orderMenu1.getPrice() )   );
                    }
                    text_total.setText(money=String.valueOf(total) );

                    //加入物件
                    //cope2.OrderMenu.getQuantity()' on a null object reference
//                Toast.makeText(context, String.valueOf( orderMenu.getQuantity()  ), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, String.valueOf( Common.CART.size()  ), Toast.LENGTH_SHORT).show();


                }//onClick(View v) {
            };
            holder.img_m.setOnClickListener(view_listener);
            holder.img_p.setOnClickListener(view_listener);



        }

        private OrderMenu findorderMenu(Menu menu) {

            OrderMenu orderMenu = new OrderMenu(menu, 0);
            int index = Common.CART.indexOf(orderMenu);
            if (index != -1) {
                orderMenu = Common.CART.get(index);   //找到就替換
            }

            return orderMenu;
        }


    }//ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder>{


}

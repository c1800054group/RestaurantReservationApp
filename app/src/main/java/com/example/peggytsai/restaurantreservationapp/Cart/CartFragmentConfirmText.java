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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Cart.menu.OrderMenu;
import com.example.peggytsai.restaurantreservationapp.Menu.Socket;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CartFragmentConfirmText extends Fragment {

    private TextView tt_toolbar;
    private TextView btCartText;
    private TextView orderid;
    private TextView discount_checkout;
    private TextView total_checkout;
    private TextView date;
    private LinearLayout linearLayout;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    private List<OrderMenu> menus_list = new ArrayList<>();
    private MyTask getTimestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("訂單完成");

        View view = inflater.inflate(R.layout.fragment_cart_confirm_text, container, false);
        menus_list = Common.CART;
        pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btCartText = view.findViewById(R.id.btCartText);
        orderid = view.findViewById(R.id.orderid);
        date = view.findViewById(R.id.date);
        total_checkout = view.findViewById(R.id.total_checkout);
        discount_checkout = view.findViewById(R.id.discount_checkout);

        String orderID="";
        orderid.setText(orderID = String.valueOf(pref.getInt("orderID",0)));
        date.setText("");

        if(pref.getString("Discount","")==""){
            discount_checkout.setText("");
        }else{
            Double Discount_num1 =  Double.valueOf( pref.getString("Discount","")  );
            int Discount_num = (int) (Discount_num1 * 10);

            discount_checkout.setText(String.valueOf(  Discount_num  )+"折");
        }

        total_checkout.setText(pref.getString("money",""));


        tt_toolbar.setText("完成點餐");
        btCartText.setText("繼續訂單");
        btCartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CART.clear();
                Common.switchFragment(new OrderFragment(), getActivity(), true);
            }
        });

        recyclerView = view.findViewById(R.id.reCheckout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(  new ShowAdapter(menus_list, getContext()));

        if(orderID!=""){

            if (Common.networkConnected(getActivity()) && Common.CART.size()>0) {//檢查網路連線

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getTimestamp");
                jsonObject.addProperty("orderId", orderID);

                getTimestamp = new MyTask(Common.URL+"/CheckOrderServlet", jsonObject.toString());

                String result;

                Timestamp timestamp = null;
                String Timejson;
                try {


                    result = getTimestamp.execute().get();
                    date.setText(result);


                } catch (Exception e) {

                }
            } else {
                Common.showToast(getContext(), "text_NoNetwork");
            }

        }

        pref.edit().putString("人數","").putString("日期時間","").putString("Coupon","").putString("Discount","").putString("money","").apply(); //clear

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Socket.disconnectServer();
    }

    public void KeyDown() {
        Common.switchFragment(new OrderFragment(), getActivity(), true);
        Common.CART.clear();
    }


    public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder> {
        private List<OrderMenu> item_list;
        private Context context;
        private int imageSize;
        public ShowAdapter(List<OrderMenu> item_list, Context context) {   //要加入 項目20~30的變數  (項目數量)
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
            View itemview = layoutInflater.inflate(R.layout.fragment_menu_checkout_item, parent, false);  // false 為間接依存 直接不可用
            return new ShowAdapter.MyViewHolder(itemview);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            private TextView text_item;
            private TextView text_quantity;
            private TextView text_subtotal;


            public MyViewHolder(View itemview) {
                super(itemview);//  可接view 建構式   表示RecyclerView.ViewHolder 可能會有 view屬性
                text_item = itemview.findViewById(R.id.text_item);
                text_quantity = itemview.findViewById(R.id.text_quantity);
                text_subtotal = itemview.findViewById(R.id.text_subtotal);

            }
        }

        @Override
        public void onBindViewHolder(final ShowAdapter.MyViewHolder holder, final int position) {
            final OrderMenu menu = item_list.get(position);


            holder.text_item.setText(menu.getName());
            holder.text_quantity.setText(String.valueOf(menu.getQuantity()));
            holder.text_subtotal.setText(String.valueOf(  (menu.getQuantity() * Integer.valueOf(menu.getPrice()))    ));

        }

    }//ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder>{


}

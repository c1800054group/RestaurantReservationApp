package com.example.peggytsai.restaurantreservationapp.Cart;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Check.CheckWaiterWebSocketClient;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Coupon;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuGetImageTask;
import com.example.peggytsai.restaurantreservationapp.Menu.OrderMenu;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceWebSocketClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URISyntaxException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class CartFragmentConfirm extends Fragment {

    private RecyclerView recyclerView;

    private List<Menu> menus_list = new ArrayList<>();

    private TextView text_total;
    private TextView count;
    private TextView discount;
    private String money="";
    private SharedPreferences pref;

    private final static String TAG = "MainActivity"; //log用
    private MyTask upcartTask;
    private MyTask getCouponTask;

    private TextView tt_toolbar;
    private TextView btCartText;


    private CheckWaiterWebSocketClient checkWaiterWebSocketClient;

    private Coupon coupon ;
    private  List<String> list = new ArrayList<String>();

    private  int total=0;
    private  float discount_money = 1;

    private List<Menu> no_menu_list = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_confirm, container, false);


        pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int memberID = pref.getInt("memberID",0);  //  會員id

        if (Common.networkConnected(getActivity()) && Common.CART.size()>0) {//檢查網路連線

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "coupon");
            jsonObject.addProperty("memderId", memberID);

            getCouponTask = new MyTask(Common.URL+"/MessageServlet", jsonObject.toString());
            String couponString;
            try {
                couponString = getCouponTask.execute().get();
                coupon = new Gson().fromJson(couponString, Coupon.class);

//                Common.showToast(getActivity(),coupon.getCoupon());
                list.add(coupon.getCoupon());


                if(list.size()>0){
                    pref.edit().putString("Coupon",coupon.getCoupon()).putString("Discount",String.valueOf(coupon.getDiscount())).apply();
                }

            } catch (Exception e) {

            }
        } else {
            Common.showToast(getContext(), "text_NoNetwork");
        }

        show(view);  //顯示 統計的資料

        recyclerView = view.findViewById(R.id.rv_CartConfirm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//MainActivity.this
        recyclerView.setAdapter(  new MenuAddAdapter(menus_list, getContext()));



//        pref = getActivity().getSharedPreferences("preference",getActivity().MODE_PRIVATE);
//        pref.edit()
//                .putString("Subtotal_main", money)
//                .apply();



        pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);

        String memberName = pref.getString("memberName","");

        //連接webSocket
        URI uri = null;
        try {
            uri = new URI(Common.URL+"/CheckOrderWebSocket/" + memberName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (checkWaiterWebSocketClient == null) {
            checkWaiterWebSocketClient = new CheckWaiterWebSocketClient(uri, getContext());
            checkWaiterWebSocketClient.connect();
        }

        return view;


    }

    private void show(View view) {

        count = view.findViewById(R.id.count);//項目數
//        discount = view.findViewById(R.id.discount);//折價

        count.setText(String.valueOf(Common.CART.size()));


        text_total = view.findViewById(R.id.total);        //計算並顯示

        for(OrderMenu orderMenu :Common.CART ){

            total += (  orderMenu.getQuantity() *  Integer.valueOf( orderMenu.getPrice() )   );

            Menu menu = orderMenu.getMenu();
            menus_list.add(menu);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_item,list);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("選擇優惠眷 ：");
        builder.setPositiveButton("略過", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pref.edit().putString("Discount","").apply();
                connect();
            }
        });


        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                discount_money=coupon.getDiscount();
                connect();
//                discount.setText(String.valueOf(coupon.getDiscount()));
//                total *= coupon.getDiscount();
//
//                discount_money= coupon.getDiscount();
//
//                text_total.setText(money= String.valueOf(total) );

//                Common.showToast(getActivity(),String.valueOf(which+1) +". "+ list.get(which));


            }
        });
        final  AlertDialog alertDialog = builder.create();


        text_total.setText(money= String.valueOf(total) );

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btCartText = view.findViewById(R.id.btCartConfirm);

        tt_toolbar.setText("確認餐點");
        btCartText.setText("結帳");
        btCartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                alertDialog.show();


            }
        });




    }

    private void connect() {

        if(Common.CART.size()>0){

//                    getFragmentManager().beginTransaction().replace(R.id.main_activty, new CartFragmentConfirmText()).commit();
//            Common.switchFragment(new CartFragmentConfirmText(), getActivity(), true);

            if (Common.networkConnected(getActivity()) && Common.CART.size()>0) {//檢查網路連線

                pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                int memberID = pref.getInt("memberID",0);  //  會員id

                String table_member = "";
                String person = "";
                String data = "";
                table_member =pref.getString("桌號","");

                person = pref.getString("人數","");
                data = pref.getString("日期時間","");

//                if(table_member == "" && person=="" && data == ""){
//                    Common.showToast(getActivity(),"資料有錯誤  全為空值 有桌號 或 人物與時間 沒抓到資料");
//                    return;
//                }

//
//                        if(person=="" || data=="" || person == null || data == null){
//                            //非預定 是內用or外帶
//                        }
                money = String.valueOf(          (int)(Integer.valueOf(money)* discount_money     )        );
                pref.edit().putString("money",money).apply();

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("action", "orderInsert");
                jsonObject.addProperty("cart", new Gson().toJson(  Common.CART  ));
                jsonObject.addProperty("total_money", money);

                jsonObject.addProperty("memberID", String.valueOf(memberID) );

                if(table_member == ""){
                    jsonObject.addProperty("person", person);
                    jsonObject.addProperty("data", data);
                }else{
                    jsonObject.addProperty("table_member", table_member);
                }


                String oderID_re = "";

                upcartTask = new MyTask(Common.URL+"/CheckOrderServlet", jsonObject.toString());
//                        upcartTask.execute();

                try {
                    oderID_re = upcartTask.execute().get();
//                            Common.showToast(getActivity(),oderID+"first");
                } catch (Exception e) {

//
//                        try {
//                            Thread.currentThread().sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                }
                try {
                    if (table_member == ""){
                        jsonObject.addProperty("table_member", "尚未入坐");
                    }
                    Thread.sleep(1000);
                    //送出
                    if (checkWaiterWebSocketClient != null) {
                        Log.d("aaaaaaaaaa", jsonObject.toString());
                        checkWaiterWebSocketClient.send(jsonObject.toString());
                    }
                    //斷線
                    if (checkWaiterWebSocketClient != null) {
                        checkWaiterWebSocketClient.close();
                        checkWaiterWebSocketClient = null;
                    }
                }catch (Exception e){
                    Log.d("web123",e.toString());
                }

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss").create();
                    JsonObject jsonObject_return = gson.fromJson(oderID_re.toString(),JsonObject.class);

                    if(jsonObject_return.get("orderId")!=null) {
                        oderID_re = jsonObject_return.get("orderId").getAsString();
                        Log.d("ss1",oderID_re);
//                        date.setText( s1 );
                    }else if(jsonObject_return.get("list_no_stock")!=null) {

                        try {
                            Type listType = new TypeToken<List<Menu>>() {
                            }.getType();

                            no_menu_list  = new Gson().fromJson(jsonObject_return.get("list_no_stock").getAsString(),listType);

                            Log.d("ss1",oderID_re);
                            oderID_re="";
//                            Common.showToast(getActivity(),"訂單沒有建立成功");

                                String textout="";
                                int j=0;
                                int menu_id=0;
                                for( Menu menu : no_menu_list){
                                    j++;  //display
                                    menu_id =  Common.CART.indexOf(menu);
//                                    Common.CART.get(menu_id).setQuantity(0);

                                    if(j==1){
                                        textout += String.valueOf(Common.CART.get(menu_id).getName()) + " 缺貨喔";
                                    }else{
                                        textout += "\n"+String.valueOf(Common.CART.get(menu_id).getName()) + " 缺貨喔";
                                    }
                                    Common.CART.remove(menu_id);

                                }
                                Common.showToast(getActivity(),String.valueOf( textout ));


                        }catch (Exception e){
                            Log.d("ss1",e.toString());

                        }

                        Common.switchFragment(new CartFragmentShow(), getActivity(), true);
                        return;





                    }else{
                        Log.d("ss1",oderID_re);
                        oderID_re="";
                        Common.showToast(getActivity(),"沒有取得 json文字");
                        return;
                    }

                pref.edit().putInt("orderID",Integer.valueOf(oderID_re) ).putString("money",money).apply();


                Common.switchFragment(new CartFragmentConfirmText(), getActivity(), true);

            } else {
                Common.showToast(getContext(), "text_NoNetwork");
            }


        }else{

            Common.showToast(getContext(), "nothing select");

        }

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
//                    text_total.setText(money=String.valueOf(  (int)( total * discount_money) )   );
                    text_total.setText(money=String.valueOf(total));

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

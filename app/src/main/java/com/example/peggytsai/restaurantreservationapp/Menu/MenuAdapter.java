package com.example.peggytsai.restaurantreservationapp.Menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private List<Menu> item_list;
    private Context context;
    private int imageSize;
    private int colorset =0;
//    private LocalBroadcastManager broadcastManager;
//    private  IntentFilter stockFilter;

    public MenuAdapter(List<Menu> item_list, Context context) {   //要加入 項目20~30的變數  (項目數量)
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
        holder.tt_money.setText("$"+ String.valueOf(menu.getPrice()));


        OrderMenu orderMenu = findorderMenu(menu);


        holder.tt_count.setText(String.valueOf(   orderMenu.getQuantity()  ));

//

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
                            }
                            holder.tt_count.setText(String.valueOf(   orderMenu.getQuantity()  )); //所以使用區域變數 替代顯示的內容


                        }
                        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));

                        break;

                    case R.id.img_plus:
                        //項目++
                        if(menu.getStock() > orderMenu.getQuantity()){
                            if(orderMenu.getQuantity()<9){

                                orderMenu.setQuantity(orderMenu.getQuantity() + 1);
                                holder.tt_count.setText(String.valueOf( orderMenu.getQuantity() ));
                                if(index == -1){
                                    Common.CART.add(orderMenu);
                                }


                            }
                        }else {
                            if(menu.getStock() <= orderMenu.getQuantity()){
                                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorWarning));
                                Common.showToast(context,"選擇已經到達庫存上線了呦");
                            }
                        }
                        break;

                }

                //加入物件
                //cope2.OrderMenu.getQuantity()' on a null object reference
//                Toast.makeText(context, String.valueOf( orderMenu.getQuantity()  ), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, String.valueOf( Common.CART.size()  ), Toast.LENGTH_SHORT).show();


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




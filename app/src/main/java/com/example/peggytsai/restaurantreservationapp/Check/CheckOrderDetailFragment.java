package com.example.peggytsai.restaurantreservationapp.Check;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckOrderDetailFragment extends Fragment {
    private static  String TAG = "CheckOrderDetailFragment";
    private MyTask masterFindByIdDetailTask;
    private RecyclerView orderDetailRecyclerView;
    private TextView tvOrderNumber, tvOrderDate;
    private View view;
    private String orderDate = "";
    private int orderId = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_check1_order_pager_detail, container, false);
      findView();
        Bundle bundle = getArguments();
        orderId = bundle.getInt("orderId3");
        orderDate = bundle.getString("tvOrderDate");
        tvOrderDate.setText(orderDate);
        tvOrderNumber.setText(String.valueOf(orderId));
      orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//      int order_id =
              showOrderMasterDetail();
//      if(order_id > 0){
//          tvOrderNumber.setText(String.valueOf(order_id));
//      }


        return view;
    }
    private void findView() {
        orderDetailRecyclerView = view.findViewById(R.id.orderDetailRecyclerView);
        tvOrderNumber = view.findViewById(R.id.tvOrderNumber);
        tvOrderDate = view.findViewById(R.id.tvOrderDate);

    }

    private void showOrderMasterDetail() {

        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/CheckOrderServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "checkOrderGet");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Common.PREF_FILE,Context.MODE_PRIVATE);
            int memberId = sharedPreferences.getInt("memberID",0);
            int orderId = sharedPreferences.getInt("orderID",0);
            jsonObject.addProperty("memberId",memberId);
            jsonObject.addProperty("orderId",orderId);
            String jsonOut = jsonObject.toString();
            ArrayList<CheckOrdered> checkOrders = new ArrayList<CheckOrdered>();
            masterFindByIdDetailTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = masterFindByIdDetailTask.execute().get();
                Log.d(TAG, jsonIn);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
                Type listType = new TypeToken<List<CheckOrdered>>(){ }.getType();
                checkOrders = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());

            }
            if (checkOrders == null) {
                Common.showToast(getActivity(), "Not found");
//                return 0;
            } else {
               orderDetailRecyclerView.setAdapter(new CheckOrderDetailAdapter(getActivity(), checkOrders));
//               int order_id;
//               order_id = checkOrders.get(0).getOrderId();

//               return order_id;
            }
        } else {
            Common.showToast(getActivity(), "no network connection available");
//            return 0;
        }

    }

    private class CheckOrderDetailAdapter extends RecyclerView.Adapter<CheckOrderDetailAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<CheckOrdered> checkOrders ;


        public CheckOrderDetailAdapter(Context context, List<CheckOrdered> checkOrders) {
            layoutInflater = LayoutInflater.from(context);
            this.checkOrders = checkOrders;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView tvCheck1Type, tvCheck1Number, tvCheck1Price;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvCheck1Type = itemView.findViewById(R.id.tvCheck1Type);
                tvCheck1Number = itemView.findViewById(R.id.tvCheck1Number);
                tvCheck1Price = itemView.findViewById(R.id.tvCheck1Price);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.check1_order_pager_detail_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            CheckOrdered checkOrder = checkOrders.get(position);
            String check1Type = checkOrder.getName();
            String check1Number = String.valueOf(checkOrder.getCount());
            String check1Price = String.valueOf(checkOrder.getPrice());
            myViewHolder.tvCheck1Type.setText(check1Type);
            myViewHolder.tvCheck1Number.setText(check1Number);
            myViewHolder.tvCheck1Price.setText(check1Price);



        }

        @Override
        public int getItemCount() {
            return checkOrders.size();
        }



    }

    @Override
    public void onStop() {
        super.onStop();
        if (masterFindByIdDetailTask != null) {
            masterFindByIdDetailTask.cancel(true);
        }

    }
}

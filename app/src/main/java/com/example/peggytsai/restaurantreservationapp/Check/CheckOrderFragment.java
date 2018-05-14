package com.example.peggytsai.restaurantreservationapp.Check;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


public class CheckOrderFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "OrderMasterFragment";
    private RecyclerView orderRecyclerView;
    private List<OrderMaster> orderMasters = null;
    private MyTask masterFindByIdTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv11Check,tv12Check;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_1, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_check);

        tv12Check = view.findViewById(R.id.tv12Check);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showOrderMaster();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showOrderMaster();

        tv12Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRecordFragment checkRecordFragment = new CheckRecordFragment();
               Common.switchFragment(checkRecordFragment, getActivity(), false);
            }
        });




        return view;
    }

    private void showOrderMaster() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/CheckOrderServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Common.PREF_FILE,Context.MODE_PRIVATE);
            int memberId = sharedPreferences.getInt("memberID",0);
            int orderId = sharedPreferences.getInt("orderID",0);
            jsonObject.addProperty("memberId",memberId);
            jsonObject.addProperty("orderId",orderId);
            String jsonOut = jsonObject.toString();
            masterFindByIdTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = masterFindByIdTask.execute().get();
                Log.d(TAG, jsonIn);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
                Type listType = new TypeToken<List<OrderMaster>>(){ }.getType();
                orderMasters = gson.fromJson(jsonIn, listType);
//                orderMasters.add(orderMaster1);
            } catch (Exception e) {
                Log.e(TAG, e.toString());

            }
            if (orderMasters == null || orderMasters.isEmpty()){
                Common.showToast(getActivity(), "Not found");
            }else {
                orderRecyclerView.setAdapter(new OrderMasterViewAdapter(getActivity(), orderMasters));
            }
        }else{
            Common.showToast(getActivity(), "no network connection available");
        }
    }


    private class OrderMasterViewAdapter extends RecyclerView.Adapter<OrderMasterViewAdapter.MyViewHolder>  {
        private LayoutInflater layoutInflater;
        private List<OrderMaster> orderMasters;


        public OrderMasterViewAdapter(Context context, List<OrderMaster> orderMasters) {
            layoutInflater = LayoutInflater.from(context);
            this.orderMasters = orderMasters;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tvOrderDate,tvCheckout,tvOrderPerson;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
//                tvCheckout = itemView.findViewById(R.id.tvCheckout);
                tvOrderPerson = itemView.findViewById(R.id.tvOrderPerson);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.order_pager_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            OrderMaster orderMaster =  orderMasters.get(position);
            final int orderId3 = orderMaster.getOrderId();
            final String orderDate = String.valueOf(orderMaster.getDate_orde());
//            String orderId = String.valueOf(orderMaster.getOrderId());
            String orderPerson = String.valueOf(orderMaster.getPerson());
            myViewHolder.tvOrderDate.setText("定位: " + orderDate);
            myViewHolder.tvOrderPerson.setText("內用人數 : " + orderPerson + "人");
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tvOrderDate", orderDate);
                    bundle.putInt("orderId3",orderId3);

                    Log.d("hhhhhh", orderDate);
                    Fragment checkOrderDetailFragment = new CheckOrderDetailFragment();
                    Common.switchFragmentBundle(checkOrderDetailFragment, getActivity(), true, bundle);

                }
            });
//            myViewHolder.tvOrderDate.setText(orderMaster.getMember_id());

        }

        @Override
        public int getItemCount() {
            return orderMasters.size();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (masterFindByIdTask != null) {
            masterFindByIdTask.cancel(true);
        }


    }
}


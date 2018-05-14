package com.example.peggytsai.restaurantreservationapp.Check;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by linyiyan on 2018/5/7.
 */

public class CheckRecordFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "OrderDetailFragment";
    private RecyclerView recordRecyclerView;
    private MyTask detailFindByIdTask;
    //    private List<OrderDetail> orderDetails = null;
    private SwipeRefreshLayout swipeRecordRefreshLayout;
    private ArrayList<OrderDetail> orderList = new ArrayList<OrderDetail>();
    private TextView tv21Check, tv22Check;
    private List<OrderDetail> orderDetailList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_2, container, false);
        tv21Check = view.findViewById(R.id.tv21Check);

        swipeRecordRefreshLayout = view.findViewById(R.id.swipeRecordRefreshLayoutt);
        swipeRecordRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRecordRefreshLayout.setRefreshing(true);
                orderList = showOrderDetail();
                swipeRecordRefreshLayout.setRefreshing(false);
            }
        });
        orderList = showOrderDetail();


        recordRecyclerView = view.findViewById(R.id.recordRecyclerView);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordRecyclerView.setAdapter(new CheckRecordFragment.OrderDetailViewAdapter(getActivity(), orderList));
        tv21Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
                Common.switchFragment(checkOrderFragment, getActivity(), false);
            }
        });

        return view;

    }

    public ArrayList<OrderDetail> showOrderDetail() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/CheckOrderServlet";
//            OrderMaster orderMaster1 = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findDetailById");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Common.PREF_FILE,Context.MODE_PRIVATE);
            int memberId = sharedPreferences.getInt("memberID",0);
            int orderId = sharedPreferences.getInt("orderID",0);
            jsonObject.addProperty("memberId",memberId);
            jsonObject.addProperty("orderId",orderId);
            String jsonOut = jsonObject.toString();
            ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();
            detailFindByIdTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = detailFindByIdTask.execute().get();
                Log.d(TAG, jsonIn);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
                Type listType = new TypeToken<List<OrderDetail>>() {
                }.getType();
                list = gson.fromJson(jsonIn, listType);
//                orderMasters.add(orderMaster1);
            } catch (Exception e) {
                Log.e(TAG, e.toString());

            }
            if (list == null) {
                Common.showToast(getActivity(), "Not found");
                return null;
            } else {
                return list;
            }
        } else {
            Common.showToast(getActivity(), "no network connection available");
            return null;
        }

    }


    private class OrderDetailViewAdapter extends RecyclerView.Adapter<OrderDetailViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<OrderDetail> orderDetailst;


        public OrderDetailViewAdapter(Context context, List<OrderDetail> orderDetails) {
            layoutInflater = LayoutInflater.from(context);
            this.orderDetailst = orderDetails;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvRecordDate, tvRecordPerson;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
                tvRecordPerson = itemView.findViewById(R.id.tvRecordPerson);

            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.record_pager_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myviewHolder, int position) {
            OrderDetail orderDetail = orderList.get(position);
            OrderDetail orderDetail1 = orderDetailst.get(position);
            String detailDate1 = String.valueOf(orderDetail1.getDate_order());
            String detailPerson1 = String.valueOf(orderDetail1.getPerson());
            final String detailDate = String.valueOf(orderDetail.getDate_order());
            String detailPerson = String.valueOf(orderDetail.getPerson());
            final int orderId = orderDetail.getOrderId();
            myviewHolder.tvRecordDate.setText("時間 : " + detailDate1);
            myviewHolder.tvRecordPerson.setText("用餐人數 : " + String.valueOf(detailPerson1));
            myviewHolder.tvRecordDate.setText("時間 : " + detailDate);
            myviewHolder.tvRecordPerson.setText("用餐人數 : " + String.valueOf(detailPerson));
            myviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId",orderId);
                    bundle.putString("date_order1", detailDate);
                    Log.d("bbbbbbbbbbb",String.valueOf(orderId));
                    Log.d("zzzzzzzz", detailDate);
                    Fragment checkRecordDetailFragment = new CheckRecordDetailFragment();
                    Common.switchFragmentBundle(checkRecordDetailFragment, getActivity(), true,bundle);

                }
            });



        }

        @Override
        public int getItemCount() {
            return orderDetailst.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (detailFindByIdTask != null) {
            detailFindByIdTask.cancel(true);
        }

    }

}

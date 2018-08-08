package com.example.peggytsai.restaurantreservationapp.Check;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

public class CheckWaiterAlreadyFragment extends Fragment{
    private static RecyclerView rvCheckOrderWaiterAlready;
    private static List<CheckOrder> listCheckOrder = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_waiter_already,container,false);

//        listCheckOrder = new ArrayList<>();
        rvCheckOrderWaiterAlready = view.findViewById(R.id.rvCheckOrderWaiterAlready);
        rvCheckOrderWaiterAlready.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCheckOrderWaiterAlready.setAdapter(new CheckWaiterAlreadyAdapter(getActivity()));

        return view;
    }
    public static void reCheckOrderAlready(CheckOrder checkOrder){
        listCheckOrder.add(checkOrder);
        if (listCheckOrder != null) {
            rvCheckOrderWaiterAlready.getAdapter().notifyDataSetChanged();
        }

    }

    private class CheckWaiterAlreadyAdapter extends RecyclerView.Adapter <CheckWaiterAlreadyAdapter.CheckWaiterViewHolder>{
        Context context;

        CheckWaiterAlreadyAdapter(Context context) {
            this.context = context;
        }

        class CheckWaiterViewHolder extends RecyclerView.ViewHolder {
            TextView tvCheckWaiterOrderName,tvCheckWaiterTableName,tvCheckWaiterOrderCount,tvCheckOrderWaiterConfirm;

            CheckWaiterViewHolder(View itemView) {
                super(itemView);
                tvCheckWaiterOrderName = itemView.findViewById(R.id.tvCheckWaiterOrderName);
                tvCheckWaiterTableName = itemView.findViewById(R.id.tvCheckWaiterTableName);
                tvCheckWaiterOrderCount = itemView.findViewById(R.id.tvCheckWaiterOrderCount);
                tvCheckOrderWaiterConfirm = itemView.findViewById(R.id.tvCheckOrderWaiterConfirm);
            }
        }
        @Override
        public int getItemCount() {
            return listCheckOrder.size();
        }

        @Override
        public CheckWaiterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.item_check_order_waiter, parent, false);
            return new CheckWaiterViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CheckWaiterViewHolder holder, int position) {
            CheckOrder order = listCheckOrder.get(position);
            holder.tvCheckWaiterOrderName.setText(order.getOrderName());
            if (order.getTableName().equals("尚未入坐")){
                holder.tvCheckWaiterTableName.setText(order.getTableName());
            }else {
                holder.tvCheckWaiterTableName.setText("第"+order.getTableName()+"桌");
            }
            holder.tvCheckWaiterOrderCount.setText(order.getCount());
            holder.tvCheckOrderWaiterConfirm.setText("已出餐");

        }

    }
}

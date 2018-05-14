package com.example.peggytsai.restaurantreservationapp.Check;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Order.Order;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckRecordDetailFragment extends Fragment {
    private static  String TAG = "recordOrderDetailFragment";
    private MyTask recordDetailDetailTask;
    private RecyclerView recordDetailRecyclerView;
    private TextView tvOrderNumber2, tvOrderDate2;
    private View view;
    private int orderId = 0;
    private String date_order = " ";


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check2_record_pager_detail, container, false);

        Bundle bundle = getArguments();
        orderId = bundle.getInt("orderId");
        date_order = bundle.getString("date_order1");
        Log.d("pppppp", date_order );
        Log.d("aaaaaaaaaaaaaaa",String.valueOf(orderId));
        recordDetailRecyclerView = view.findViewById(R.id.recordDetailRecyclerView1);
        tvOrderNumber2 = view.findViewById(R.id.tvOrderNumber2);
        tvOrderDate2 = view.findViewById(R.id.tvOrderDate2);
        recordDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String order1Id = String.valueOf(orderId);
        tvOrderNumber2.setText(order1Id);
        tvOrderDate2.setText(date_order);
        showRecordDetail();
        return view;
    }



    private void showRecordDetail() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/CheckOrderServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "checkAllOrder");
            jsonObject.addProperty("orderId",orderId);
            String jsonOut = jsonObject.toString();
            ArrayList<CheckAllOrder> checkAllOrders = new ArrayList<CheckAllOrder>();
            recordDetailDetailTask = new MyTask(url, jsonOut);
            try{
                String jsonIn = recordDetailDetailTask.execute().get();
                Log.d(TAG, jsonIn);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
                Type listType = new TypeToken<List<CheckAllOrder>>(){ }.getType();
                checkAllOrders = gson.fromJson(jsonIn, listType);
            }catch (Exception e) {
                Log.e(TAG, e.toString());

            }
            if (checkAllOrders == null) {
                Common.showToast(getActivity(), "Not found");

            } else {
                recordDetailRecyclerView.setAdapter(new RecordOrderDetailAdapter(getActivity(), checkAllOrders));
            }
        } else {
            Common.showToast(getActivity(), "no network connection available");

        }
    }


    private class RecordOrderDetailAdapter extends RecyclerView.Adapter<RecordOrderDetailAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<CheckAllOrder> checkAllOrders;


        public RecordOrderDetailAdapter(Context  context, ArrayList<CheckAllOrder> checkAllOrders) {
            layoutInflater = LayoutInflater.from(context);
            this.checkAllOrders = checkAllOrders;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView tvCheck2Type,tvCheck2Number,tvCheck2Price;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvCheck2Type = itemView.findViewById(R.id.tvCheck2Type);
                tvCheck2Number = itemView.findViewById(R.id.tvCheck2Number);
                tvCheck2Price = itemView.findViewById(R.id.tvCheck2Price);

            }
        }

        @NonNull
        @Override
        public RecordOrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.check2_record_pager_detail_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordOrderDetailAdapter.MyViewHolder myViewHolder, int position) {
            CheckAllOrder checkAllOrder = checkAllOrders.get(position);
            String  orderId = String.valueOf(checkAllOrder.getOrderId());
            String check2Type = checkAllOrder.getName();
            String check2Number = String.valueOf(checkAllOrder.getCount());
            String check2Price = String.valueOf(checkAllOrder.getPrice());
            myViewHolder.tvCheck2Type.setText(check2Type);
            myViewHolder.tvCheck2Number.setText(check2Number);
            myViewHolder.tvCheck2Price.setText(check2Price);



        }

        @Override
        public int getItemCount() {
            return checkAllOrders.size();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (recordDetailDetailTask != null) {
            recordDetailDetailTask.cancel(true);
        }

    }
}
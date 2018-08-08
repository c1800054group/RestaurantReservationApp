package com.example.peggytsai.restaurantreservationapp.Check;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.OrderMenu;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceWebSocketClient;
import com.example.peggytsai.restaurantreservationapp.Waiter.StateMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CheckWaiterFragment extends Fragment {

    private BottomNavigationView navigationView;
    private List<CheckOrder> checkOrderList;
    private RecyclerView rvCheckOrderWaiter;
    private LocalBroadcastManager broadcastManager;
    private static final String TAG = "CheckWaiterFragment";
    private CheckWaiterWebSocketClient checkWaiterWebSocketClient;
    private String memberName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_waiter, container, false);


        navigationView = getActivity().findViewById(R.id.Navigation);
        if (!(navigationView.getSelectedItemId()== R.id.item_CheckWaiter)){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navigate_menu_waiter);
        }
        checkOrderList = new ArrayList<>();
        rvCheckOrderWaiter = view.findViewById(R.id.rvCheckOrderWaiter);
        rvCheckOrderWaiter.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCheckOrderWaiter.setAdapter(new CheckWaiterAdapter(getActivity()));

        if (rvCheckOrderWaiter.getAdapter() != null) {
            rvCheckOrderWaiter.setAdapter(new CheckWaiterAdapter(getActivity()));
        }

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        registerFriendStateReceiver();

        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE,Context.MODE_PRIVATE);
        memberName = preferences.getString("memberName","");

        URI uri = null;
        try {
            uri = new URI(Common.URL+"/CheckOrderWebSocket/" + memberName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (checkWaiterWebSocketClient == null) {
            checkWaiterWebSocketClient = new CheckWaiterWebSocketClient(uri, getActivity());
            checkWaiterWebSocketClient.connect();
        }



        return view;
    }

    private void registerFriendStateReceiver() {

        IntentFilter openFilter = new IntentFilter("orderStatus");
        CheckWaiterStateReceiver checkWaiterStateReceiver = new CheckWaiterStateReceiver();
        broadcastManager.registerReceiver(checkWaiterStateReceiver, openFilter);

    }

    private class CheckWaiterStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            JsonObject jsonObject = new Gson().fromJson(message,JsonObject.class);
            StateMessage stateMessage = new Gson().fromJson(message, StateMessage.class);
            String type = jsonObject.get("type").getAsString();
//            String checkOrder = jsonObject.get("jsonCheckOrderList").getAsString();
            Type listType = new TypeToken<ArrayList<CheckOrder>>(){}.getType();
            checkOrderList = new Gson().fromJson(jsonObject.get("jsonCheckOrderList").getAsJsonArray(),listType);
//            checkOrderList = new Gson().fromJson(checkOrder,listType);
            rvCheckOrderWaiter.getAdapter().notifyDataSetChanged();
//            CheckWaiterTabFragment.vpService.getAdapter().notifyDataSetChanged();

        }
    }

    private class CheckWaiterAdapter extends RecyclerView.Adapter <CheckWaiterAdapter.CheckWaiterViewHolder> {
        Context context;
        CheckWaiterAdapter(Context context) {
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
            return checkOrderList.size();
        }

        @Override
        public CheckWaiterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.item_check_order_waiter, parent, false);

            return new CheckWaiterViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(CheckWaiterViewHolder holder, int position) {
            final CheckOrder order = checkOrderList.get(position);
            holder.tvCheckWaiterOrderName.setText(order.getOrderName());
            if (order.getTableName().equals("尚未入坐")){
                holder.tvCheckWaiterTableName.setText(order.getTableName());
            }else {
                holder.tvCheckWaiterTableName.setText("第"+order.getTableName()+"桌");
            }
            holder.tvCheckWaiterOrderCount.setText(order.getCount());
            holder.tvCheckOrderWaiterConfirm.setText(order.getStatus());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckWaiterAlreadyFragment.reCheckOrderAlready(order);
                    checkOrderList.remove(order);
                    rvCheckOrderWaiter.getAdapter().notifyDataSetChanged();
                }
            });


        }



    }


}
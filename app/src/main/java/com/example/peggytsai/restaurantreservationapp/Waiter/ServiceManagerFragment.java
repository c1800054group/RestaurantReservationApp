package com.example.peggytsai.restaurantreservationapp.Waiter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServiceManagerFragment extends Fragment {

    private BottomNavigationView navigationView;
    private static final String TAG = "ServiceManagerFragment";
    private LocalBroadcastManager broadcastManager;
    private List<String> tableList;
    private RecyclerView rvService;
    private ServiceWebSocketClient serviceWebSocketClient;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_manager, container, false);


//        navigationView = getActivity().findViewById(R.id.Navigation);
//        navigationView.getMenu().clear();
//        navigationView.inflateMenu(R.menu.navigate_menu_waiter);

        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        registerFriendStateReceiver();

        tableList = new ArrayList<>();
        rvService = view.findViewById(R.id.rvService);
        rvService.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvService.setAdapter(new ServiceAdapter(getActivity()));


        //取得偏好設定的帳號
        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        email = pref.getString("email","");

        URI uri = null;
        try {
            uri = new URI(Common.URL+"/ServerBellServer/" + email);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (serviceWebSocketClient == null) {
            serviceWebSocketClient = new ServiceWebSocketClient(uri, getActivity());
            serviceWebSocketClient.connect();
        }


        return view;
    }


    // 攔截user連線或斷線的Broadcast
    private void registerFriendStateReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        ServiceBellStateReceiver serviceBellStateReceiver = new ServiceBellStateReceiver();
        broadcastManager.registerReceiver(serviceBellStateReceiver, openFilter);
    }
    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private class ServiceBellStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            StateMessage stateMessage = new Gson().fromJson(message, StateMessage.class);
            String type = stateMessage.getType();
            String tableName = stateMessage.getUser();
            switch (type) {
                // 有user連線
                case "open":

                    if (tableName.equals(email)){
                        tableList = new ArrayList<>(stateMessage.getUsers());
                        tableList.remove(email);
                    }else {
                        // 如果其他user連線且尚未加入，就加上
                        if (!tableList.contains(tableName)) {
                            tableList.add(tableName);
                        }
                    }
                    // 重刷清單
                    rvService.getAdapter().notifyDataSetChanged();
                    break;
            }
            Log.d(TAG, message);
        }
    }




    private class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
        Context context;
        ServiceAdapter(Context context) {
            this.context = context;
        }

        class ServiceViewHolder extends RecyclerView.ViewHolder {
            TextView tvServiceTableNumber,tvServiceConfirm;

            ServiceViewHolder(View itemView) {
                super(itemView);
                tvServiceTableNumber = itemView.findViewById(R.id.tvServiceTableNumber);
                tvServiceConfirm = itemView.findViewById(R.id.tvServiceConfirm);
            }
        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }

        @Override
        public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.item_service, parent, false);
            return new ServiceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ServiceViewHolder holder, int position) {
            final String tableName = tableList.get(position);
            holder.tvServiceTableNumber.setText(tableName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tableNumber = holder.tvServiceTableNumber.getText().toString();
                    tableList.remove(tableNumber);
                    rvService.getAdapter().notifyDataSetChanged();
                    serviceWebSocketClient.send(tableName);

//                    WaiterTabFragment.a = "8";
                    ServiceAlreadyManagerFragment.renew(tableName);
//                    WaiterTabFragment.vpService.getAdapter().notifyDataSetChanged();
//                    Log.d("aaa",WaiterTabFragment.a);



                }
            });

        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (serviceWebSocketClient != null) {
            serviceWebSocketClient.close();
            serviceWebSocketClient = null;
        }
    }
}

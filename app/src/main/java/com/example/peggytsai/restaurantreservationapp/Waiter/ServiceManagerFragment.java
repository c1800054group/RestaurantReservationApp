package com.example.peggytsai.restaurantreservationapp.Waiter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ServiceManagerFragment extends Fragment {

    private BottomNavigationView navigationView;
    private static final String TAG = "ServiceManagerFragment";
    private LocalBroadcastManager broadcastManager;
    private List<String> tableList;
    private RecyclerView rvService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_manager, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_ServiceWaiter);

//        navigationView = getActivity().findViewById(R.id.Navigation);
//        navigationView.getMenu().clear();
//        navigationView.inflateMenu(R.menu.navigate_menu_waiter);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        tableList = new ArrayList<>();
        rvService = view.findViewById(R.id.rvService);
        rvService.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvService.setAdapter(new ServiceAdapter(getActivity()));


        return view;
    }


    private class ServiceAdapter extends RecyclerView.Adapter {
        Context context;
        public ServiceAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }


    }
}

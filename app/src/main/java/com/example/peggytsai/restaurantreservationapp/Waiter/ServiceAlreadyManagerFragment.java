package com.example.peggytsai.restaurantreservationapp.Waiter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServiceAlreadyManagerFragment extends Fragment{
    private static RecyclerView rvService;
    private static List<String> tableList;
    private String waiterName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_already_manager,container,false);
        tableList = new ArrayList<>();
        rvService = view.findViewById(R.id.rvServiceAlready);
        rvService.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvService.setAdapter(new ServiceAlreadyAdapter(getActivity()));

        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE,MODE_PRIVATE);
        waiterName = preferences.getString("memberName","");

        return view;
    }

    public static void renew(String text){
        tableList.add(text);
        rvService.getAdapter().notifyDataSetChanged();

    }

    private class ServiceAlreadyAdapter extends RecyclerView.Adapter<ServiceAlreadyAdapter.ServiceViewHolder> {
        Context context;

        ServiceAlreadyAdapter(Context context) {
            this.context = context;
        }

        class ServiceViewHolder extends RecyclerView.ViewHolder {
            TextView tvAlreadyServiceTableNumber;

            ServiceViewHolder(View itemView) {
                super(itemView);
                tvAlreadyServiceTableNumber = itemView.findViewById(R.id.tvAlreadyServiceTableNumber);
            }
        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }

        @Override
        public ServiceAlreadyAdapter.ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.item_service_already, parent, false);
            return new ServiceAlreadyAdapter.ServiceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ServiceAlreadyAdapter.ServiceViewHolder holder, int position) {
            final String tableName = tableList.get(position);
            holder.tvAlreadyServiceTableNumber.setText(tableName);

        }
    }

}

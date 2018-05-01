package com.example.peggytsai.restaurantreservationapp.Order;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReservationDetailFragment extends Fragment {
    private ReservationInsertTask reservationTask;
    private final static String TAG = "ReservationDetailFragment";
    private TextView tvReserveResult;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_reservation_result, container, false);
        tvReserveResult.findViewById(R.id.tvReserveResult);


        List<Orders> orderList = getOrders();

        return view;

    }




    public List<Orders> getOrders() {
        List<Orders> orderList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            if (name != null) {
                if (Common.networkConnected(getActivity())) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getAll");

                    reservationTask = new ReservationInsertTask(Common.URL + "/ReserveSerVlet", jsonObject.toString());
                    try {
                        String jsoIn = reservationTask.execute().get();
                        Type listType = new TypeToken<List<Order>>() {//他是<List<String>>泛型這樣形式 要用 TypeToken 如果用gson帶有泛行的資料的話要用TypeToken
                        }.getType();
                        orderList = new Gson().fromJson(jsoIn, listType);


                    } catch (Exception e) {
                        Log.e(TAG, "error Message" + toString());
                    }

                } else {
                    Toast.makeText(getActivity(), "connection to netWork failed", Toast.LENGTH_LONG).show();
                }
            }
        }


        return orderList;
    }
}

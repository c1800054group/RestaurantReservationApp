package com.example.peggytsai.restaurantreservationapp.Order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {
    private ListView lvOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
//        getActivity().setTitle("定位/點餐");

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_order);


        lvOrder = view.findViewById(R.id.lvOrder);
        List<Order> orderList = getOrder();
        lvOrder.setAdapter(new OrderAdapter(getContext(), orderList));

        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Fragment reserveFragment = new ReservationFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Content, reserveFragment);
                fragmentTransaction.commit();




            }
        });


        return view;


    }


    class OrderAdapter extends BaseAdapter {
        Context context;
        List<Order> orderlist;


        public OrderAdapter(Context context, List<Order> orderlist) {
            this.context = context;
            this.orderlist = orderlist;
        }

        @Override
        public int getCount() {
            return orderlist.size();
        }

        @Override
        public Object getItem(int position) {
            return orderlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return orderlist.get(position).getNextImage();
        }

        @Override
        public View getView(int position, View itemview, final ViewGroup parent) {
            if (itemview == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                itemview = layoutInflater.inflate(R.layout.item_view_order, parent, false);
            }
            Order order = orderlist.get(position);
            ImageView ivTypeImage = itemview
                    .findViewById(R.id.imageOrderView);
            ivTypeImage.setImageResource(order.getTypeImage());

            TextView tvType = itemview
                    .findViewById(R.id.tvReserve);
            tvType.setText(String.valueOf(order.getType()));

//            Order order1 = Orderlist.get(position);
            ImageView ivNextImage = itemview
                    .findViewById(R.id.ivNextView);
            ivNextImage.setImageResource(order.getNextImage());


            return itemview;


        }
    }


    private List<Order> getOrder() {
        List<Order> orderlist = new ArrayList<>();
        orderlist.add(new Order(R.drawable.reservation, "定位", R.drawable.next));

        return orderlist;
    }

}


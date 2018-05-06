package com.example.peggytsai.restaurantreservationapp.Menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Member.RegisterFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;

public class MenuFragment extends Fragment {

    private Button btOrder;

    private Button btService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_menu);


        btOrder = view.findViewById(R.id.btOrder);
        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment orderFragment = new OrderFragment();
                Common.switchFragment(orderFragment,getActivity(),true);
            }
        });


        return view;
    }

}

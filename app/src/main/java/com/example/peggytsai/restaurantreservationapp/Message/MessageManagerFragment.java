package com.example.peggytsai.restaurantreservationapp.Message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class MessageManagerFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTask messagesGetAllTask;
    private ImageButton btService;
    private BottomNavigationView navigationView;
    private FloatingActionButton btAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_manager, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_MessageManager);

        btAdd = view.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageInsertFragment messageInsertFragment = new MessageInsertFragment();
                Common.switchFragment(messageInsertFragment,getActivity(),true);
            }
        });



//        recyclerView = view.findViewById(R.id.rvMessage);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new MessageAdapter(showAllMessage(),getActivity(),getFragmentManager()));
//        swipeRefreshLayout =
//                view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //設定動畫
//                swipeRefreshLayout.setRefreshing(true);
//                showAllMessage();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        return view;
    }


    private List<Message> showAllMessage() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/MessageServlet";
            List<Message> messages = null;
            JsonObject jsonObject = new JsonObject();
            //定key（跟server開發者訂好即可）
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            messagesGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = messagesGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Message>>(){ }.getType();
                messages = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (messages == null || messages.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMessagesFound);
            } else {
                return messages;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        return null;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (messagesGetAllTask != null) {
            messagesGetAllTask.cancel(true);
        }
    }


}

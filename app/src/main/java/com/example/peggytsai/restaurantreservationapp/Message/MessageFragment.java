package com.example.peggytsai.restaurantreservationapp.Message;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingNewFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceWebSocketClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTask messagesGetAllTask;
    private ImageButton btService;
    private TextView btMessageNew;
    private BottomNavigationView navigationView;
    private ServiceWebSocketClient serviceWebSocketClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int authority_id = pref.getInt("authority_id", 1);

        btService = view.findViewById(R.id.btService);
        btMessageNew = view.findViewById(R.id.btMessageNew);


        if (authority_id == 4) {
            tvtoolBarTitle.setText(R.string.text_MessageManager);

            btService.setVisibility(BottomNavigationView.GONE);
            btMessageNew.setVisibility(BottomNavigationView.VISIBLE);

            btMessageNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageInsertFragment messageInsertFragment = new MessageInsertFragment();
                    Common.switchFragment(messageInsertFragment,getActivity(),true);
                }
            });

        } else if (authority_id == 1) {
            tvtoolBarTitle.setText(R.string.text_message);

//            navigationView = getActivity().findViewById(R.id.Navigation);
//            if (!(navigationView.getSelectedItemId() == R.id.item_Message)){
//                navigationView.getMenu().clear();
//                navigationView.inflateMenu(R.menu.navigate_menu);
//            }

            btService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                    String tableNumber = pref.getString("桌號","");
                    if (!tableNumber.equals("")) {
                        URI uri = null;
                        try {
                            uri = new URI(Common.URL + "/CheckOrderWebSocket/" + tableNumber);
                        } catch (URISyntaxException e) {
                            Log.e(TAG, e.toString());
                        }
                        if (serviceWebSocketClient == null) {
                            serviceWebSocketClient = new ServiceWebSocketClient(uri, getContext());
                            serviceWebSocketClient.connect();
                            try{
                                // delay 1 second
                                Thread.sleep(1000);

                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        JsonObject json = new JsonObject();
                        json.addProperty("action","callService");
                        json.addProperty("tableNumber",tableNumber);
                        serviceWebSocketClient.send(json.toString());
                        if (serviceWebSocketClient != null) {
                            serviceWebSocketClient.close();
                            serviceWebSocketClient = null;
                        }
                        Common.showToast(getActivity(),"已幫您呼叫服務生");
                    }else {
                        Common.showToast(getActivity(),"尚未入座");
                    }
                }
            });
        }

        recyclerView = view.findViewById(R.id.rvMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MessageAdapter(showAllMessage(),getActivity(),getFragmentManager()));
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //設定動畫
                swipeRefreshLayout.setRefreshing(true);
                showAllMessage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }


    @Nullable
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
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                messages = gson.fromJson(jsonIn, listType);
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

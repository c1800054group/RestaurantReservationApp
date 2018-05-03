package com.example.peggytsai.restaurantreservationapp.Message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTask messagesGetAllTask;
    private MessageGetImageTask messageGetImageTask;
    private ImageButton btService;
    private BottomNavigationView navigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.app_name);

        btService = view.findViewById(R.id.btService);
        btService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("ddd","ddd");
                Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
            }
        });



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


//    private class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder> {
//        private LayoutInflater layoutInflater;
//        private List<Message> messages;
//        private int imageSize;
//
//        //螢幕解析度等比例縮小
//        MessageRecyclerViewAdapter(Context context, List<Message> messages) {
//            layoutInflater = LayoutInflater.from(context);
//            this.messages = messages;
//            /* 螢幕寬度除以4當作將圖的尺寸 */
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//
//            TextView tvMessageTitle;
//            TextView tvMessageContent;
//            ImageView imageView;
//
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                tvMessageTitle = itemView.findViewById(R.id.tvMessageTitle);
//                tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
//                imageView = itemView.findViewById(R.id.ivMessage);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return messages.size();
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View itemView = layoutInflater.inflate(R.layout.recyclerview_message, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
//
//            //抓文字
//            final Message message = messages.get(position);
//            String url = Common.URL + "/MessageServlet";
//            int id = message.getId();
//            //開啟asynctask做事，做完事情再來顯示
//            messageGetImageTask = new MessageGetImageTask(url, id, imageSize, myViewHolder.imageView);
//            //主程序先往下執行，將文字的部分貼好，事件處理
//            messageGetImageTask.execute();
//            myViewHolder.tvMessageTitle.setText(message.getMessage_title());
//            myViewHolder.tvMessageContent.setText(message.getMessage_content());
//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Fragment messageDetailFragment = new MessageDetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("message", message);
//                    messageDetailFragment.setArguments(bundle);
//                    Common.switchFragment(messageDetailFragment.getActivity(),true);
//                }
//            });
//
//        }
//
//    }



    @Override
    public void onStop() {
        super.onStop();
        if (messagesGetAllTask != null) {
            messagesGetAllTask.cancel(true);
        }
    }


}

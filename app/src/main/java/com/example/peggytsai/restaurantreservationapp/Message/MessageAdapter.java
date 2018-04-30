package com.example.peggytsai.restaurantreservationapp.Message;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    private List<Message> messages;
    private Activity context;
    private FragmentManager fragmentManager;
    private MessageGetImageTask messageGetImageTask;
    private MyTask messageDeleteTask;
    private int imageSize;


    public MessageAdapter(List<Message> messages, Activity context, FragmentManager fragmentManager) {
        this.messages = messages;
        this.context = context;
        this.fragmentManager = fragmentManager;
        /* 螢幕寬度除以4當作將圖的尺寸 */
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.recyclerview_message, parent, false);
        return new MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //        ImageView imageView;
        TextView tvMessageTitle;
        TextView tvMessageContent;
        ImageView imageVie;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMessageTitle = itemView.findViewById(R.id.tvMessageTitle);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
            imageVie = itemView.findViewById(R.id.rvMessage);
        }
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MyViewHolder myViewHolder, int position) {

        //抓文字
        final Message message = messages.get(position);
        String url = Common.URL + "/MessageServlet";
        int id = message.getId();
        //開啟asynctask做事，做完事情再來顯示
        messageGetImageTask = new MessageGetImageTask(url, id, imageSize, myViewHolder.imageVie);
        //主程序先往下執行，將文字的部分貼好，事件處理
        messageGetImageTask.execute();
        myViewHolder.tvMessageTitle.setText(message.getMessage_title());
        myViewHolder.tvMessageContent.setText(message.getMessage_content());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MessageDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", message);
                fragment.setArguments(bundle);
                switchFragment(fragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
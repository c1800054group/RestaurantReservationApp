package com.example.peggytsai.restaurantreservationapp.Message;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    private static final String TAG = "MessageAdapter";
    private List<Message> messages;
    private Activity context;
    private FragmentManager fragmentManager;
    private MessageGetImageTask messageGetImageTask;
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
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMessageTitle = itemView.findViewById(R.id.tvMessageTitle);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
            imageView = itemView.findViewById(R.id.ivMessage);

        }
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.MyViewHolder myViewHolder, int position) {

        //抓文字
        final Message message = messages.get(position);
        String url = Common.URL + "/MessageServlet";
        int id = message.getId();
        Bitmap bitmap = null;
        try {
            messageGetImageTask = new MessageGetImageTask(url, id, imageSize, myViewHolder.imageView);
            // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
            bitmap = messageGetImageTask.execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            myViewHolder.imageView.setImageBitmap(bitmap);
        } else {
            myViewHolder.imageView.setImageResource(R.drawable.default_image);
        }




//        //開啟asynctask做事，做完事情再來顯示
//        //主程序先往下執行，將文字的部分貼好，事件處理

        myViewHolder.tvMessageContent.setText(message.getMessage_content());
        final Bitmap finalBitmap = bitmap;
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] b = null;

                Fragment fragment = new MessageDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", message);


                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bundle.putByteArray("image",baos.toByteArray());

                fragment.setArguments(bundle);
                switchFragment(fragment);
            }
        });
//
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
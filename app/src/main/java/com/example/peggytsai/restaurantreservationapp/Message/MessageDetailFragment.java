package com.example.peggytsai.restaurantreservationapp.Message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.R;

import com.example.peggytsai.restaurantreservationapp.Main.Common;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MessageDetailFragment extends Fragment {

    private static final String TAG = "MessageDetailFragment";
    private Button btCoupon;

    private int imageSize;
    private MessageGetImageTask messageGetImageTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_detail, container, false);

        Message message = (Message) getArguments().getSerializable("message");
        byte [] b = getArguments().getByteArray("image");

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_message);

        btCoupon = view.findViewById(R.id.btCoupon);
        btCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Text!", Toast.LENGTH_SHORT).show();
            }
        });
        //圖片接收
        String url = Common.URL + "/MessageServlet";
        ImageView ivMessageDetail = view.findViewById(R.id.ivMessageDetail);
        ivMessageDetail.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));


        //顯示內頁文字
        TextView tvMessageDetailTitle = view.findViewById(R.id.tvMessageDetailTitle);
        tvMessageDetailTitle.setText(message.getMessage_title());

        TextView tvMessageContent = view.findViewById(R.id.tvMessageDetailContent);
        tvMessageContent.setText(message.getMessage_content());

        TextView tvMessageDetailDate = view.findViewById(R.id.tvMessageDetailDate);
        tvMessageDetailDate.setText(message.getMessage_date());




        return view;
    }




    @Override
    public void onStop() {
        super.onStop();
        if (messageGetImageTask != null) {
            messageGetImageTask.cancel(true);
        }
    }
}


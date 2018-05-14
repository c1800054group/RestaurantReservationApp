package com.example.peggytsai.restaurantreservationapp.Message;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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

import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Member.Member;
import com.example.peggytsai.restaurantreservationapp.R;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class MessageDetailFragment extends Fragment {

    private static final String TAG = "MessageDetailFragment";
    private TextView btCoupon, btMessageEdit;

    private int imageSize;
    private MessageGetImageTask messageGetImageTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_detail, container, false);

        final Message message = (Message) getArguments().getSerializable("message");
        byte [] b = getArguments().getByteArray("image");

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_message);

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int authority_id = pref.getInt("authority_id", 1);

        //優惠卷領取
        if (authority_id == 1) {
            tvtoolBarTitle.setText(R.string.text_message);
            btCoupon = view.findViewById(R.id.btCoupon);
            btCoupon.setVisibility(BottomNavigationView.VISIBLE);
            btCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                    int memberID = pref.getInt("memberID", 0);
                    Message message = (Message) getArguments().getSerializable("message");
                    int couponID = message.getCoupon_id();

                    getCoupon(memberID, couponID);
                    Toast.makeText(getActivity(), "成功取得優惠券!", Toast.LENGTH_SHORT).show();
                }
            });
        //編輯
        } else if (authority_id == 4) {
            tvtoolBarTitle.setText(R.string.text_MessageManager);
            btMessageEdit = view.findViewById(R.id.btMessageEdit);
            btMessageEdit.setVisibility(BottomNavigationView.VISIBLE);
            btMessageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Fragment fragment = new MessageEditFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("message", message);
//                    Common.switchFragmentBundle(fragment, getActivity(), false,bundle);

                }
            });

        }


        //圖片接收
//        String url = Common.URL + "/MessageServlet";
        ImageView ivMessageDetail = view.findViewById(R.id.ivMessageDetail);
        ivMessageDetail.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));


        //顯示內頁文字
        TextView tvMessageDetailTitle = view.findViewById(R.id.tvMessageDetailTitle);
        tvMessageDetailTitle.setText(message.getMessage_title());

        TextView tvMessageContent = view.findViewById(R.id.tvMessageDetailContent);
        tvMessageContent.setText(message.getMessage_content());

        TextView tvMessageDetailDate = view.findViewById(R.id.tvMessageDetailDate);
        String couponStar= String.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(new Date(String.valueOf(message.getCoupon_start()))));
        String couponEnd= String.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(new Date(String.valueOf(message.getCoupon_end()))));
        tvMessageDetailDate.setText(couponStar+" - "+couponEnd);

        return view;
    }


    public void getCoupon(int memberID,int couponID){
        String url = Common.URL + "/MessageServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "couponInsert");
        jsonObject.addProperty("couponID", couponID);
        jsonObject.addProperty("memberID", memberID);

        int count = 0;
        try {
            String result = new MyTask(url, jsonObject.toString()).execute().get();
            count = Integer.valueOf(result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (messageGetImageTask != null) {
            messageGetImageTask.cancel(true);
        }
    }
}


package com.example.peggytsai.restaurantreservationapp.Rating;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.R;

public class RatingNewFragment extends Fragment {

    private static final String TAG = "RatingNewFragment";
    private TextView btRatingSave;

    private int imageSize;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_new, container, false);


        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_rating_new);

        btRatingSave = view.findViewById(R.id.btRatingSave);
        btRatingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Text!", Toast.LENGTH_SHORT).show();
            }
        });


        RatingBar ratingBarComment = view.findViewById(R.id.ratingBarComment);
        EditText etCommentName = view.findViewById(R.id.etCommentName);
        EditText etComment = view.findViewById(R.id.etComment);


//        //圖片接收
//        String url = Common.URL + "/MessageServlet";
//        ImageView ivMessageDetail = view.findViewById(R.id.ivMessageDetail);
//        ivMessageDetail.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
//
//
//        //顯示內頁文字
//        TextView tvMessageDetailTitle = view.findViewById(R.id.tvMessageDetailTitle);
//        tvMessageDetailTitle.setText(message.getMessage_title());
//
//        TextView tvMessageContent = view.findViewById(R.id.tvMessageDetailContent);
//        tvMessageContent.setText(message.getMessage_content());
//
//        TextView tvMessageDetailDate = view.findViewById(R.id.tvMessageDetailDate);
//        tvMessageDetailDate.setText(message.getMessage_date());
//



        return view;
    }



}


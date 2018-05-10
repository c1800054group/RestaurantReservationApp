package com.example.peggytsai.restaurantreservationapp.Rating;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RatingNewFragment extends Fragment {

    private static final String TAG = "RatingNewFragment";
    private TextView btRatingSave, tvCommentName;

    private RatingBar ratingBarComment;
    private EditText etComment;

    private int imageSize;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_new, container, false);


        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_rating_new);

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String member_name = pref.getString("memberName", "");

        ratingBarComment = view.findViewById(R.id.ratingBarComment);
        tvCommentName = view.findViewById(R.id.tvCommentName);
        tvCommentName.setText(member_name);

        etComment = view.findViewById(R.id.etComment);


        btRatingSave = view.findViewById(R.id.btRatingSave);
        btRatingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Float score = ratingBarComment.getRating();
                if (score <= 0) {
                    Common.showToast(getActivity(), R.string.msg_RatingIsInvalid);
                    return;
                }

                String Comment = etComment.getText().toString().trim();
                if (Comment.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_CommentIsInvalid);
                    return;
                }

                if (Common.networkConnected(getActivity())) {
                    String url = Common.URL + "/RatingServlet";
                    SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                    String member_name = pref.getString("memberName", "");
                    String comment_reply = null;
                    int member_id = pref.getInt("memberID", 0);
                    RatingPage ratingPage = new RatingPage(0, Comment, "0", member_name, member_id, score, comment_reply);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "ratingInsert");
                    jsonObject.addProperty("rating", new Gson().toJson(ratingPage));
                    int count = 0;
                    try {
                        String result = new MyTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_RatingInsertFail);
                    } else {
                        Common.showToast(getActivity(), R.string.msg_RatingInsertSuccess);
                        Fragment ratingFragment = new RatingFragment();
                        Common.switchFragment(ratingFragment, getActivity(), false);
                    }
                } else {
                    Common.showToast(getActivity(), R.string.msg_NoNetwork);
                }
                /* 回前一個Fragment */
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }


}


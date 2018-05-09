package com.example.peggytsai.restaurantreservationapp.Rating;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Message.MessageDetailFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private static final String TAG = "RatingAdapter";
    private List<RatingPage> ratingPages;
    private Activity context;
    private FragmentManager fragmentManager;
    private MyTask ratingGetReplyTask;


    public RatingAdapter(List<RatingPage> ratingPages, Activity context, FragmentManager fragmentManager) {
        this.ratingPages = ratingPages;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.recyclerview_rating, parent, false);
        return new MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView tvRatingContent, tvRatingMember, tvRatingDate, tvRatingReplayTitle, tvRatingReplay;
        View ratingLineView;
        EditText etCommentReply;
        Button btCommentSave;

        public MyViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingContent = itemView.findViewById(R.id.tvRatingContent);
            tvRatingMember = itemView.findViewById(R.id.tvRatingMember);
            tvRatingDate = itemView.findViewById(R.id.tvRatingDate);

            ratingLineView = itemView.findViewById(R.id.ratingLineView);
            tvRatingReplayTitle = itemView.findViewById(R.id.tvRatingReplayTitle);
            tvRatingReplay = itemView.findViewById(R.id.tvRatingReplay);

            etCommentReply = itemView.findViewById(R.id.etCommentReply);
            btCommentSave = itemView.findViewById(R.id.btCommentSave);

        }
    }

    @Override
    public void onBindViewHolder(final RatingAdapter.MyViewHolder myViewHolder, int position) {

        //抓文字
        final RatingPage ratingPage = ratingPages.get(position);
        myViewHolder.tvRatingContent.setText(ratingPage.getComment());
        myViewHolder.ratingBar.setRating(ratingPage.getScore());
        myViewHolder.tvRatingMember.setText(ratingPage.getMember_name());
        myViewHolder.tvRatingDate.setText(ratingPage.getComment_time());
        myViewHolder.tvRatingReplay.setText(ratingPage.getComment_reply());

        if (ratingPage.getComment_reply() != null) {

            myViewHolder.ratingLineView.setVisibility(BottomNavigationView.VISIBLE);
            myViewHolder.tvRatingReplayTitle.setVisibility(BottomNavigationView.VISIBLE);
            myViewHolder.tvRatingReplay.setVisibility(BottomNavigationView.VISIBLE);

        }


        SharedPreferences pref = context.getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int authority_id = pref.getInt("authority_id", 1);

        if (authority_id == 4) {

            if (Common.networkConnected(context)) {

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myViewHolder.ratingLineView.setVisibility(BottomNavigationView.VISIBLE);
                        myViewHolder.tvRatingReplayTitle.setVisibility(BottomNavigationView.VISIBLE);

                        if (ratingPage.getComment_reply() == null) {
                            myViewHolder.etCommentReply.setVisibility(BottomNavigationView.VISIBLE);
                            myViewHolder.btCommentSave.setVisibility(BottomNavigationView.VISIBLE);

                        } else {

                            myViewHolder.tvRatingReplay.setVisibility(BottomNavigationView.GONE);
                            myViewHolder.etCommentReply.setText(ratingPage.getComment_reply());
                            myViewHolder.etCommentReply.setVisibility(BottomNavigationView.VISIBLE);
                            myViewHolder.btCommentSave.setVisibility(BottomNavigationView.VISIBLE);
                        }

                    }
                });

                myViewHolder.btCommentSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment_reply = myViewHolder.etCommentReply.getText().toString().trim();
                        ratingPage.setComment_reply(myViewHolder.etCommentReply.getText().toString().trim());
                        int commend_id = ratingPage.getId();

                        String url = Common.URL + "/RatingServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "updateReply");
                        jsonObject.addProperty("comment_reply", comment_reply);
                        jsonObject.addProperty("commend_id", commend_id);

                        int count = 0;
                        try {
                            String result = new MyTask(url, jsonObject.toString()).execute().get();
                            count = Integer.valueOf(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                        myViewHolder.etCommentReply.setVisibility(BottomNavigationView.GONE);
                        myViewHolder.tvRatingReplay.setText(comment_reply);
                        myViewHolder.tvRatingReplay.setVisibility(BottomNavigationView.VISIBLE);

                    }
                });

            } else {
                Common.showToast(context, R.string.msg_NoNetwork);
            }


        }


    }

    @Override
    public int getItemCount() {
        return ratingPages.size();
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.Content, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

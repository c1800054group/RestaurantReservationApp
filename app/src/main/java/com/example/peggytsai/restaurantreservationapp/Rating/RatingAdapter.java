package com.example.peggytsai.restaurantreservationapp.Rating;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;


import java.io.ByteArrayOutputStream;
import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private static final String TAG = "RatingAdapter";
    private List<RatingPage> ratings;
    private Activity context;
    private FragmentManager fragmentManager;

    public RatingAdapter(List<RatingPage> ratings, Activity context, FragmentManager fragmentManager) {
        this.ratings = ratings;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.recyclerview_message, parent, false);
        return new RatingAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView tvRatingContent;
        TextView tvRatingMember;
        TextView tvRatingDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingContent = itemView.findViewById(R.id.tvRatingContent);
            tvRatingMember = itemView.findViewById(R.id.tvRatingMember);
            tvRatingDate = itemView.findViewById(R.id.tvRatingDate);

        }
    }

    @Override
    public void onBindViewHolder(final RatingAdapter.MyViewHolder myViewHolder, int position) {

        //抓文字
        final RatingPage rating = ratings.get(position);
        String url = Common.URL + "/RatingServlet";
        int id = rating.getId();
//        myViewHolder.ratingBar.setRating(Float.parseFloat(String.valueOf(rating.getScore())));
        myViewHolder.tvRatingContent.setText(rating.getComment());
        myViewHolder.tvRatingMember.setText(rating.getMember_name());
        myViewHolder.tvRatingDate.setText(rating.getComment_time());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }


}

package com.example.peggytsai.restaurantreservationapp.Rating;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.R;


import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private static final String TAG = "RatingAdapter";
    private List<RatingPage> ratingPages;
    private Activity context;
    private FragmentManager fragmentManager;

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
        final RatingPage ratingPage = ratingPages.get(position);
        String url = Common.URL + "/RatingServlet";
        int id = ratingPage.getId();

        myViewHolder.tvRatingContent.setText(ratingPage.getComment());
        myViewHolder.ratingBar.setRating(ratingPage.getScore());
        myViewHolder.tvRatingMember.setText(ratingPage.getMember_name());
        myViewHolder.tvRatingDate.setText(ratingPage.getComment_time());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return ratingPages.size();
    }


}

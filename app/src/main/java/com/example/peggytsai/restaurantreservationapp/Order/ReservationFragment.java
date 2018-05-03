package com.example.peggytsai.restaurantreservationapp.Order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MainActivity;

import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.security.PrivateKey;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReservationFragment extends Fragment {
    private final static String TAG = "ReservationFragment";
    private View view;
    private TextView tvTimeContent, tvDateContent,edPersonNumber;
    private Button dateButton, timeButton, confirmButton;
    private ReservationInsertTask reservationTask;
    private String jsonStr = "";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_reservation, container, false);


        findView();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view) {
                showTime();
            }


        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                insertDateData();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_layout, null);

                TextView title = (TextView) view.findViewById(R.id.title);
//                ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                Button order = view.findViewById(R.id.order);

                title.setText("Hello There!");
                insertDateData();

//                imageButton.setImageResource(R.drawable.default_image);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Thank you", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "Never Mind!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(view);
                builder.show();




//                PopupMenu pm = new PopupMenu(getActivity(), confirmButton);
//                pm.getMenuInflater().inflate(R.menu.reservation_menu, pm.getMenu());
//                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.first:
//                                Toast.makeText(getActivity(), "clicked first meni", Toast.LENGTH_SHORT).show();
//                                return true;
//                            case R.id.second:
//                                insertDateData();
//                                return true;
//                            case R.id.therd:
//                                Toast.makeText(getActivity(), "clicked thred meni", Toast.LENGTH_SHORT).show();
//                                return true;
//
//
//                        }
//                        return true;
//                    }
//                });




            }
        });


        return view;
    }

    private void insertDateData() {



        boolean isVaild = true;
        String date = tvDateContent.getText().toString();
        if (date.trim().isEmpty()) {
            tvTimeContent.setError("請選擇這日期");
           isVaild = false;
        }
        String time = tvTimeContent.getText().toString();
        if (time.trim().isEmpty()) {
            tvTimeContent.setError("請選擇時間");
            isVaild = false;
        }
        String person = edPersonNumber.getText().toString();
        if (person.trim().isEmpty()) {
            edPersonNumber.setError("請填入人數");
            isVaild = false;
        }
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        String d = "2001-03-15 15:37:05";
        String d = date + " " + time;
//            ts = Timestamamp.valueOf(d);
//        String t = time;
//        SimpleDateFormat simpleDateFormat  =  new  SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//24小時制
//        try {
//            LongDate = simpleDateFormat.parse(d).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if (isVaild) {
            if (Common.networkConnected(getActivity())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "insert");
                jsonObject.addProperty("date", d);
//                jsonObject.addProperty("time", LongDate);
                jsonObject.addProperty("person", person);

                try {
                    reservationTask = new ReservationInsertTask(Common.URL
                            + "/ReserveSerVlet", jsonObject.toString());
                    int count = Integer.valueOf(reservationTask.execute().get());

                    if (count == 0) {
                        Toast.makeText(getActivity(), "Reservation failed", Toast.LENGTH_LONG).show();
                    } else {
                        Fragment reservationDetailFragment = new  ReservationDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("date", date);
                        bundle.putString("time", person);
                        bundle.putString("person", person);
                        reservationDetailFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.Content, reservationDetailFragment).addToBackStack(null).commit();
                    }


                } catch (Exception e) {
                    Log.e(TAG, "error message" + e.toString());
                }
            }else {
                Toast.makeText(getActivity(), "connection to netWork failed", Toast.LENGTH_LONG).show();
            }

        }



    }

    private void findView() {

        tvDateContent = view.findViewById(R.id.tvDateContent);
        tvTimeContent = view.findViewById(R.id.tvTimeContent);
        dateButton = view.findViewById(R.id.dateButton);
        timeButton = view.findViewById(R.id.timeButton);
        confirmButton = view.findViewById(R.id.confirmButton);
        edPersonNumber = view.findViewById(R.id.edPersonNumber);

    }





    private void showDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m = m + 1;
                tvDateContent.setText(y + "-" + m + "-" + d);
            }
        }
                , yy, mm, dd).show();
    }

    private void showTime() {
        final Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mmm = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                tvTimeContent.setText(i + ":" + i1 + ":" + "00");
            }
        }
                , hh, mmm, true).show();

    }




    public static class DatePickerDialogFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            MainActivity activity = (MainActivity) getActivity();
            return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);

        }

        @Override
        public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {

        }


    }



    public static class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
            int mMinute = calendar.get(Calendar.MINUTE);
            MainActivity activity = (MainActivity) getActivity();
            return new TimePickerDialog(getActivity(), this, mHour, mMinute, false);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hh, int mmm) {

        }

      
    }

    public void onStop() {
        super.onStop();
        if (reservationTask != null) {
            reservationTask.cancel(true);
        }
    }




}

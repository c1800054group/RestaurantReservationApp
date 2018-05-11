package com.example.peggytsai.restaurantreservationapp.Order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MainActivity;

import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentShow;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ReservationFragment extends Fragment {
    private final static String TAG = "ReservationFragment";
    private View view;
    private TextView tvTimeContent, tvDateContent, edPersonNumber, confirmButton;
    private LinearLayout dateButton, timeButton;
    private ReservationInsertTask reservationTask;
    private String jsonStr = "";
    private BottomNavigationView navigation;
    private static int TIME_PICKER_DIALOG_TAG;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            @Override
            public void onClick(View view) {
                String date1 = tvDateContent.getText().toString().trim();

                Log.d("date1: ", date1);
                if (date1.equals("____________")) {

                    Common.showToast(getActivity(), "請先選擇日期時間與人數");
                } else {
                    insertDateData();
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_layout, null);
                    Button customConButton = view.findViewById(R.id.CustomConButton);
                    Button customNotButton = view.findViewById(R.id.CustomNotButton);
                    Button CustomCancelButton = view.findViewById(R.id.CustomcancelButton);
                    builder.setView(view);

                    final AlertDialog alertDialog = builder.show();

                    customConButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.switchFragment(new CartFragmentShow(), getActivity(), true);
                            alertDialog.cancel();
                        }
                    });


                    CustomCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                    customNotButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            insertDateData();
                            AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                            alertDialog1.setMessage("定位完成 若要稍後點餐,請至訂單查詢修改");
                            alertDialog1.setButton(DialogInterface.BUTTON_POSITIVE, "返回主頁", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Common.showToast(getActivity(), "請先選擇日期時間與人數");
                                }
                            });
                        }
                    });
                }

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

        String d = date + " " + time;

        if (isVaild) { //之後提取用
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            pref.edit()
                    .putString("日期時間", d)
                    .putString("人數", person.trim())
                    .apply();
        }


        if (isVaild) {
            if (Common.networkConnected(getActivity())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "insert");
                jsonObject.addProperty("date", d);
                jsonObject.addProperty("person", person);

                try {
                    reservationTask = new ReservationInsertTask(Common.URL
                            + "/ReserveSerVlet", jsonObject.toString());
                    int count = Integer.valueOf(reservationTask.execute().get());

                    if (count == 0) {
                        Toast.makeText(getActivity(), "Reservation failed", Toast.LENGTH_LONG).show();
                    } else {
                        navigation.setSelectedItemId(R.id.item_Message);
                    }


                } catch (Exception e) {
                    Log.e(TAG, "error message" + e.toString());
                }
            } else {
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
        navigation = getActivity().findViewById(R.id.Navigation);
    }


    private void showDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                //個位數補0
                int m1 = m + 1;
                String month = m1 < 10 ? "/0" + m1 : "/" + m1;
                String date = d < 10 ? "/0" + d : "/" + d;

                tvDateContent.setText(y + month + date);

            }
        }, yy, mm, dd);

        DatePicker dp = datePickerDialog.getDatePicker();

        //設定最早時間為明天
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dp.setMinDate(calendar.getTimeInMillis());

        //設定最久時間為4週後
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
        dp.setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();

    }

    private void showTime() {
        final Calendar calendar = Calendar.getInstance();
//        Min
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mmm = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                tvTimeContent.setText(i + ":" + i1 + ":" + "00");
            }
        }
                , hh, mmm, true).show();

//        TimePicker tp = timePickerDialog.getDatePicker();
//
//        //設定最早時間為明天
//        calendar.add(Calendar.HOUR_OF_DAY,10);
//        tp.setHour(calendar.getTimeInMillis());
//
//        //設定最久時間為4週後
//        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH,4);
//        dp.setMaxDate(calendar.getTimeInMillis());

//
//        minutePicker = (NumberPicker) timePicker
//                .findViewById(field.getInt(null));
//
//        minutePicker.setMinValue(0);
//        minutePicker.setMaxValue(3);
//
//
//        datePickerDialog.show();

//        TIME_PICKER_DIALOG_TAG = 2;
//
//        Calendar now = Calendar.getInstance();
//
//        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                getActivity().AddTaskActivity.this,
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                mHoursMode
//        );
//
//        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                Log.d("TimePicker", "Dialog was cancelled");
//            }
//        });
//        tpd.show(getFragmentManager(), "Timepickerdialog");

//        TimePicker tp = (TimePicker)this.findViewById(R.id.timePicker);
//
//        java.util.Formatter timeF = new java.util.Formatter();
//        timeF.format("Time defaulted to %d:%02d", tp.getCurrentHour(),
//                tp.getCurrentMinute());
//        timeDefault.setText(timeF.toString());
//
//        tp.setIs24HourView(true);
//        tp.setCurrentHour(new Integer(10));
//        tp.setCurrentMinute(new Integer(10));

    }


    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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


//    public static class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
//
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Calendar calendar = Calendar.getInstance();
//            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
//            int mMinute = calendar.get(Calendar.MINUTE);
//            MainActivity activity = (MainActivity) getActivity();
//            return new TimePickerDialog(getActivity(), this, mHour, mMinute, false);
//        }
//
//        @Override
//        public void onTimeSet(TimePicker timePicker, int hh, int mmm) {
//
//            String hourString = hh < 10 ? "0"+hh : ""+hh;
//            String minuteString = mmm < 10 ? "0"+mmm : ""+mmm;
//
//
//            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
//            Calendar mCalendar = Calendar.getInstance();
//            mCalendar.set(Calendar.HOUR_OF_DAY, hh);
//            mCalendar.set(Calendar.MINUTE, mmm);
//            mCalendar.set(Calendar.SECOND,0);
//
////            Date date = mCalendar.getTime();
////            if(TIME_PICKER_DIALOG_TAG == 2) {
////                alertTime.setText(df.format(date));
////            }else if(TIME_PICKER_DIALOG_TAG  == 4){
////                dueTime.setText(df.format(date));
////            }
//
//        }
//
//
//    }


    public class CustomTimePickerDialog extends TimePickerDialog {

        private final static int TIME_PICKER_INTERVAL = 5;
        private TimePicker mTimePicker;
        private final OnTimeSetListener mTimeSetListener;
        private final boolean mIs24HourView;

        public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                      int hourOfDay, int minute, boolean is24HourView) {
            super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                    minute / TIME_PICKER_INTERVAL, is24HourView);
            mTimeSetListener = listener;
            mIs24HourView = false;
        }

        @Override
        public void updateTime(int hourOfDay, int minuteOfHour) {
            mTimePicker.setCurrentHour(hourOfDay);
            mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    if (mTimeSetListener != null) {
                        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                                mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    }
                    break;
                case BUTTON_NEGATIVE:
                    cancel();
                    break;
            }
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            try {
                Class<?> classForid = Class.forName("com.android.internal.R$id");
                Field timePickerField = classForid.getField("timePicker");
                mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
                Field field = classForid.getField("minute");

                NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                        .findViewById(field.getInt(null));
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
                List<String> displayedValues = new ArrayList<>();
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
                minuteSpinner.setDisplayedValues(displayedValues
                        .toArray(new String[displayedValues.size()]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onStop() {
        super.onStop();
        if (reservationTask != null) {
            reservationTask.cancel(true);
        }
    }


}

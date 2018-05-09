package com.example.peggytsai.restaurantreservationapp.Order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MainActivity;

import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentShow;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;

import java.util.Calendar;


public class ReservationFragment extends Fragment {
    private final static String TAG = "ReservationFragment";
    private View view;
    private TextView tvTimeContent, tvDateContent, edPersonNumber;
    private Button dateButton, timeButton, confirmButton;
    private ReservationInsertTask reservationTask;
    private String jsonStr = "";
    private BottomNavigationView navigation;

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
                }else {
                    insertDateData();
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_layout, null);
                    Button customConButton = view.findViewById(R.id.CustomConButton);
                    Button customNotButton = view.findViewById(R.id.CustomNotButton);
                    Button CustomcancelButton = view.findViewById(R.id.CustomcancelButton);
                    builder.setView(view);

                    final AlertDialog alertDialog = builder.show();

                    customConButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.switchFragment(new CartFragmentShow(),getActivity(),true);
                            alertDialog.cancel();
                        }
                    });


                    CustomcancelButton.setOnClickListener(new View.OnClickListener() {
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

package com.example.peggytsai.restaurantreservationapp.Order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MainActivity;

import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentShow;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.JsonObject;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class ReservationFragment extends Fragment {
    private final static String TAG = "ReservationFragment";
    private View view;
    private TextView tvTimeContent, tvDateContent, confirmButton ,edPersonNumber;
    private LinearLayout dateButton, timeButton, personButton;
    private ReservationInsertTask reservationTask;
    private String jsonStr = "";
    private BottomNavigationView navigation;
    private static int TIME_PICKER_DIALOG_TAG;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_reservation, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_Reservaton);

        findView();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
                tvDateContent.setVisibility(TextView.VISIBLE);
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
                tvTimeContent.setVisibility(TextView.VISIBLE);
            }
        });

        personButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPerson();
                edPersonNumber.setVisibility(TextView.VISIBLE);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            @Override
            public void onClick(View view) {
                String date1 = tvDateContent.getText().toString().trim();
                String time1 = tvTimeContent.getText().toString().trim();
                String person1 = edPersonNumber.getText().toString().trim();

                Log.d("date1: ", date1);
                if (date1.trim().isEmpty() && time1.trim().isEmpty() && person1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "日期、時間與人數尚未填寫");
                } else if (date1.trim().isEmpty() && time1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "日期與時間尚未填寫");
                } else if (date1.trim().isEmpty() && person1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "日期與人數尚未填寫");
                } else if (date1.trim().isEmpty() && time1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "日期與時間尚未填寫");
                } else if (time1.trim().isEmpty() && person1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "時間與人數尚未填寫");
                } else if (date1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "日期尚未填寫");
                } else if (time1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "時間尚未填寫");
                } else if (person1.trim().isEmpty()) {
                    Common.showToast(getActivity(), "人數尚未填寫");
                } else {
                    insertDateData();
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_order_reservation_layout, null);
                    Button customConButton = view.findViewById(R.id.CustomConButton);
                    Button customNotButton = view.findViewById(R.id.CustomNotButton);
                    Button CustomCancelButton = view.findViewById(R.id.CustomCancelButton);
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

//                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                            alertDialog.setTitle( Html.fromHtml("<font color='#005B4F'>"+getResources()
//                                    .getString(R.string.text_LiveOrdering)+"</font>"));
//                            alertDialog.setMessage(message);
//                            alertDialog.setCancelable(true);
//                            alertDialog.setButton(BUTTON_POSITIVE,"確定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Fragment cartFragmentShow = new CartFragmentShow();
//                                    Common.FragmentSwitch = 1;
//                                    Common.switchFragment(cartFragmentShow,getActivity(),true);
//                                }
//                            });
//                            alertDialog.setButton(BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                            alertDialog.show();
//



                            alertDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Html.fromHtml("<font color='#009688'>"+"訂位完成"+"</font>"));
                            builder.setMessage("若要稍後點餐\n請至\"訂單查詢\"修改");
                            builder.setNegativeButton("前往訂單查詢", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    navigation.setSelectedItemId(R.id.item_Check);
                                }
                            });

                            builder.setPositiveButton("返回優惠訊息", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    navigation.setSelectedItemId(R.id.item_Message);
                                }
                            });

                            AlertDialog alertDialog1 = builder.create();
                            alertDialog1.show();
                            Button nbutton = alertDialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
                            nbutton.setTextColor(getResources().getColor(R.color.colorButton));
                            Button pbutton = alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE);
                            pbutton.setTextColor(getResources().getColor(R.color.colorButton));

                        }
                    });
                }

            }
        });


        return view;
    }

    private void insertDateData() {

        boolean isVaild = true;
        String date = tvDateContent.getHint().toString();
        if (date.trim().isEmpty()) {
            tvTimeContent.setError("請選擇日期");
            isVaild = false;
        }
        String time = tvTimeContent.getHint().toString();
        if (time.trim().isEmpty()) {
            tvTimeContent.setError("請選擇時間");
            isVaild = false;
        }
        String person = edPersonNumber.getHint().toString();
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
        personButton = view.findViewById(R.id.personButton);
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
                String month2 = month.replace("/","-");
                String date2 = date.replace("/","-");
                tvDateContent.setHint(y + month2 + date2);

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

        final String[] setTime = {"11:00","11:30","12:00","12:30",
                "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30",
                "17:00","17:30","18:00","18:30","19:00","19:30","20:00"};

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(getActivity());
        dialog_list.setTitle("請選擇訂位時間");
        dialog_list.setItems(setTime, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                tvTimeContent.setText(setTime[which]);
                String setTime1 = setTime[which] + ":00";
                tvTimeContent.setHint(setTime1);
            }
        });
        dialog_list.show();

    }

    private void showPerson() {

        final String[] setPerson = {"1","2","3","4","5","6","7","8","9","10","11","12",
                "13","14","15","16","17","18","19","20"};

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(getActivity());
        dialog_list.setTitle("請選擇訂位人數");
        dialog_list.setItems(setPerson, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                edPersonNumber.setText(setPerson[which] + " 人");
                edPersonNumber.setHint(setPerson[which] );
            }
        });
        dialog_list.show();

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

    public void onStop() {
        super.onStop();
        if (reservationTask != null) {
            reservationTask.cancel(true);
        }
    }


}

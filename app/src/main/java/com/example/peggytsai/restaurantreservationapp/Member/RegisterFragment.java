package com.example.peggytsai.restaurantreservationapp.Member;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.peggytsai.restaurantreservationapp.Login.LoginFragment;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {
    private View view;
    private EditText etInputName,etInputAccount,etInputPassword,etInputCheckPassword,etInputPhone;
    private TextView btRegister;
    private RadioGroup rgGender;
    private final static String TAG = "RegisterFragment";
    private String sex = "1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member_register,container,false);
        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_member);
        findViews();
        return view;
    }

    private void findViews() {
        btRegister = view.findViewById(R.id.btRegister);
        etInputName =view.findViewById(R.id.etInputName);
        rgGender = view.findViewById(R.id.rgGender);
        etInputAccount =view.findViewById(R.id.etInputAccount);
        etInputPassword =view.findViewById(R.id.etInputPassword);
        etInputCheckPassword =view.findViewById(R.id.etInputCheckPassword);
        etInputPhone =view.findViewById(R.id.etInputPhone);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                if (radioButton.getText().toString().equals("先生")){
                    sex = "1";
                }else {
                    sex = "2";
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etInputName.getText().toString().trim();
                String email = etInputAccount.getText().toString().trim();
                String password = etInputPassword.getText().toString().trim();
                String checkPassword = etInputCheckPassword.getText().toString().trim();
                String phone = etInputPhone.getText().toString().trim();
                //判斷email格式
                String EMAIL_REGEX ="^\\w+\\.*\\w+@(\\w+\\.){1,5}[a-zA-Z]{2,3}$";
                if (name.length()<= 0){
                    Common.showToast(getActivity(), R.string.msg_InvalidName);
                    return;
                }

                if (!email.matches(EMAIL_REGEX)){
                    Common.showToast(getActivity(),"email格式錯誤");
                    return;
                }

                //再次確認密碼
                if (!password.equals(checkPassword)){
                    Common.showToast(getActivity(), R.string.msg_PasswordError);
                    return;
                }

                if (password.length()<4){
                    Common.showToast(getActivity(), R.string.msg_InvalidPassword);
                    return;
                }

                if (phone.length()<= 0){
                    Common.showToast(getActivity(),R.string.msg_InvalidPhone);
                    return;
                }

                if (Common.networkConnected(getActivity())) {
                    String url = Common.URL + "/MemberServlet";
                    Member member = new Member("0", name, sex, phone,email,password );
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "memberInsert");
                    jsonObject.addProperty("member", new Gson().toJson(member));
                    int count = 0;
                    try {
                        String result = new MyTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_InsertFail);
                    } else if (count == -1){
                        Common.showToast(getActivity(), R.string.msg_AccountExists);
                    }else {
                        Common.showToast(getActivity(), R.string.msg_InsertSuccess);
                        Fragment loginFragment = new LoginFragment();
                        Common.switchFragment(loginFragment,getActivity(),false);
                    }
                } else {
                    Common.showToast(getActivity(), R.string.msg_NoNetwork);
                }

            }
        });

    }
}

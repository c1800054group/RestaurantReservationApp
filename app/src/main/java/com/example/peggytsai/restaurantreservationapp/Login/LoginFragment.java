package com.example.peggytsai.restaurantreservationapp.Login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Member.RegisterFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private TextView tvRigster;
    private EditText etEmail,etPassword;
    private Button btLogin;
    private View view;
    private MyTask loginTask;
    private BottomNavigationView navigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,container,false);
        findView();
        navigation = getActivity().findViewById(R.id.Navigation);
        navigation.setVisibility(BottomNavigationView.VISIBLE);
        return view;
    }

    private void findView() {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btLogin = view.findViewById(R.id.btLogin);
        tvRigster = view.findViewById(R.id.tvRegister);
        tvRigster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment registerFragment = new RegisterFragment();
                Common.switchFragment(registerFragment,getActivity(),true);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (email.length() <= 0 || password.length() <= 0) {
                    Common.showToast(getActivity(),R.string.msg_InvalidEmailOrPassword);
                    return;
                }

                if (isUserValid(email, password)) {
                    SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE,
                            MODE_PRIVATE);
                    pref.edit()
                            .putBoolean("login", true)
                            .putString("email", email)
                            .putString("password", password)
                            .apply();
                    getActivity().setResult(RESULT_OK);
                    Fragment messaFragment = new MessageFragment();
                    Common.switchFragment(messaFragment,getActivity(),false);

                } else {
                    Common.showToast(getActivity(),R.string.msg_InvalidUserOrPassword);
                }
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);//後面是沒取得值的話預設給false
        if (login) {
            String name = pref.getString("email", "");
            String password = pref.getString("password", "");
            if (isUserValid(name, password)) {
                getActivity().setResult(getActivity().RESULT_OK);
                //已登入的話直接進到首頁
                Fragment messageFragment = new MessageFragment();
                Common.switchFragment(messageFragment,getActivity(),false);

            } else {
                pref.edit().putBoolean("login",false).apply(); //把偏好設定改成flase然後跳出登入畫面，因沒寫finish()所以會跳出
                Common.showToast(getActivity(),R.string.msg_InvalidUserOrPassword);
            }
        }
    }

    private boolean isUserValid(String email, String password) {
        boolean isUserValid = false;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isUserValid");
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);
            loginTask = new MyTask(url, jsonObject.toString());
            int memberID = 0;
            try {
                String jsonIn = loginTask.execute().get();
                jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                isUserValid = jsonObject.get("isUserValid").getAsBoolean();
                memberID = jsonObject.get("memberId").getAsInt();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (isUserValid) {
                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit().putInt("memberID", memberID).apply();
            }
        }else{
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return isUserValid;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (loginTask != null) {
            loginTask.cancel(true);
        }
    }

}


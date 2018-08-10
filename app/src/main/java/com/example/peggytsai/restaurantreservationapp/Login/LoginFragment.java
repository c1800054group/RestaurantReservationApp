package com.example.peggytsai.restaurantreservationapp.Login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Check.CheckFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckWaiterFragment;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Manager.FoodManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Member.RegisterFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.WaiterTabFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private TextView tvRigster;
    private EditText etEmail,etPassword;
    private Button btLogin;
    private View view;
    private MyTask loginTask;
    public BottomNavigationView navigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,container,false);
        findView();
        navigation = getActivity().findViewById(R.id.Navigation);
        navigation.getMenu().clear();
        navigation.setVisibility(BottomNavigationView.GONE);

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        pref.edit().putString("桌號","").apply();

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
                    int authority_id = pref.getInt("authority_id",1);

                    authority(authority_id);

                    navigation.setVisibility(BottomNavigationView.VISIBLE);

                } else {
                    Common.showToast(getActivity(),R.string.msg_InvalidUserOrPassword);
                }
            }

        });
    }
    //依權限切換fragment
    private void authority(int authority_id) {
        switch (authority_id){
            case 1:
//                if (!(navigation.getSelectedItemId() == R.id.item_Message)){
                    navigation.inflateMenu(R.menu.navigate_menu);
//                }
                Fragment messageFragment = new MessageFragment();
                Common.switchFragment(messageFragment,getActivity(),false);

                break;
            case 2:
                Fragment chefFragment = new CheckFragment();
                Common.switchFragment(chefFragment,getActivity(),false);
                break;
            case 3:
//                if (!(navigation.getSelectedItemId() == R.id.item_CheckWaiter)){
                    navigation.inflateMenu(R.menu.navigate_menu_waiter);
//                }
                Fragment waiterTabFragment = new WaiterTabFragment();
                Common.switchFragment(waiterTabFragment,getActivity(),false);
                break;
            case 4:
//                if (!(navigation.getSelectedItemId() == R.id.item_FoodManager)){
                    navigation.inflateMenu(R.menu.navigate_menu_manager);
//                }
                Fragment foodManagerFragment = new FoodManagerFragment();
                Common.switchFragment(foodManagerFragment,getActivity(), false);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);//後面是沒取得值的話預設給false
        if (login) {
            String name = pref.getString("email", "");
            String password = pref.getString("password", "");
            int authority_id = pref.getInt("authority_id",1);
            if (isUserValid(name, password)) {
                navigation.setVisibility(BottomNavigationView.VISIBLE);
                //已登入的話直接進到首頁

                authority(authority_id);

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
            int authority_id = 0;
            String memberName = "";
            try {
                String jsonIn = loginTask.execute().get();
                jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                isUserValid = jsonObject.get("isUserValid").getAsBoolean();
                memberID = jsonObject.get("memberId").getAsInt();
                authority_id = jsonObject.get("authority_id").getAsInt();
                memberName = jsonObject.get("memberName").getAsString();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (isUserValid) {
                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit().putInt("memberID", memberID).apply();
                pref.edit().putInt("authority_id", authority_id).apply();
                pref.edit().putString("memberName", memberName).apply();
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


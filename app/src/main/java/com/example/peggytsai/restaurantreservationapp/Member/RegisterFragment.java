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


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class RegisterFragment extends Fragment {
    private View view;
    private EditText etInputName,etInputAccount,etInputPassword,etInputCheckPassword,etInputPhone;
    private Button btRegister;
    private RadioGroup rgGender;
    private final static String TAG = "RegisterFragment";
    private String sex = "1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.registerfragment,container,false);
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

                if (name.length()<= 0 || email.length()<=0 || password.length()<=0 || phone.length()<=0){
                    Common.showToast(getActivity(), R.string.msg_ColumnNull);
                    return;
                }

                //再次確認密碼
                if (!password.equals(checkPassword)){
                    Common.showToast(getActivity(), R.string.msg_PasswordError);
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
                    } else {
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

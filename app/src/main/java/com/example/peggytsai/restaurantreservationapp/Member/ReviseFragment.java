package com.example.peggytsai.restaurantreservationapp.Member;


import android.content.SharedPreferences;
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


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static android.content.Context.MODE_PRIVATE;

public class ReviseFragment extends Fragment {
    private static String TAG = "ReviseFragment";
    private View view;
    private Member member;
    private EditText etName,etPassword,etPhone,etCheckPassword;
    private TextView tvEmail;
    private RadioGroup rgReviseGender;
    private RadioButton rbFemale;
    private Button btReviseSave;
    private String name,sex,password,checkPassword,phone,email,id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.revise_fragment,container,false);
        Bundle bundle = this.getArguments();
        member = (Member)bundle.getSerializable("member");
        findView();
        return view;
    }

    private void findView() {
        etName = view.findViewById(R.id.etReviseName);
        tvEmail = view.findViewById(R.id.etReviseEmail);
        etPassword = view.findViewById(R.id.etRevisePassword);
        etCheckPassword = view.findViewById(R.id.etReviseCheckPassword);
        etPhone = view.findViewById(R.id.etRevisePhone);
        rbFemale = view.findViewById(R.id.rbReviseFemale);
        btReviseSave = view.findViewById(R.id.btReviseSave);
        rgReviseGender = view.findViewById(R.id.rgReviseGender);

        if (!member.getSex().equals("1")){
            rbFemale.setChecked(true);
        }

        etName.setText(member.getName());
        tvEmail.setText(member.getEmail());
        etPassword.setText(member.getPassword());
        etPhone.setText(member.getPhone());


        btReviseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                email = tvEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                checkPassword =etCheckPassword.getText().toString().trim();
                phone = etPhone.getText().toString().trim();

                if (!password.equals(checkPassword)){
                    Common.showToast(getActivity(), R.string.msg_PasswordError);
                    return;
                }

                switch (rgReviseGender.getCheckedRadioButtonId()){
                    case R.id.rbReviseMale:
                        sex = "1";
                        break;
                    case R.id.rbReviseFemale:
                        sex = "2";
                        break;
                }

                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                id = String.valueOf(pref.getInt("memberID",0));
                //偏好設定裡沒ＩＤ則跳回登入畫面
                if (id.equals("0")){
                    Common.showToast(getActivity(), R.string.msg_noLogin);
                    Fragment loginFragment = new LoginFragment();
                    Common.switchFragment(loginFragment,getActivity(),false);
                    return;
                }

                if (Common.networkConnected(getActivity())) {
                    String url = Common.URL + "/MemberServlet";
                    Member member = new Member(id, name, sex, phone,email,password );
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "memberUpdate");
                    jsonObject.addProperty("member", new Gson().toJson(member));
                    int count = 0;
                    try {
                        String result = new MyTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_UpdateFail);
                    } else {
                        Common.showToast(getActivity(), R.string.msg_UpdateSuccess);
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

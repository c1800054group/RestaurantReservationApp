package com.example.peggytsai.restaurantreservationapp.Member;

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
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Login.LoginFragment;
import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static android.content.Context.MODE_PRIVATE;

public class MemberManagerFragment extends Fragment {

    private static final String TAG = "MemberIndexFragment";
    private View view;
    private TextView tvAccount,tvName,tvSex,tvPhone,tvMemberLogOut;
    private MyTask memberTask;
    private int prefID;
    private Button btRevise;
    private Member member;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_index, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_MemberManager);

        findView();

        return view;
    }


    private void findView() {
        tvAccount = view.findViewById(R.id.tvAccount);
        tvName = view.findViewById(R.id.tvName);
        tvSex = view.findViewById(R.id.tvSex);
        tvPhone = view.findViewById(R.id.tvPhone);
        btRevise = view.findViewById(R.id.btRevise);
        tvMemberLogOut = view.findViewById(R.id.tvMemberLogOut);

        tvMemberLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                pref.edit().clear().apply();
                Fragment loginFragment = new LoginFragment();
                Common.switchFragment(loginFragment,getActivity(),false);
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);//後面是沒取得值的話預設給false
        if (login){
            prefID = pref.getInt("memberID",0);
        }else{
            Common.showToast(getActivity(),R.string.msg_noLogin);
            //尚未登入回到登入畫面
            Fragment loginFragment = new LoginFragment();
            Common.switchFragment(loginFragment,getActivity(),false);
        }

        if (Common.networkConnected(getActivity())){
            String url = Common.URL + "/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getMemberData");
            jsonObject.addProperty("memberId",prefID);
            memberTask = new MyTask(url, jsonObject.toString());
            try {
                String jsonIn = memberTask.execute().get();
                Gson gson = new Gson();
                member = gson.fromJson(jsonIn,Member.class);
                tvAccount.setText(member.getEmail());
                tvName.setText(member.getName());
                tvSex.setText(Common.sexString(member.getSex(),getActivity()));
                tvPhone.setText(member.getPhone());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }else{
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        btRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment reviseFragment = new ReviseFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("member",member);
                Common.switchFragmentBundle(reviseFragment,getActivity(),true,bundle);
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (memberTask != null) {
            memberTask.cancel(true);
        }
    }


}

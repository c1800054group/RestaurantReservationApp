package com.example.peggytsai.restaurantreservationapp.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentConfirmText;
import com.example.peggytsai.restaurantreservationapp.Cart.CartFragmentShow;
import com.example.peggytsai.restaurantreservationapp.Check.CheckFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckOrderFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckWaiterFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckWaiterTabFragment;
import com.example.peggytsai.restaurantreservationapp.Manager.FoodManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Member.MemberIndexFragment;
import com.example.peggytsai.restaurantreservationapp.Login.LoginFragment;

import com.example.peggytsai.restaurantreservationapp.Menu.demomenu.MenuFragment;
import com.example.peggytsai.restaurantreservationapp.Menu.modifymenu.MenuManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.WaiterTabFragment;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private static final int REQ_PERMISSIONS = 0;
    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.Navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initContent();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                //Customer menu item
                case R.id.item_Message:
                    fragment = new MessageFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_message);
                    return true;
                case R.id.item_Menu:
                    fragment = new MenuFragment();
//                    fragment = new MenuManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_menu);
                    return true;
                case R.id.item_Check:
                    fragment = new CheckOrderFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_check);
                    return true;
                case R.id.item_Rating:
                    fragment = new RatingFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_rating);
                    return true;
                case R.id.item_Member:
                    fragment = new MemberIndexFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_member);
                    return true;

                //manager menu
                case R.id.item_FoodManager:
                    fragment = new FoodManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_FoodManager);
                    return true;
                case R.id.item_MenuManager:
                    fragment = new MenuManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MenuManager);
                    return true;
                case R.id.item_MessageManager:
                    fragment = new MessageFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MessageManager);
                    return true;
                case R.id.item_RatingManager:
                    fragment = new RatingFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_RatingManager);
                    return true;
                case R.id.item_MemberManager:
                    fragment = new MemberIndexFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MemberManager);
                    return true;

                //waiter menu
                case R.id.item_CheckWaiter:
                    fragment = new CheckWaiterTabFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_CheckWaiter);
                    return true;
                case R.id.item_CheckManager:
                    fragment = new CheckManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_CheckManager);
                    return true;
                case R.id.item_ServiceWaiter:
                    fragment = new WaiterTabFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_ServiceWaiter);
                    return true;
                case R.id.item_MemberWaiter:
                    fragment = new MemberIndexFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_member);
                    return true;


                default:
                    initContent();
                    break;
            }
            return false;
        }

    };

    private void initContent() {
        Fragment fragment = new LoginFragment();
        navigation.setVisibility(BottomNavigationView.GONE);
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Content, fragment);
        fragmentTransaction.commit();
    }
    

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK   ) {
            Fragment current_Fragment = getSupportFragmentManager().findFragmentById(R.id.Content);
            if (current_Fragment instanceof CartFragmentConfirmText) {
                ((CartFragmentConfirmText) current_Fragment).KeyDown();
                return true;
            }

            if (current_Fragment instanceof CartFragmentShow) {
                ((CartFragmentShow) current_Fragment).KeyDown();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    private void askPermissions(){

        String[] permissions = {
                Manifest.permission.CAMERA
        };
        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this,permission);
            if (result != PackageManager.PERMISSION_GRANTED){
                permissionsRequest.add(permission);
            }
        }
        if(!permissionsRequest.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionsRequest.toArray(
                    new String[permissionsRequest.size()]), REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case REQ_PERMISSIONS:
                String text = "";
                for (int i = 0; i < grantResults.length; i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()){
                    text += getString(R.string.text_NotGranted);
                    Common.showToast(this,text);
                }
                break;
        }


    }
}

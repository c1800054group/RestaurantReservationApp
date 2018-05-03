package com.example.peggytsai.restaurantreservationapp.Main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;

import com.example.peggytsai.restaurantreservationapp.Check.CheckFragment;
import com.example.peggytsai.restaurantreservationapp.Member.MemberIndexFragment;
import com.example.peggytsai.restaurantreservationapp.Login.LoginFragment;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;


    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.Navigation);
//        navigation.seton
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initContent();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case  R.id.item_Member:
                    fragment = new MemberIndexFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_member);
                    return true;
                case R.id.item_Check:
                    fragment = new CheckFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_check);
                    return true;
                case R.id.item_Menu:
                    fragment = new MenuFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_menu);
                    return true;
                case R.id.item_Message:
                    fragment = new MessageFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_message);
                    return true;
                case R.id.item_Rating:
                    fragment = new RatingFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_rating);
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


}

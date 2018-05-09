package com.example.peggytsai.restaurantreservationapp.Main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.R;
public class Common {
    // Android官方模擬器連結本機web server可以直接使用 http://10.0.2.2
    public final static String URL = "http://10.0.2.2:8080/RestaurantReservationApp_Web";
    public final static String PREF_FILE = "preference";

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void switchFragment(Fragment fragment , FragmentActivity context, boolean backstack){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backstack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.Content,fragment);
        fragmentTransaction.commit();
    }
    public static void switchFragmentBundle(Fragment fragment , FragmentActivity context, boolean backstack, Bundle bundle){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backstack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.Content,fragment);
        fragmentTransaction.commit();
    }

    public static String sexString(String sexId, Activity activity){
        String sex;
        if (sexId.equals("1")){
            sex = activity.getResources().getString(R.string.text_rbMale);
        }else {
            sex = activity.getResources().getString(R.string.text_rbFemale);
        }
        return sex;
    }


}

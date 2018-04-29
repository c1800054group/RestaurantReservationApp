package com.example.peggytsai.restaurantreservationapp.Main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.R;

public class Common {
    public final static String URL = "http://10.0.2.2:8080/RestaurantReservationApp_Web";

    public static boolean networkConnected(Context context) {
        ConnectivityManager conManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();

    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
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


}
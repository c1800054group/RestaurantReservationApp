package com.example.peggytsai.restaurantreservationapp.Other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Order.OrderFragment;
import com.example.peggytsai.restaurantreservationapp.R;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrCodeFragment extends Fragment {
    private TextView tvTableNamer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrcode,container,false);
        findViews(view);

        /* 若在Activity內需要呼叫IntentIntegrator(Activity)建構式建立IntentIntegrator物件；
        * 而在Fragment內需要呼叫IntentIntegrator.forSupportFragment(Fragment)建立物件，
        * 掃瞄完畢時，Fragment.onActivityResult()才會被呼叫 */
        // IntentIntegrator integrator = new IntentIntegrator(this);
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QrCodeFragment.this);
        // Set to true to enable saving the barcode image and sending its path in the result Intent.
        integrator.setBarcodeImageEnabled(true);
        // Set to false to disable beep on scan.
        integrator.setBeepEnabled(false);
        // Use the specified camera ID.
        integrator.setCameraId(0);
        // By default, the orientation is locked. Set to false to not lock.
        integrator.setOrientationLocked(false);
        // Set a prompt to display on the capture screen.
        integrator.setPrompt("Scan a QR Code");
        // Initiates a scan
        integrator.initiateScan();


        return view;
    }

    private void findViews(View view) {
        tvTableNamer = view.findViewById(R.id.tvTableNumber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            tvTableNamer.setText(intentResult.getContents());
        } else {
//            tvTableNamer.setText("Result Not Found");
            Fragment orderFragment = new OrderFragment();
            Common.switchFragment(orderFragment,getActivity(),false);
        }
    }
}


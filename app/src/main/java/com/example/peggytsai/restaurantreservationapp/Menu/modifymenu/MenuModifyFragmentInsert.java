package com.example.peggytsai.restaurantreservationapp.Menu.modifymenu;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Menu.Menu;
import com.example.peggytsai.restaurantreservationapp.Menu.Socket;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingFragment;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingNewFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MenuModifyFragmentInsert extends Fragment implements View.OnClickListener {

    private EditText et_name;
    private EditText et_price;
    private EditText et_count;
    private Button button, bt_cancel;
    private TextView bt_insert;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private int selectRadio = 1;

    private TextView tt_toolbar;
    private TextView btMenuModifyInsert;
    MyTask MenuInertTask;

    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;
    private Uri croppedImageUri;

    private final static String TAG = "MenuInsert";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("菜單2");
        View view = inflater.inflate(R.layout.fragment_menu_modify_insert, container, false);

        findbutton(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String memberName = String.valueOf(  pref.getInt("memberID",0)    );

        Socket.connectServer(getActivity(),memberName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Socket.disconnectServer();
    }

    private void findbutton(View view) {
        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        bt_insert = view.findViewById(R.id.bt_insert);
        tt_toolbar.setText("新增菜單");
//        btMenuModifyInsert.setText("");
        bt_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Use_fragment useFragment = new Use_fragment();
//                useFragment.use_fragment(new TempFragment(), getFragmentManager());
            }
        });

        et_name = view.findViewById(R.id.et_name);
        et_price = view.findViewById(R.id.et_price);
        et_count = view.findViewById(R.id.et_count);
        button = view.findViewById(R.id.button);
        imageView = view.findViewById(R.id.image);
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_insert = view.findViewById(R.id.bt_insert);

        bt_cancel.setOnClickListener(this);
        bt_insert.setOnClickListener(this);
        button.setOnClickListener(this);

        radioGroup = view.findViewById(R.id.rg_select_insert);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // if rbByDate is checked, show date picker dialog
                if (checkedId == R.id.rb_select_insert_main) {
                    //加入主餐list
                    selectRadio = 1;
                } else if (checkedId == R.id.rb_select_insert_sub){
                    //加入副餐list
                    selectRadio = 2;
                } else {
                    //加入加購list
                    selectRadio = 3;
                }
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.button:
                //選取圖片
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        getActivity(), getActivity().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (Common.isIntentAvailable(getActivity(), intent)) {

                    int x = 0;
                    try {
                        startActivityForResult(intent, REQ_TAKE_PICTURE);
                    } catch (Exception e) {
                        Log.e("123", e.toString());
                    }

                } else {
                    Common.showToast(getActivity(), "NoCameraApp");
                }

                break;
            case R.id.bt_insert:

                if (et_name.getText().toString().trim().isEmpty()) {
                    et_name.setError("請輸入正確格式");
                    return;
                }

                if (et_price.getText().toString().trim().isEmpty()) {
                    et_price.setError("請輸入正確格式");
                    return;
                }

                if (et_count.getText().toString().trim().isEmpty()) {
                    et_count.setError("請輸入正確格式");
                    return;
                }


                if (image == null) {
                    Common.showToast(getActivity(), " no image ");
                    return;
                }

                int count = Integer.valueOf(   et_count.getText().toString().trim()  );

                Menu menu = new Menu(et_name.getText().toString().trim(),
                        et_price.getText().toString().trim(),
                        selectRadio,
                        count
                        );

//                        (et_name.getText().toString().trim(),
//                        et_price.getText().toString().trim(),
//                        selectRadio ,
//                        et_count.getText().toString().trim()
//                );


                //聯網 成功回傳Toast
                if (Common.networkConnected(getActivity())) {//檢查網路連線

                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("action", "menuInsert");
                    jsonObject.addProperty("menu", new Gson().toJson(menu));
                    jsonObject.addProperty("imageBase64", imageBase64);

                    MenuInertTask = new MyTask(Common.URL + "/MenuServlet", jsonObject.toString());
                    try {
                        MenuInertTask.execute().get();  //need use get() ending for send notifyDataSetChanged
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (Socket.SocketClient != null){
                        Socket.SocketClient.send("notifyDataSetChanged");

                        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        int num = pref.getInt("memberID",0);
                        Socket.connectServer(getActivity(),  String.valueOf(num)  );
                    }else {
                        Common.showToast(getActivity(),"disconnect");
                    }
                } else {
                    Common.showToast(getContext(), "text_NoNetwork");
                }
                //返回主取單
                Toast.makeText(getActivity(), "送出: " + selectRadio + " " + et_name.getText() + " " + et_price.getText(), Toast.LENGTH_SHORT).show();

            case R.id.bt_cancel:

                Fragment Fragment = new MenuManagerFragment();
                Common.switchFragment(Fragment, getActivity(), false);
                break;
        }

    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap picture = BitmapFactory.decodeStream(
                                getActivity().getContentResolver().openInputStream(croppedImageUri));
                        imageView.setImageBitmap(picture);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        image = out.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0); // this sets the max width
            cropIntent.putExtra("aspectY", 0); // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Common.showToast(getActivity(), "This device doesn't support the crop action!");
        }
    }


}

package com.example.peggytsai.restaurantreservationapp.Message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Member.Member;
import com.example.peggytsai.restaurantreservationapp.Member.MemberIndexFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MessageEditFragment extends Fragment {
    private final static String TAG = "MessageInsertFragment";
    private EditText etMessageEditTitle, etMessageEditPromotionMessage, etMessageEditDiscount;
    private TextView btMessageEditSave, tvMessageEditStartTime, tvMessageEditEndTime;
    private ImageView ivMessageEditImage;
    private Button btMessageEditPicture;
    private DatePickerDialog datePickerDialog;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private Uri contentUri, croppedImageUri;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private byte[] image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_manager_edit, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);
        tvtoolBarTitle.setText(R.string.text_MessageManager);

        findViews(view);
        return view;
    }

    private void findViews(View view) {

        etMessageEditTitle = view.findViewById(R.id.etMessageEditTitle);
        etMessageEditPromotionMessage = view.findViewById(R.id.etMessageEditPromotionMessage);
        etMessageEditDiscount = view.findViewById(R.id.etMessageEditDiscount);
        tvMessageEditStartTime = view.findViewById(R.id.tvMessageEditStartTime);
        tvMessageEditEndTime = view.findViewById(R.id.tvMessageEditEndTime);
        btMessageEditSave = view.findViewById(R.id.btMessageEditSave);
        ivMessageEditImage = view.findViewById(R.id.ivMessageEditImage);
        btMessageEditPicture = view.findViewById(R.id.btMessageEditPicture);

        Message message = (Message) getArguments().getSerializable("message");

        //時間轉換
        String couponStar= String.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(new Date(String.valueOf(message.getCoupon_start()))));
        String couponEnd= String.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(new Date(String.valueOf(message.getCoupon_end()))));

        etMessageEditTitle.setText(message.getMessage_title());
        etMessageEditPromotionMessage.setText(message.getMessage_content());
        etMessageEditDiscount.setText(String.valueOf(message.getCoupon_discount()));
        tvMessageEditStartTime.setText(couponStar);
        tvMessageEditEndTime.setText(couponEnd);

        tvMessageEditStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogListener(getActivity(), tvMessageEditStartTime);
                datePickerDialog.show();
            }
        });

        tvMessageEditEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogListener(getActivity(), tvMessageEditEndTime);
                datePickerDialog.show();
            }
        });

        btMessageEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                String messageEditTitle = etMessageEditTitle.getText().toString();
                String messageEditPromotionMessage = etMessageEditPromotionMessage.getText().toString();
                String messageEditDiscount = etMessageEditDiscount.getText().toString();
                String messageEditStartTime = tvMessageEditStartTime.getText().toString();
                String messageEditEndTime = tvMessageEditEndTime.getText().toString();
                Date startTime = null, endTime = null;

                try {
                    Date startDate = simpleDateFormat.parse(messageEditStartTime);
                    Date endDate = simpleDateFormat.parse(messageEditEndTime);
                    // end date should not be less than start date
                    if (startDate.after(endDate)) {
                        Common.showToast(getActivity(), R.string.msg_EndDateNotLessThanStartDate);
                        return;
                    }
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }

                if (messageEditTitle.length() <= 0 || messageEditPromotionMessage.length() <= 0
                        || messageEditDiscount.length() <= 0 || messageEditStartTime.length() <= 0
                        || messageEditEndTime.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_ColumnNull);
                    return;
                }

                if (image == null) {
                    Common.showToast(getActivity(), R.string.msg_NoImage);
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

                try {
                    startTime = sdf.parse(messageEditStartTime);
                    endTime = sdf.parse(messageEditEndTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (Common.networkConnected(getActivity())) {
                    String url = Common.URL + "/MessageServlet";
                    Message message = new Message(0, messageEditTitle, messageEditPromotionMessage
                            , 0, Float.valueOf(messageEditDiscount), startTime
                            , endTime);
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "messageUpdate");
                    jsonObject.addProperty("message", new GsonBuilder().setDateFormat("yyyy/MM/dd").create().toJson(message));
                    jsonObject.addProperty("imageBase64", imageBase64);
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
                    }

                } else {
                    Common.showToast(getActivity(), R.string.msg_NoNetwork);
                }
                /* 回前一個Fragment */
                Fragment messageFragment = new MessageFragment();
                Common.switchFragment(messageFragment,getActivity(),true);
            }
        });

        btMessageEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View alertView = getLayoutInflater().inflate(R.layout.dialog_camera_layout, null);
        ImageView ivDialogCamera = alertView.findViewById(R.id.ivDialogCamera);
        ImageView ivDialogSelectPicture = alertView.findViewById(R.id.ivDialogSelectPicture);

        alertDialogBuilder.setView(alertView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        ivDialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        getActivity(), getActivity().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                if (isIntentAvailable(getActivity(), intent)) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(getActivity(), R.string.text_NoCameraApp);
                }

            }
        });

        ivDialogSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);
            }
        });

    }

    private void datePickerDialogListener(Activity activity, final TextView textView) {
        datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = year + "/" + (month + 1) + "/" + dayOfMonth;
                textView.setText(text);
            }

        }, gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH)
                , gregorianCalendar.get(Calendar.DAY_OF_MONTH));

        DatePicker dp = datePickerDialog.getDatePicker();

        final Calendar calendar = Calendar.getInstance();
        //設定最早時間為今天
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        dp.setMinDate(calendar.getTimeInMillis());

        //設定最久時間為4週後
        calendar.add(Calendar.MONTH, 12);
        dp.setMaxDate(calendar.getTimeInMillis());
    }

    private boolean isIntentAvailable(Context context, Intent intent) {
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
                        ivMessageEditImage.setImageBitmap(picture);
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

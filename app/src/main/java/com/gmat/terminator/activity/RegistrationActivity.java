package com.gmat.terminator.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.model.AccountModel;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;
import com.gmat.terminator.utils.PermissionUtil;
import com.gmat.terminator.utils.SecureSharedPrefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

import static com.gmat.terminator.R.id.second_name;

/**
 * Created by Akanksha on 09-Dec-16.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    private EditText mFirstName, mLastName;
    private Button mProceedBtn;
    private SecureSharedPrefs prefs;
    private String mIsFirstLaunch;
    private ImageView mUserImg;
    private Realm mRealm;
    private String imagepath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        prefs = new SecureSharedPrefs(getApplicationContext());
        mIsFirstLaunch = prefs.getString(Constants.PREF_NAME_REGISTRATION, null);
        mRealm = Realm.getInstance(getApplicationContext());

        if (mIsFirstLaunch != null) {
            launchHomeScreen();
            finish();
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.PREF_NAME_REGISTRATION, "isFirstLaunch");
            editor.commit();
        }


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_registration);

        initializeViews();
    }

    private void launchHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initializeViews() {
        mFirstName = (EditText) findViewById(R.id.first_name);
        mFirstName.addTextChangedListener(this);
        mLastName = (EditText) findViewById(second_name);
        mLastName.addTextChangedListener(this);
        mProceedBtn = (Button) findViewById(R.id.proceed_btn);
        mProceedBtn.setOnClickListener(this);
        mUserImg = (ImageView) findViewById(R.id.img_user_pic);
        mUserImg.setOnClickListener(this);
    }

    private void handleProceedBtnVisibility() {
        if(!TextUtils.isEmpty(mFirstName.getText().toString()) &&
                !TextUtils.isEmpty(mLastName.getText().toString())) {
            mProceedBtn.setVisibility(View.VISIBLE);
        } else {
            mProceedBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed_btn :
                handleProceedBtnClick();
                break;
            case R.id.img_user_pic:
                checkPermission();
        }
    }

    private void handleProceedBtnClick() {
        storeInRealmDatabase();
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_FIRST_NAME, mFirstName.getText().toString());
        i.putExtra(Constants.INTENT_EXTRA_LAST_NAME, mLastName.getText().toString());
        startActivity(i);
    }

    private void storeInRealmDatabase() {
        mRealm.beginTransaction();
        AccountModel accountModel = mRealm.createObject(AccountModel.class);
        accountModel.setFirstName(mFirstName.getText().toString());
        accountModel.setLastName(mLastName.getText().toString());
        mRealm.commitTransaction();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        handleProceedBtnVisibility();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void checkPermission() {
        if (PermissionUtil.isVersionMarshmallowAndAbove() && !PermissionUtil.checkWriteExternalStoragePermission(this)) {
            PermissionUtil.requestWriteExternalPermission(this);
        } else {
            showDialogPickOption();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                showDialogPickOption();
            } else {
                int rationalState = PermissionUtil.shouldShowRequestPermissionRationaleState(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
                if (rationalState == PermissionUtil.REQUEST_PERMISSION_SHOW_RATIONALE) {
                    //Toast.makeText(this, getResources().getString(R.string.read_contacts_rational_message), Toast.LENGTH_SHORT).show();
                } else {
                    showWriteExternalStorageAlertDialog();
                }
            }
        }
        if (requestCode == PermissionUtil.REQUEST_CODE_CAMERA) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                takeMorePhotos();
            }
        } else {
            int rationalState = PermissionUtil.shouldShowRequestPermissionRationaleState(permissions, grantResults, Manifest.permission.CAMERA, this);

            if (rationalState == PermissionUtil.REQUEST_PERMISSION_SHOW_RATIONALE) {
                // finish();
            } else if (rationalState == PermissionUtil.REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE) {
                try {
                    showAlertDialog();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private void checkCameraPermission() {

        if (PermissionUtil.isVersionMarshmallowAndAbove() && !PermissionUtil.checkCameraPermission(this)) {
            PermissionUtil.requestCameraPermission(this);
        } else {
            takeMorePhotos();
        }
    }

    public void showDialogPickOption() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.select_from));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        selectImageFromGallery();
                    }
                })
                .setNegativeButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkCameraPermission();
                    }
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void selectImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            this.startActivityForResult(i, Constants.REQUEST_CODE_PIC_UPLOAD);
        } catch (ActivityNotFoundException exe) {
            showAccessDenied();
        }
    }

    public void takeMorePhotos() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.isVersionMarshmallowAndAbove() && this != null && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PermissionUtil.REQUEST_CODE_CAMERA);
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.isVersionMarshmallowAndAbove() && this != null && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PermissionUtil.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().toString() + Constants.USER_PROFILE_PIC_PARENT_PATH;
                File folder = new File(path);
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    File f = new File(path, "picture.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    this.startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA_UPLOAD);
                }
            }
        } catch (ActivityNotFoundException exe) {
            showAccessDenied();
        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.camera_permission_title))
                .setMessage(getResources().getString(R.string.camera_permission))
                .setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.action_settings),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        String packageName = AppUtility.getPackageName(RegistrationActivity.this);
                        if (!TextUtils.isEmpty(packageName))
                            intent.setData(Uri.parse("package:" + packageName));
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();

    }

    private void showWriteExternalStorageAlertDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle(getResources().getString(R.string.send_money));
            builder.setMessage(getResources().getString(R.string.write_to_sdcard_permission_alert_msg));
            builder.setPositiveButton(getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PermissionUtil.openAppSettingPage(RegistrationActivity.this);
                    RegistrationActivity.this.finish();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do nothing
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAccessDenied() {
        showAlert("", getString(R.string.camera_permission_title));
    }

    /**
     * Show alert dialog
     *
     * @param title
     * @param message
     */
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_sucess_dialog, null);
        TextView dialogTitle = (TextView) view
                .findViewById(R.id.dialog_title);
        TextView successMsg = (TextView) view
                .findViewById(R.id.sucess_msg);
        TextView successNumber = (TextView) view
                .findViewById(R.id.sucess_number);
        View seperator = view.findViewById(R.id.seperator);
        if (title != null && title.trim().length() > 0) {
            dialogTitle.setText(title);
        } else {
            dialogTitle.setVisibility(View.GONE);
            seperator.setVisibility(View.GONE);
        }
        successNumber.setVisibility(View.GONE);

        successMsg.setText(message);
        final Button positiveBtn = (Button) view.findViewById(R.id.btn_ok);
        positiveBtn.setText(R.string.ok);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(false);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CAMERA_UPLOAD) {
            if (resultCode == Activity.RESULT_OK) {
                if (android.os.Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                    String path = android.os.Environment.getExternalStorageDirectory().toString() + Constants.USER_PROFILE_PIC_PARENT_PATH;
                    File folder = new File(path);
                    if (folder.exists()) {

                        File f = new File(path);
                        for (File temp : f.listFiles()) {
                            if (temp.getName().equalsIgnoreCase("picture.jpg")) {
                                f = temp;
                                break;
                            }
                        }
                        try {
                            Uri selectedImage = Uri.fromFile(new File(f.getAbsolutePath()));
                                String resp= compressImage(f.getAbsolutePath());
                                if(resp!=null){
                                    File imageFile = new File(resp);
                                    updateProfileImage(imageFile, selectedImage);
                                } else
                                    showAlert("", getString(R.string.title_400));
                                return;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_PIC_UPLOAD) {
            if (resultCode == RESULT_OK) {
                if(data.getData()!=null){
                    Uri selectedImage = data.getData();
                    if (selectedImage!=null) {
                        if(getPath(selectedImage)!=null){
                            imagepath = getPath(selectedImage);
                            String resp= compressImage(imagepath);
                            if(resp!=null){
                                File imageFile = new File(resp);
                                updateProfileImage(imageFile,selectedImage);
                            }
                            else
                                showAlert("",getString(R.string.title_400));
                            return;
                        }else
                            showAlert("",getString(R.string.title_400));
                        return;
                    }
                    else
                        showAlert("",getString(R.string.title_400));
                    return;
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return null;
        }
    }

    public void updateProfileImage(File picUri, Uri selectedImage) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            //mImgCorner.setVisibility(View.VISIBLE);
            mUserImg.setImageBitmap(bitmap);
            //uploadImage(picUri);
        }
        catch(ActivityNotFoundException anfe){
            anfe.printStackTrace();
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String compressImage(String imageUri) {
        if (imageUri != null) {
            String filePath = imageUri;
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();

        /*by setting this field as true, the actual bitmap pixels are not loaded in the
        memory.Just the bounds are loaded.If
        you try the use the bitmap here, you will get null.*/
            options.inJustDecodeBounds = true;


            File image = new File(filePath);
            Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath(), options);


            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            //max Height and width values of the compressed image is taken as 816 x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            if (actualHeight <= 0)
                actualHeight = 1;
            if (actualWidth <= 0)
                actualWidth = 1;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            //width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(image.getAbsolutePath(), options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;


            if (bmp != null) {
                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                Canvas canvas = new Canvas(scaledBitmap);
                canvas.setMatrix(scaleMatrix);

                if ((bmp.getWidth() / 2) == 0 && (bmp.getHeight() / 2 == 0)) {
                    canvas.drawBitmap(bmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
                } else if ((bmp.getWidth() / 2) == 0) {
                    canvas.drawBitmap(bmp, 0, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
                } else if ((bmp.getHeight() / 2 == 0)) {
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
                } else {
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
                }


//      check the rotation of the image and display it properly
                ExifInterface exif;
                try {
                    exif = new ExifInterface(filePath);

                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, 0);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                            scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                            true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileOutputStream out = null;
                String filename = imageUri;
                try {
                    out = new FileOutputStream(imageUri);

//          write the compressed bitmap at the destination specified by filename.
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return filename;
            } else {

                return imageUri;
            }
        } else {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}

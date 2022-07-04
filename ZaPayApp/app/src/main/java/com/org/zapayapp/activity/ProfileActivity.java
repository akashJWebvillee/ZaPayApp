package com.org.zapayapp.activity;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.ChangePassDialogActivity;
import com.org.zapayapp.dialogs.EditProfileDialogActivity;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.ImagePathUtil;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WFileUtils;
import com.org.zapayapp.utils.WRuntimePermissions;
import com.org.zapayapp.webservices.APICalling;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView editProfileTV, changePasswordTV,setPinTV, profileTxtName, profileTxtEmail, profileTxtMobile, profileTxtAddress;
    private Intent intent;
    private ImageView profileImageView;
    private WRuntimePermissions runtimePermissions;
    private String path = "";
    private static final int GALLERY_REQUEST_OLD = 1890;
    private static final int GALLERY_REQUEST = 200;
    private boolean isDataUpdate=true;
    private Uri fileUri;
    private boolean isCallApi= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(MY_PROFILE);
        if (isDataUpdate && isCallApi){
            callAPIGetUserDetail();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDataUpdate=true;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    private void init() {
        runtimePermissions = new WRuntimePermissions(this);
        changePasswordTV = findViewById(R.id.changePasswordTV);
        setPinTV = findViewById(R.id.setPinTV);
        editProfileTV = findViewById(R.id.editProfileTV);
        profileTxtName = findViewById(R.id.profileTxtName);
        profileTxtEmail = findViewById(R.id.profileTxtEmail);
        profileTxtMobile = findViewById(R.id.profileTxtMobile);
        profileTxtAddress = findViewById(R.id.profileTxtAddress);
        profileImageView = findViewById(R.id.profileImageView);
    }

    private void initAction() {
        changePasswordTV.setOnClickListener(this);
        setPinTV.setOnClickListener(this);
        editProfileTV.setOnClickListener(this);
        profileImageView.setOnClickListener(this);
        setDataOnScreen();
    }

    private void setDataOnScreen() {
        if (SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString().length() > 0) {
            Glide.with(ProfileActivity.this)
                    .load(APICalling.getImageUrl(SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString()))
                    .placeholder(R.mipmap.default_profile)
                    .into(profileImageView);
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 0) {
            String name = SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME, "") + " " + SharedPref.getPrefsHelper().getPref(Const.Var.LAST_NAME, "");
            profileTxtName.setText(name);
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.EMAIL) != null && SharedPref.getPrefsHelper().getPref(Const.Var.EMAIL).toString().length() > 0) {
            profileTxtEmail.setText(SharedPref.getPrefsHelper().getPref(Const.Var.EMAIL, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE).toString().length() > 0) {
            profileTxtMobile.setText(SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1).toString().length() > 0) {
            profileTxtAddress.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1, ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePasswordTV:
                intent = new Intent(ProfileActivity.this, ChangePassDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.setPinTV:
                intent = new Intent(ProfileActivity.this, SetPinActivity.class);
                intent.putExtra("forWhat",getString(R.string.update_pin));
                startActivity(intent);
                break;

            case R.id.editProfileTV:
                intent = new Intent(ProfileActivity.this, EditProfileDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.profileImageView:
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (runtimePermissions.checkPermissionForStorage()) {
//                        openGallery();
//                    } else {
//                        runtimePermissions.requestPermissionForStorage();
//                    }
//                } else {
//                    openGallery();
//                }
                isCallApi = false;
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage(R.string.if_you_reject_permissions)
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
                break;
        }
    }


    /**
     * permission listener for CAMERA
     */
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            selectImage();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            CommonMethods.showToast(ProfileActivity.this,"Permission Denied\n" + deniedPermissions.toString());
            isCallApi = true;
        }

    };

    /**
     * choose image dialog
     */
    private void selectImage() {
        try {
            final CharSequence[] options = {getString(R.string.pick_img), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setTitle(R.string.select_option);
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals(getString(R.string.pick_img))) {
                    dialog.dismiss();
                    captureImage();
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    dialog.dismiss();
                    openGallery();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {

        long l = System.currentTimeMillis();
        String time = String.valueOf(l);
        File photo = new File(WFileUtils.DIR_IMAGE, "image_" + time + ".jpg");
        fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activityResultLauncherImageCamera.launch(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_sdcard), Toast.LENGTH_LONG).show();
        }

    }

    ActivityResultLauncher<Intent> activityResultLauncherImageCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if (fileUri != null) {
                                selectUri(fileUri);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

    private void openGallery() {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, GALLERY_REQUEST);
            } else {
                if (WFileUtils.isSdPresent()) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_REQUEST_OLD);
                } else {
                    String no_sdcard = "No sd card present";
                    CommonMethods.showToast(this, no_sdcard);
                    isCallApi = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * request permission result to show if runtime permission is accepted
     *
     * @param requestCode  request code to identify method
     * @param permissions  permission requested
     * @param grantResults if permission is given
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRuntimePermissions.REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (runtimePermissions.requestPermissionForStorageDenied()) {
                        runtimePermissions.showAlertDialog(WRuntimePermissions.STORAGE_MSG, WRuntimePermissions.REQUEST_CODE_STORAGE, this);
                    } else {
                        runtimePermissions.openSettingsDialog(WRuntimePermissions.STORAGE_MSG_DESC, this);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GALLERY_REQUEST_OLD && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        try {
                            String path = picturePath;
                            if (path != null){
                               // Glide.with(ProfileActivity.this).load(path).placeholder(R.mipmap.ic_user).into(profileImageView);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            isDataUpdate=false;
            isCallApi = true;
            if (data != null) {
                try {
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        selectUri(selectedUri);
                    } else {
                        //CommonMethods.showLogs(TAG, " get file uri is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectUri(Uri selectedUri) {
        path = selectedUri.getPath();
        try {
            if (path != null) {
                //path = WFileUtils.getFilePath(this, selectedUri);
                Glide.with(ProfileActivity.this).load(selectedUri).placeholder(R.mipmap.ic_user).into(profileImageView);
                path = ImagePathUtil.getPath(ProfileActivity.this, selectedUri);
                path = ImagePathUtil.compressImage(path);
                callAPIUploadFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUploadFile() {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenMultiPartApi(SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString(), getString(R.string.api_update_profile_image), fileToUpload);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_profile_image), profileImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetUserDetail() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApiToken(token, getString(R.string.api_get_user_details));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_user_details), profileImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (from.equals(getResources().getString(R.string.api_update_profile_image))) {
                if (status == 200) {
                    path = "";
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                        if (jsonObject.get("profile_image").getAsString() != null) {
                            String profile_image = jsonObject.get("profile_image").getAsString();
                            SharedPref.getPrefsHelper().savePref(Const.Var.PROFILE_IMAGE, profile_image);
                        }
                        isCallApi = true;
                    }
                    showSimpleAlert(msg, "");
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_user_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                        MySession.MakeSession(jsonObject);
                        setDataOnScreen();
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }
}

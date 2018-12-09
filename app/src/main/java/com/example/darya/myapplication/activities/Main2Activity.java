package com.example.darya.myapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.example.darya.myapplication.BuildConfig;
import com.example.darya.myapplication.R;
import com.example.darya.myapplication.data.filemanager.FilePhotoManager;
import com.example.darya.myapplication.interfaces.PhoneimeiInfoFragment;
import com.example.darya.myapplication.data.models.User;
import com.example.darya.myapplication.interfaces.UserEditFragment;
import com.example.darya.myapplication.interfaces.UserInfoFragment;
import com.example.darya.myapplication.data.db.UserSQLiteRepository;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class Main2Activity extends AppCompatActivity
implements UserEditFragment, UserInfoFragment, PhoneimeiInfoFragment {

    final int OPEN_CAMERA_REQUEST = 1;
    final int OPEN_GALLERY_REQUEST = 2;

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;

    private NavController navController;
    private UserSQLiteRepository userSQLiteRepository;
    private FilePhotoManager filePhotoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userSQLiteRepository = new UserSQLiteRepository(this);
        filePhotoManager = new FilePhotoManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);//отобразить xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment); // получаем граф навигации(элементы выбора)
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);// связываем навигацию с меню
        NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.drawer_layout));


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void ButtonEditClick() {
        navController.navigate(R.id.editUserInfo);
    }

    @Override
    public void ClickButtonUdpate(User user) {//save edited information about user
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        Bitmap avatar = ((BitmapDrawable)viewForAvatar.getDrawable()).getBitmap();
        userSQLiteRepository.savedUser(user);
        filePhotoManager.updateAvatar(avatar);
        navController.popBackStack();

    }

    @Override
    public void ClickButtonBack() {
        navController.popBackStack();
    }

    @Override
    public void loadUserAvatar(ImageView view) {

        Uri uriForAvatar = filePhotoManager.getUriForUserAvatar();
        if (uriForAvatar != null) {
            view.setImageURI(uriForAvatar);
        }
    }
    public void loadNewAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    public void ClickButtonOpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePhotoManager.generateUriForSave().getPath());
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }

    @Override
    public User getUser() {
        return userSQLiteRepository.getUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        Bitmap avatar;
        switch (requestCode) {
            case OPEN_CAMERA_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    if (viewForAvatar != null) {
                        avatar = (Bitmap) data.getExtras().get("data");
                        viewForAvatar.setImageBitmap(avatar);
                        viewForAvatar.setRotation(90);
                    }
                }
                break;
            case OPEN_GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        avatar = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        viewForAvatar.setImageBitmap(avatar);
                        viewForAvatar.setRotation(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void updateAvatarImageView(){
        final ImageView avatarImageView = findViewById(R.id.avatar_image_view);
        Uri uriForAvatar =  filePhotoManager.getUriForUserAvatar();
        if (uriForAvatar != null){
            avatarImageView.setImageURI(uriForAvatar);
        }
    }

    @Override
    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }


    @Override
    public String getImei() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        }
        return null;
    }

    @Override
    public void requestPermissionForImei() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
    }

    @Override
    public String getDescriptionPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                return getResources().getString(R.string.permission_description);
            }
            return getResources().getString(R.string.never_permission_description);
        }
    }

    @Override
    public void updatePhoto(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Важное сообщение!")
//                .setMessage("Покормите кота!")
//                .setCancelable(false)
//                .setPositiveButton("Camera", (dialogInterface, i) -> {
//                   ClickButtonOpenCamera();
//                })
//                .setNeutralButton("Gallery", (dialogInterface, i) -> {
//                    loadNewAvatar();
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        final String[] mCatsName ={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setItems(mCatsName, (dialog, item) -> {
            switch (item){
                case 0:
                    ClickButtonOpenCamera();
                    break;
                case 1:
                    loadNewAvatar();
                    break;
            }
        });

        AlertDialog alert = builder.create();

        alert.show();
    }
}
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darya.myapplication.BuildConfig;
import com.example.darya.myapplication.R;
import com.example.darya.myapplication.data.db.UserSQLiteRepository;
import com.example.darya.myapplication.data.filemanager.FilePhotoManager;
import com.example.darya.myapplication.data.models.User;
import com.example.darya.myapplication.fragments.FragmentUser;
import com.example.darya.myapplication.interfaces.PhoneimeiInfoFragment;
import com.example.darya.myapplication.interfaces.UserEditFragment;
import com.example.darya.myapplication.interfaces.UserInfoFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class Main2Activity extends AccountManagerActivity
implements UserEditFragment, UserInfoFragment, PhoneimeiInfoFragment {

    final int OPEN_CAMERA_REQUEST = 1;
    final int OPEN_GALLERY_REQUEST = 2;

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;
    private int currentUserId;

    private NavController navController;
    private FilePhotoManager filePhotoManager;

    private boolean currentPageIsEditing = false;

    private View headerView;

    private interface Delegate{
        void invoke();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userId = accountManager.getIdAuthorizedUser();
        if (userId != null) {
            currentUserId = Integer.valueOf(userId);
        }
        else {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }

        filePhotoManager = new FilePhotoManager(this);

        setContentView(R.layout.activity_main2);//отобразить xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment); // получаем граф навигации(элементы выбора)
        NavigationUI.setupWithNavController(navView, navController);// связываем навигацию с меню
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(this::onToolbarNavigationClickListener);

        User user = userSQLiteRepository.getUserById(currentUserId);
        headerView = navView.getHeaderView(0);
        headerView.findViewById(R.id.fragmentUser).setOnClickListener(v -> {
            navController.navigate(R.id.editUserInfo);
            drawer.closeDrawer(GravityCompat.START);
        });
        findViewById(R.id.imei_button).setOnClickListener(v ->
                navController.navigate(R.id.aboutFragment));

        TextView nameTextView = headerView.findViewById(R.id.user_email_menu);
        nameTextView.setText(user.getFirstName());

        TextView emailTextView = headerView.findViewById(R.id.textView);
        emailTextView.setText(user.getEmail());

        findViewById(R.id.logout_button).setOnClickListener(
                v -> logOut());

        loadUserAvatar(headerView.findViewById(R.id.fragmentUser));
    }

    private void onToolbarNavigationClickListener(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof NavHostFragment) {
                List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
                if (childFragments.get(0) instanceof FragmentUser) {
                    toggleNavigationDrawer(drawer);
                } else {
                    if (currentPageIsEditing) {
                        checkChangesUserInfo(navController::popBackStack);
                    } else {
                        Navigation.findNavController(Main2Activity.this, R.id.nav_host_fragment).popBackStack();
                    }
                }
            }
        }
    }

    private void logOut(){
        final String positive = getResources().getString(R.string.positive_logout);
        final String negative = getResources().getString(R.string.negative_logout);
        final String title = getResources().getString(R.string.logout_description_for_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(positive, (dialog, which) -> {
                    Intent intent = new Intent(this, AuthenticationActivity.class);
                    intent.putExtra(IS_LOGOUT_KEY, currentUserId);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(negative, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void toggleNavigationDrawer(DrawerLayout drawer) {
        int drawerLockMode = drawer.getDrawerLockMode(GravityCompat.START);
        if (drawer.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed(){
        if (currentPageIsEditing){
            checkChangesUserInfo(super::onBackPressed);
        }
        else {
            super.onBackPressed();
        }
    }

    private void checkChangesUserInfo(Delegate goTo){
        TextView firstNameEditText = findViewById(R.id.first_name_edit);
        TextView lastNameEditText = findViewById(R.id.last_name_edit);
        TextView phoneEditText = findViewById(R.id.phone_edit);
        TextView emailEditText = findViewById(R.id.email_edit);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        User user = userSQLiteRepository.getUserById(currentUserId);

        if (!user.getFirstName().equals(firstName) || !user.getLastName().equals(lastName) ||
                !user.getPhone().equals(phone) || !user.getEmail().equals(email)){

            final String positive = getResources().getString(R.string.positive_logout);
            final String negative = getResources().getString(R.string.negative_logout);
            final String title = "Save changes??";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title)
                    .setPositiveButton(positive, (dialog, which) -> {
                        ClickButtonUdpate(new User(firstName, lastName, email, phone));
                        goTo.invoke();
                    })
                    .setNegativeButton(negative, (dialog, which) ->  goTo.invoke());
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            goTo.invoke();
        }
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

        TextView nameTextView = headerView.findViewById(R.id.user_email_menu);
        nameTextView.setText(user.getFirstName());

        TextView emailTextView = headerView.findViewById(R.id.textView);
        emailTextView.setText(user.getEmail());

        loadUserAvatar(headerView.findViewById(R.id.fragmentUser), avatar);
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

    public void loadUserAvatar(ImageView view, Bitmap bitmap) {
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
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
        return userSQLiteRepository.getUserById(currentUserId);
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

    @Override
    public void setCurrenPage(boolean state) {
        currentPageIsEditing = state;
    }
}
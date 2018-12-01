package com.example.darya.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;//id запроса
    private final String preferenceKey = "IMEI";
    private final String preferenceName = "PHONE_SETTINGS";

    private Button buttonPermissionDescription;
    private TextView imeiView;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//размещаем наши вьюшки на форме, которые нах. в xml

        TextView versionView = findViewById(R.id.version);
        buttonPermissionDescription = findViewById(R.id.update);
        imeiView = findViewById(R.id.imei);

        settings = getSharedPreferences(preferenceName, MODE_PRIVATE);// открываем Preferences MODE private значит, что после сохранения, данные будут видны только этому приложению

        if(getResources().getBoolean(R.bool.is_portrait)) {// если true устанавливаем в портретную ориентацию
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        versionView.setText(BuildConfig.VERSION_NAME);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (settings.contains(preferenceKey)){
                imeiView.setText(settings.getString(preferenceKey, getResources().getString(R.string.default_for_imei)));
            } // если imei есть, то мы его выводим, а если нет, то мы кидаем запрос на разрешение
            else {
                requestPermissions(Manifest.permission.READ_PHONE_STATE);
            }
        } else {
            imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));// если есть разрешение, то мы выводим имэй
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// если пользователь не дал доступ
                    imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
                    disablePermissionDescription();
                }
                else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){//не разрешл
                        enablePermissionsDescription(getResources().getString(R.string.permission_description),
                                Manifest.permission.READ_PHONE_STATE);
                    }
                    else {
                        enablePermissionsDescription(getResources().getString(R.string.never_permission_description),//и сказал никогда больше не спрашивать
                                Manifest.permission.READ_PHONE_STATE);
                    }
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void requestPermissions(String... permissions) {//всплывающие окна
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) { // если ранее был запрос

            final Snackbar descriptionBar = Snackbar.make(findViewById(R.id.root),// всплывающие сообщения
                    getResources().getString(R.string.permission_description),
                    Snackbar.LENGTH_INDEFINITE);

            descriptionBar.setAction(getResources().getString(R.string.ok),
                    view -> ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_READ_PHONE_STATE))
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
        }
    }


    private String getImei(String defaultString) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = manager.getDeviceId();
            if (!settings.contains(preferenceKey)) {// если мы дали доступ к получению имэи,то мы его записываем его по ключу preferencekey
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(preferenceKey, imei);
                editor.apply();
            }
            return imei;

        }
        return  defaultString;
    }



    private void enablePermissionsDescription(String description, String permission) {// кнопка для получения доступа
        final Snackbar bar = Snackbar.make(findViewById(R.id.root), description, Snackbar.LENGTH_INDEFINITE);

        buttonPermissionDescription.setText(getResources().getString(R.string.name_for_update_button));
        buttonPermissionDescription.setVisibility(View.VISIBLE);

        buttonPermissionDescription.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
                disablePermissionDescription();
            }
            else {
                bar.setAction(getResources().getString(R.string.ok),
                        v1 -> ActivityCompat.requestPermissions(this, new String[]{permission},
                                REQUEST_CODE_PERMISSION_READ_PHONE_STATE)).show();
            }
        });
    }

    private void disablePermissionDescription() {
        buttonPermissionDescription.setVisibility(View.INVISIBLE);
    }
}

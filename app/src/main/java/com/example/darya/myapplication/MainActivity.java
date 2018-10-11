package com.example.darya.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.example.darya.myapplication.BuildConfig;
import com.example.darya.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView versionView = (TextView)findViewById(R.id.version);
        String version = BuildConfig.VERSION_NAME;
        versionView.setText(version);

        ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                TextView imeiView = (TextView) findViewById(R.id.imei);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    imeiView.setText(manager.getDeviceId());
                }
                else {
                    imeiView.setText("Sorry");
                }



            }
        }
    }
}

package com.example.darya.myapplication.interfaces;

public interface PhoneimeiInfoFragment {
    String getVersion();
    String getImei();
    void requestPermissionForImei();
    String getDescriptionPermission();
}

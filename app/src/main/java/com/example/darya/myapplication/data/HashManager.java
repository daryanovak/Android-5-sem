package com.example.darya.myapplication.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashManager {

    public String getHash(final String text){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD4");
            messageDigest.update(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b: messageDigest.digest()){
            String item = String.format("%02X", b);
            sb.append(item);
        }
        return sb.toString();
    }
}

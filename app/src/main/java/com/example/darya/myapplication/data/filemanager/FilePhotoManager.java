package com.example.darya.myapplication.data.filemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilePhotoManager {
    private final String AVATAR_NAME = "avatar.jpg";
    private Context context;

    public FilePhotoManager(Context context){
        this.context = context;
    }

    public void updateAvatar(Bitmap avatar){
        File newAvatar = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        try {
            FileOutputStream out = new FileOutputStream(newAvatar);
            avatar.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Uri getUriForUserAvatar(){//получение адреса для картинки по пути
        File file = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        return file.exists() ? Uri.fromFile(file) : null;
    }

    public Uri generateUriForSave(){// генерация пути
        File file = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        return Uri.fromFile(file);
    }
}

package com.example.darya.myapplication.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {
    private Context context;

    public CheckInternetAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();


        if (isConnected) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                if (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0)
                    return true;

            } catch (IOException e) {
                Log.e("TAG", "Error checking internet connection", e);

                return false;
            }
        } else {
            Log.d("TAG", "No network available!");
            return false;
        }


        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d("TAG", "result" + result);

        Toast toast;
        if(result){
            toast = Toast.makeText(context, "online", Toast.LENGTH_LONG);
        }
        else {
            toast = Toast.makeText(context, "offline", Toast.LENGTH_LONG);
        }
        toast.show();
    }

}

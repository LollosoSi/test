package roccaccino.splashofflesh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LollosoSiTv on 12/12/2016.
 */
public class utilities {


    String LogTag = "utilities.class";
    Context questo;

    public void Init(Context ctx){

        questo = ctx;

    }

    public void NoInternetDialog(){

        new AlertDialog.Builder(questo)
                .setTitle(questo.getString(R.string.wts))
                .setMessage(questo.getString(R.string.ynct))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        questo.startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                    }
                })
                .setCancelable(false).show();

    }

    public boolean isNetworkAvailable() {

        final SharedPreferences sh = questo.getSharedPreferences("temp",Context.MODE_PRIVATE);

        sh.edit().putBoolean("action",false).commit();

        Handler handler = new Handler();


        final Runnable r = new Runnable() {
            public void run() {

                if (isNetworkAvailable()) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection)
                                (new URL("http://clients3.google.com/generate_204")
                                        .openConnection());
                        urlc.setRequestProperty("User-Agent", "Android");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();


                        sh.edit().putBoolean("action",(urlc.getResponseCode() == 204 && urlc.getContentLength() == 0)).commit();
                    } catch (IOException e) {
                        Log.e(LogTag, "Error checking internet connection", e);
                    }
                } else {
                    Log.d(LogTag, "No network available!");
                }
                sh.edit().putBoolean("action",false).commit();

            }
        };
        handler.post(r);
        boolean tmp = sh.getBoolean("action",false);
        sh.edit().clear();
        return tmp;

    }
}

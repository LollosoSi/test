package roccaccino.splashofflesh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by LollosoSiTv on 12/12/2016.
 */
public class utilities {

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
        ConnectivityManager connectivityManager
                = (ConnectivityManager) questo.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

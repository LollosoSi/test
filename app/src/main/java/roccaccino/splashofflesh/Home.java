package roccaccino.splashofflesh;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class Home extends Activity {

     user utente =new user();
     utilities utils = new utilities();

    String LogTag = "Home.class";

    Long Food= Long.valueOf(0);
    Long Gold= Long.valueOf(0);
    Long Exp= Long.valueOf(0);
    Long Level= Long.valueOf(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        utils.Init(Home.this);

        if(utils.isNetworkAvailable()){
        utente.Init(Home.this);
        }
    }

}

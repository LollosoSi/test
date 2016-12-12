package roccaccino.splashofflesh;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        if(utils.isInternetAvailable()){
        utente.Init(Home.this);

            Food=utente.GetElement("Food");
            Gold=utente.GetElement("Gold");
            Level=utente.GetElement("Level");
            Exp=utente.GetElement("Exp");

            ((TextView)findViewById(R.id.FoodTextView)).setText(Food.toString());
            ((TextView)findViewById(R.id.GoldTextView)).setText(Gold.toString());
            ((TextView)findViewById(R.id.LevelTextView)).setText(Level.toString());
        }else{utils.NoInternetDialog();}
    }

}

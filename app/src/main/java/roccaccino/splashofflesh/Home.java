package roccaccino.splashofflesh;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Home extends Activity {

     user utente =new user();

    String LogTag = "Home.class";

    Long Food= Long.valueOf(0);
    Long Gold= Long.valueOf(0);
    Long Exp= Long.valueOf(0);
    Long Level= Long.valueOf(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        utente.Init(Home.this);
    }
}

package roccaccino.splashofflesh;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;


public class user {

    public String nome;
    public String regione;
    public long x;
    public long y;

    boolean hasSetInstance = false;

    Context questo;

    String LogTag = "user.class";

    private DatabaseReference mDatabase;

    void Init(Context ths) {

        SetInstance();

        questo = ths;

        final SharedPreferences sh = ths.getSharedPreferences("userdata", ths.MODE_PRIVATE);

        nome = sh.getString("name", "fac");
        Log.i(LogTag, "Setting user " + nome);
        regione = sh.getString("region", "Vizzyland");
        Log.i(LogTag, "Setting region " + regione);

        x = sh.getLong("x", 0);
        y = sh.getLong("y", 0);
        Log.i(LogTag, "Setting coordinates: x " + x + " y " + y);

        mDatabase.child("users").child("region").child(regione).child("users").child(nome).child("Properties").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap a = ((HashMap) dataSnapshot.getValue());


                sh.edit().putLong("Food", (Long) a.get("Food")).commit();
                sh.edit().putLong("Gold", (Long) a.get("Gold")).commit();
                sh.edit().putLong("Exp", (Long) a.get("Exp")).commit();
                sh.edit().putLong("Level", (Long) a.get("Level")).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void SetInstance() {
        if (!hasSetInstance) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            hasSetInstance = true;
            Log.e(LogTag, "USER.class UPDATED ITS mDatabase");
        }
    }


    void UpdateProperties(Long Food, Long Gold,Long Level, Long Exp) {

        SetInstance();

        SharedPreferences sh = questo.getSharedPreferences("userdata", questo.MODE_PRIVATE);


        HashMap a = new HashMap();
        a.put("Gold", Gold);
        a.put("Food", Food);
        a.put("Exp",Exp);
        a.put("Level",Level);



        sh.edit().putLong("Food", (Long) a.get("Food")).commit();
        sh.edit().putLong("Gold", (Long) a.get("Gold")).commit();
        sh.edit().putLong("Exp", (Long) a.get("Exp")).commit();
        sh.edit().putLong("Level", (Long) a.get("Level")).commit();

        mDatabase.child("users").child("region").child(regione).child("users").child(nome).child("Properties").setValue(a);

        Log.i(LogTag, "Updated Properties");

    }

    Long GetElement(final String Element) {


        final SharedPreferences sh=questo.getSharedPreferences("userdata",questo.MODE_PRIVATE);
        Long exValue = sh.getLong(Element,0);
        sh.edit().putLong(Element,-1).commit();
        mDatabase.child("users").child("region").child(regione).child("users").child(nome).child("Properties").child(Element).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sh.edit().putLong(Element, (Long) dataSnapshot.getValue()).commit();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (sh.getLong(Element,-1) != -1){
            return sh.getLong(Element,-1);
        }else{
            sh.edit().putLong(Element,exValue).commit();
            return sh.getLong(Element,0);}
    }
}

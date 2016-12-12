package roccaccino.splashofflesh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Login extends AppCompatActivity {

    private DatabaseReference mDatabase;

    String SelectedRegion = "";


    utilities utils = new utilities();

    boolean oneTime = true;
    boolean HasReadEULA = false;
    boolean shititsFirst = true;

    void ShowEULASnackbar() {
        if (oneTime) {
            oneTime = false;
            Snackbar.make(getCurrentFocus(), getString(R.string.tnprmt), Snackbar.LENGTH_SHORT).setAction(getString(R.string.shw), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowEula();
                }
            }).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.Init(Login.this);
        if(utils.isInternetAvailable()){
        setContentView(R.layout.activity_login);

        if (android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0)==1) {


            (findViewById(R.id.eulaswitch)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ShowEULASnackbar();


                }
            });

            (findViewById(R.id.eulaswitch)).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ShowEula();

                    return false;
                }
            });

            mDatabase = FirebaseDatabase.getInstance().getReference();
            final ProgressDialog pdd = new ProgressDialog(Login.this);
            pdd.setMessage(getString(R.string.processing));
            pdd.setTitle(getString(R.string.wait));
            pdd.setCancelable(false);
            pdd.show();

            mDatabase.child("users").child("region").child("regionlist").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add("Scegli la tua regione");
                    temp.addAll((ArrayList<String>) dataSnapshot.getValue());

                    final ArrayList<String> regions = temp;

                    ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
                    ad.addAll(regions);

                    ((Spinner) findViewById(R.id.spinner)).setAdapter(ad);
                    findViewById(R.id.spinner).setVisibility(View.VISIBLE);
                    pdd.dismiss();
                    ((Spinner) findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!shititsFirst) {
                                if (position != 0) {
                                    SelectedRegion = regions.get(position);
                                    (findViewById(R.id.gtin)).setVisibility(View.VISIBLE);
                                    (findViewById(R.id.eulaswitch)).setVisibility(View.VISIBLE);
                                } else {
                                    SelectedRegion = "";
                                    Log.e("INFO", "Hey user chose 0!");
                                }
                            } else {
                                shititsFirst = false;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            if (!shititsFirst) {
                                (findViewById(R.id.gtin)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.eulaswitch)).setVisibility(View.VISIBLE);
                            } else {
                                shititsFirst = false;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            listenerForButton();


        } else {
            new AlertDialog.Builder(Login.this)
                    .setTitle(getString(R.string.wts))
                    .setMessage(getString(R.string.ftgynatd))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                            finish();
                        }
                    })
                    .setCancelable(false).show();
        }
    }else{
utils.NoInternetDialog();
}
    }

    public void listenerForButton() {

        findViewById(R.id.gtin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oneTime) {
                    ShowEULASnackbar();
                } else {
                    Switch eulasw = (Switch) findViewById(R.id.eulaswitch);
                    Log.e("INFO", "Eula is " + eulasw.isChecked());
                    if (eulasw.isChecked()) {
                        Log.e("INFO", "Region is " + SelectedRegion);
                        if (SelectedRegion.equals("")) {
                            Snackbar.make(getCurrentFocus(), getString(R.string.chregion), Snackbar.LENGTH_SHORT).show();
                        } else {

                            if ((!(((EditText) findViewById(R.id.passwordInput)).getText().equals(""))) && (!(((EditText) findViewById(R.id.nameInput)).getText().equals("")))) {

                                getIn(SelectedRegion, String.valueOf(((EditText) findViewById(R.id.nameInput)).getText()), String.valueOf(((EditText) findViewById(R.id.passwordInput)).getText()));

                            } else {
                                Snackbar.make(getCurrentFocus(), getString(R.string.dtmncnt), Snackbar.LENGTH_SHORT).show();
                            }


                        }


                    } else {
                        Snackbar.make(getCurrentFocus(), getString(R.string.ymstagr), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }


    void getIn(final String Region, final String name, final String password) {

        mDatabase.child("users").child("userslist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList TotalUserList = (ArrayList) dataSnapshot.getValue();

                ArrayList names = new ArrayList();
                ArrayList passwords = new ArrayList();

                for (int i = 0; i < TotalUserList.size(); i++) {

                    names.add(TotalUserList.get(i));
                    i += 1;
                    passwords.add(TotalUserList.get(i));

                }


                // Checking if user exists

                if (names.contains(name)) {
                    //Checking password

                    if (passwords.get(names.indexOf(name)).equals(password)) {
                        //Setting up Userspace

                        final SharedPreferences userdata = getSharedPreferences("userdata", MODE_PRIVATE);

                        userdata.edit().putString("name", name).commit();
                        userdata.edit().putString("password", password).commit();
                        userdata.edit().putString("region", Region).commit();

                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Position").child("x").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userdata.edit().putLong("x", (long) dataSnapshot.getValue()).commit();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Position").child("y").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userdata.edit().putLong("y", (long) dataSnapshot.getValue()).commit();
                                GoToHome();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {
                        //Sorry man that's not your best day

                        Snackbar.make(getCurrentFocus(), getString(R.string.wrgpsw), Snackbar.LENGTH_SHORT).show();

                    }

                } else {
                    //Asking if user wants a new account

                    new AlertDialog.Builder(Login.this)
                            .setTitle(getString(R.string.registr))
                            .setMessage(getString(R.string.dywntc))
                            .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Set up user
                                    final ProgressDialog pd = new ProgressDialog(Login.this);
                                    pd.setProgress(0);
                                    pd.setMax(14);
                                    pd.setMessage(getString(R.string.processing));
                                    pd.setTitle(getString(R.string.wait));
                                    pd.setCancelable(false);
                                    pd.show();


                                    final SharedPreferences userdata = getSharedPreferences("userdata", MODE_PRIVATE);
                                    userdata.edit().putString("name", name).commit();
                                    pd.setProgress(pd.getProgress() + 1);
                                    userdata.edit().putString("password", password).commit();
                                    pd.setProgress(pd.getProgress() + 1);
                                    userdata.edit().putString("region", Region).commit();
                                    pd.setProgress(pd.getProgress() + 1);

                                    mDatabase.child("users").child("region").child(Region).child("BusyCoords").child("x").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            final ArrayList<Long> xcoords = (ArrayList<Long>) dataSnapshot.getValue();


                                            mDatabase.child("users").child("region").child(Region).child("BusyCoords").child("y").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    ArrayList<Long> ycoords = (ArrayList<Long>) dataSnapshot.getValue();

                                                    Random r = new Random();
                                                    int x = r.nextInt(101);
                                                    int y = r.nextInt(101);

                                                    boolean available = false;

                                                    for (int i = 0; i < xcoords.size(); i++) {

                                                        if (Objects.equals(xcoords.get(i), (long) x)) {
                                                            if (Objects.equals(ycoords.get(i), (long) y)) {
                                                                x = r.nextInt(101);
                                                                y = r.nextInt(101);
                                                                i = 0;
                                                                available = false;
                                                            } else {
                                                                available = true;
                                                            }
                                                        } else {
                                                            available = true;
                                                        }
                                                    }

                                                    for (int f = 0; f < xcoords.size(); f++) {

                                                        if (Objects.equals(xcoords.get(f), (long) x)) {
                                                            if (Objects.equals(ycoords.get(f), (long) y)) {
                                                                f = xcoords.size() + 1;
                                                                available = false;
                                                            } else {
                                                                available = true;
                                                            }
                                                        } else {
                                                            available = true;
                                                        }
                                                    }
                                                    pd.setProgress(pd.getProgress() + 1);
                                                    if (available) {

                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Position").child("BusyCoordPosition").setValue(xcoords.size());
                                                        pd.setProgress(pd.getProgress() + 1);
                                                        xcoords.add((long) x);
                                                        ycoords.add((long) y);

                                                        mDatabase.child("users").child("region").child(Region).child("BusyCoords").child("x").setValue(xcoords);
                                                        pd.setProgress(pd.getProgress() + 1);
                                                        mDatabase.child("users").child("region").child(Region).child("BusyCoords").child("y").setValue(ycoords);
                                                        pd.setProgress(pd.getProgress() + 1);

                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Properties").child("Level").setValue(0);
                                                        pd.setProgress(pd.getProgress() + 1);
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Properties").child("Gold").setValue(50);
                                                        pd.setProgress(pd.getProgress() + 1);
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Properties").child("Food").setValue(50);
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Properties").child("Exp").setValue(0);
                                                        pd.setProgress(pd.getProgress() + 1);

                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Actions").child("List").child("0").setValue("000000");
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Actions").child("Passed").child("000000").setValue("Welcome");
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Actions").child("Incoming").child("000000").setValue("Welcome");

                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Position").child("x").setValue(x);
                                                        pd.setProgress(pd.getProgress() + 1);
                                                        mDatabase.child("users").child("region").child(Region).child("users").child(name).child("Position").child("y").setValue(y);
                                                        pd.setProgress(pd.getProgress() + 1);


                                                        userdata.edit().putLong("x", x).commit();
                                                        userdata.edit().putLong("y", y).commit();


                                                        final int ics = x;
                                                        final int ipsilon = y;

                                                        mDatabase.child("users").child("userslist").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                ArrayList<String> inf = (ArrayList<String>) dataSnapshot.getValue();
                                                                inf.add(name);
                                                                inf.add(password);
                                                                mDatabase.child("users").child("userslist").setValue(inf);
                                                                pd.setProgress(pd.getProgress() + 1);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        mDatabase.child("users").child("region").child(Region).child("userlist").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                ArrayList<String> inf = (ArrayList) dataSnapshot.getValue();
                                                                inf.add(name);
                                                                inf.add(String.valueOf(ics));
                                                                inf.add(String.valueOf(ipsilon));
                                                                inf.add("0");
                                                                mDatabase.child("users").child("region").child(Region).child("userlist").setValue(inf);
                                                                pd.setProgress(pd.getProgress() + 1);
                                                                pd.dismiss();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                    } else {
                                                        Snackbar.make(getCurrentFocus(), "region full", Snackbar.LENGTH_SHORT).show();
                                                        pd.dismiss();
                                                        GoToHome();
                                                    }


                                                    Toast.makeText(getApplicationContext(), String.valueOf(x) + "," + String.valueOf(y), Toast.LENGTH_SHORT).show();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            })
                            .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // This leads nowhere because user is lezzo


                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void GoToHome(){

        finish();
        startActivity(new Intent(this,Home.class));

    }

    void ShowEula() {

        mDatabase.child("EULA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new AlertDialog.Builder(Login.this)
                        .setTitle("EULA")
                        .setMessage((String) (dataSnapshot.getValue()))
                        .setPositiveButton(getString(R.string.ired), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                oneTime = false;
                HasReadEULA = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

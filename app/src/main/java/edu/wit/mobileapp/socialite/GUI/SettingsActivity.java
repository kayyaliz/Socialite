package edu.wit.mobileapp.socialite.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.wit.mobileapp.socialite.Keyboard.R;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Switch NLU_toggle;
    Switch TA_toggle;
    Button Delete_NLU_Data;
    Button Delete_TA_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.settings_GUI);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.settings_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NLU_toggle = (Switch) findViewById(R.id.toggle_NLU_Data) ;
        TA_toggle = (Switch) findViewById(R.id.toggle_TA_Data) ;
        Delete_NLU_Data = (Button) findViewById(R.id.Delete_NLU_Data_Button);
        Delete_TA_Data = (Button) findViewById(R.id.Delete_TA_Data_Button);
        loadSettings();
    }

    private void loadSettings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        final Query settingsQuery = reference.child("Settings");

        //LOAD DEFAULT SETTINGS
        settingsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.hasChild("Retrieve_NLU"))) {
                    reference.child("Settings").child("Retrieve_NLU").setValue(true);
                    NLU_toggle.setChecked((Boolean) dataSnapshot.child("Retrieve_NLU").getValue());
                } else {
                    NLU_toggle.setChecked((Boolean) dataSnapshot.child("Retrieve_NLU").getValue());
                }
                if(!(dataSnapshot.hasChild("Retrieve_TA"))) {
                    reference.child("Settings").child("Retrieve_TA").setValue(true);
                    TA_toggle.setChecked((Boolean) dataSnapshot.child("Retrieve_NLU").getValue());
                } else {
                    TA_toggle.setChecked((Boolean) dataSnapshot.child("Retrieve_NLU").getValue());
                }
                if(!(dataSnapshot.hasChild("Twitter_Acc"))) {
                    reference.child("Settings").child("Twitter_Acc").setValue("");
                } else {
                    TA_toggle.setChecked((Boolean) dataSnapshot.child("Retrieve_NLU").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //ON TOGGLE OF NLU OR TA
        NLU_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reference.child("Settings").child("Retrieve_NLU").setValue(isChecked);
            }
        });
        TA_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reference.child("Settings").child("Retrieve_TA").setValue(isChecked);
            }
        });

        //DELETE NLU OR TA DATA Listeners
        Delete_NLU_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder gotoalert;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    gotoalert = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    gotoalert = new AlertDialog.Builder(SettingsActivity.this);
                }
                gotoalert.setTitle("Please Confirm");
                gotoalert.setMessage("Select 'Yes' if you would like to delete all of your stored Natural Language Understanding data.");
                gotoalert.setCancelable(true);
                gotoalert.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                gotoalert.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.child("NLU_Data").removeValue();
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert = gotoalert.create();
                alert.show();
            }
        });

        Delete_TA_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder gotoalert;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    gotoalert = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    gotoalert = new AlertDialog.Builder(SettingsActivity.this);
                }
                gotoalert.setTitle("Please Confirm");
                gotoalert.setMessage("Select 'Yes' if you would like to delete all of your stored Tone Analyzer data.");
                gotoalert.setCancelable(true);
                gotoalert.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                gotoalert.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.child("TA_Data").removeValue();
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert = gotoalert.create();
                alert.show();
            }
        });
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent myIntent = new Intent(this, Home_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_nlu) {
            Intent myIntent = new Intent(this, NLU_Parent_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_ta) {
            Intent myIntent = new Intent(this, TA_Parent_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_testing) {
            Intent myIntent = new Intent(this, Testing_Parent_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        } else if (id == R.id.sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(myIntent);
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.settings_GUI);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

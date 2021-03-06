package edu.wit.mobileapp.socialite.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Set;

import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_DataEntryFragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_NLU_Fragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_TA_Fragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.TestingFragmentAdapter;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Testing_Parent_GUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Test_DataEntryFragment.SendMessage{

    public static String POSITION = "POSITION";
    TabLayout tabLayout;
    ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_create_white_24dp,
            R.drawable.ic_mic_none_white_24dp,
            R.drawable.ic_record_voice_over_white_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_parent_gui);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.testing_GUI);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.testing_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.testing_parent_viewpager);
        viewPager.setAdapter(new TestingFragmentAdapter(getSupportFragmentManager(),
                Testing_Parent_GUI.this));
        viewPager.setOffscreenPageLimit(3);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.testing_parent_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
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
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Socialite! Check it out!");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome application developed by students at Wentworth Institute of Technology called Socialite! More information at: https://www.facebook.com/Socialite-1988944241417284");
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.testing_GUI);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    @Override
    public void sendData(String message) {
        String nlu_tag = "android:switcher:" + R.id.testing_parent_viewpager + ":" + 1;
        String ta_tag = "android:switcher:" + R.id.testing_parent_viewpager + ":" + 2;
        Test_NLU_Fragment nlu_frag = (Test_NLU_Fragment) getSupportFragmentManager().findFragmentByTag(nlu_tag);
        Test_TA_Fragment ta_frag = (Test_TA_Fragment) getSupportFragmentManager().findFragmentByTag(ta_tag);
        nlu_frag.displayReceivedData(message);
        ta_frag.displayReceivedData(message);
        AlertDialog.Builder gotoalert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gotoalert = new AlertDialog.Builder(Testing_Parent_GUI.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            gotoalert = new AlertDialog.Builder(Testing_Parent_GUI.this);
        }
        gotoalert.setTitle("Insights Recieved!");
        gotoalert.setMessage("View an insight by tapping below or toggle using the tabs above.");
        gotoalert.setCancelable(true);
        gotoalert.setNegativeButton(
                "NLU",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tabLayout.getTabAt(1).select();
                        dialog.cancel();
                    }
                }
        );
        gotoalert.setPositiveButton(
                "Tone Analyzer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tabLayout.getTabAt(2).select();
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alert = gotoalert.create();
        alert.show();
    }
}

package edu.wit.mobileapp.socialite.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;

import junit.framework.Test;

import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_DataEntryFragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_NLU_Fragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_TA_Fragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.TestingFragmentAdapter;
import edu.wit.mobileapp.socialite.IBM.NLU;
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
            Intent myIntent = new Intent(this, NLU_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_ta) {
            Intent myIntent = new Intent(this, TA_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_testing) {
            Intent myIntent = new Intent(this, Testing_Parent_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

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
//        Test_NLU_Fragment nlu_frag = (Test_NLU_Fragment) getSupportFragmentManager().findFragmentByTag(nlu_tag);
//        Test_TA_Fragment ta_frag = (Test_TA_Fragment) getSupportFragmentManager().findFragmentByTag(ta_tag);
//        nlu_frag.displayReceivedData(message);
//        ta_frag.displayReceivedData(message);
        Test_NLU_Fragment nlu_frag = (Test_NLU_Fragment) getSupportFragmentManager().findFragmentByTag(nlu_tag);
        Test_TA_Fragment ta_frag = (Test_TA_Fragment) getSupportFragmentManager().findFragmentByTag(ta_tag);
        if(nlu_frag != null) {
            nlu_frag.displayReceivedData(message);
        } else {
            Log.v("Fragment", "not found");
        }
        if(ta_frag != null) {
            ta_frag.displayReceivedData(message);
        } else {
            Test_TA_Fragment new_ta_frag = new Test_TA_Fragment();
            Bundle args = new Bundle();
            args.putString("Message", message);
            new_ta_frag.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.testing_ta_fragment, new_ta_frag);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
    }
}

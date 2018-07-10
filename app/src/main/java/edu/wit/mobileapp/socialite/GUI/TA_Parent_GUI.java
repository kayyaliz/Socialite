package edu.wit.mobileapp.socialite.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.wit.mobileapp.socialite.GUI.NLU_Fragments.NLUFragmentAdapter;
import edu.wit.mobileapp.socialite.GUI.TA_Fragments.TAFragmentAdapter;
import edu.wit.mobileapp.socialite.Keyboard.R;


public class TA_Parent_GUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_parent_gui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ta_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ta_GUI);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.ta_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.ta_parent_viewpager);
        viewPager.setAdapter(new TAFragmentAdapter(getSupportFragmentManager(),
                TA_Parent_GUI.this));
        viewPager.setOffscreenPageLimit(3);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.ta_parent_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        //Create BarChart
        BarChart barChart = (BarChart) findViewById(R.id.TA_Language_Chart);

        //loadView(barChart);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ta_GUI);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

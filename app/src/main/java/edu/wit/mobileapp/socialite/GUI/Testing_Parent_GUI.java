package edu.wit.mobileapp.socialite.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import junit.framework.Test;

import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.TestingFragmentAdapter;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Testing_Parent_GUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TabLayout tabLayout;
    ViewPager viewPager;
    public static String POSITION = "POSITION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing__parent__gui);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.testing_layout);
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

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.testing_parent_tablayout);
        tabLayout.setupWithViewPager(viewPager);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.testing_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

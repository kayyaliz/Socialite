package edu.wit.mobileapp.socialite.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.IBM.NLU;
import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Testing_GUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button clickButton;
    EditText textSubmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing__gui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.testing_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.testing_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textSubmission = (EditText) findViewById(R.id.SampleText);

        clickButton = (Button) findViewById(R.id.submitText_Button);

        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String submissionText = textSubmission.getText().toString();
                if(!submissionText.isEmpty()) {
                    NLU NLUhandler = new NLU(submissionText);
                    TA TAhandler = new TA(submissionText);

                    try {
                        AnalysisResults NLU_results = NLUhandler.execute().get();
                        ToneAnalysis TA_Results = TAhandler.execute().get();
                        Log.v("TA_Results", TA_Results.toString());
                        Log.v("NLU_results", NLU_results.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent myIntent = new Intent(this, Home.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_nlu) {
            Intent myIntent = new Intent(this, NLU_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_ta) {
            Intent myIntent = new Intent(this, TA_GUI.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_testing) {
            Intent myIntent = new Intent(this, Testing_GUI.class);
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

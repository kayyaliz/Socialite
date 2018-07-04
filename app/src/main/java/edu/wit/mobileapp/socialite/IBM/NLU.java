package edu.wit.mobileapp.socialite.IBM;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import edu.wit.mobileapp.socialite.GUI.LoginActivity;
import edu.wit.mobileapp.socialite.Keyboard.R;

import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class NLU extends AsyncTask<String, String, AnalysisResults> {
    private String text = "";
    public FirebaseUser user;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference db_ref = database.getReference("Users");

    public NaturalLanguageUnderstanding service;

    public NLU() {

    }

    public NLU (String text) {
        Log.v("NLU", "Inside NLU");
        this.text = text;
        user = FirebaseAuth.getInstance().getCurrentUser();
        getValidity();
    }

    @Override
    protected AnalysisResults doInBackground(String... strings) {
        service = new NaturalLanguageUnderstanding(
                "2018-03-16",
                "11f6f8e0-24bc-414c-88a4-108ec3124d34",
                "uQT4h6Oc8qtA"
        );
        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        Features features = new Features.Builder()
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .language("en")
                .build();
        AnalysisResults response = service
                .analyze(parameters)
                .execute();
        if (user != null) {
            storeInDB(user.getUid(), response);
        } else {
            //What do?
        }
        return response;
    }

    private void storeInDB(final String uid, final AnalysisResults response) {
        final Query validityQuery = db_ref.child(user.getUid()).child("Settings");

        validityQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean valid;
                valid = (Boolean) dataSnapshot.child("Retrieve_NLU").getValue();
                if(valid) {
                    Map<String, String> timestamp = ServerValue.TIMESTAMP;
                    DatabaseReference PostRef = db_ref.child(uid).child("NLU_Data").push();
                    PostRef.child("data").setValue(response);
                    PostRef.child("Timestamp").setValue(timestamp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getValidity() {

    }
}

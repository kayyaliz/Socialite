package edu.wit.mobileapp.socialite.IBM;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ServerValue;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneChatOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Utterance;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.UtteranceAnalyses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TA extends AsyncTask<String, String, ToneAnalysis> {
    private String text = "";
    public FirebaseUser user;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference db_ref = database.getReference("Users");

    public ToneAnalyzer service;

    public TA() {

    }

    public TA(String text)
    {
        Log.v("TA", "Inside TA");
        this.text = text;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    protected ToneAnalysis doInBackground(String... strings) {
        service = new ToneAnalyzer(
                "2017-09-21",
                "90462232-3dca-4cea-9845-f83efd34b6f3",
                "h2X0mSzGug4O");
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        ToneAnalysis documentTone = service.tone(toneOptions).execute();
        if (user != null){
            storeInDb(user.getUid(), documentTone);
        }
        else
        {

        }
        return documentTone;
    }

    private void storeInDb(String uid, ToneAnalysis utterancesTone) {
        Map<String, String> timestamp = ServerValue.TIMESTAMP;
        DatabaseReference PostRef = db_ref.child(uid).child("TA_Data").push();
        PostRef.child("data").setValue(utterancesTone);
        PostRef.child("Timestamp").setValue(timestamp);
    }
}

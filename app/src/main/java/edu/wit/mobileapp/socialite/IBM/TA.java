package edu.wit.mobileapp.socialite.IBM;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ServerValue;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneChatOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Utterance;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.UtteranceAnalyses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TA extends AsyncTask<String, Void, Void>{
    public String text = "";

    public FirebaseUser user;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference db_ref = database.getReference("Users");


    public ToneAnalyzer service;
    List<Utterance> utteranceList;

    public TA(String text)
    {
        Log.v("TA", "Inside TA");
        this.text = text;
        this.utteranceList = new ArrayList<Utterance>();

        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    protected Void doInBackground(String... strings) {
        service = new ToneAnalyzer(
                "2017-09-21",
                "90462232-3dca-4cea-9845-f83efd34b6f3",
                "h2X0mSzGug4O");
        Log.v("TA", text);

        String [] sentences = text.split("(?<=\\.\\s)|(?<=[?!]\\s)");
        List<Utterance> utterances = new ArrayList<Utterance>();

        for (int i=0; i < sentences.length; i++)
        {
            utterances.add(new Utterance.Builder()
                    .text(sentences[i])
                    .user("agent")
                    .build());
        }

        ToneChatOptions toneChatOptions = new ToneChatOptions.Builder()
                .utterances(utterances)
                .build();
        UtteranceAnalyses utterancesTone = service.toneChat(toneChatOptions).execute();
        System.out.println(utterancesTone);

        if (user != null){
            storeInDb(user.getUid(), utterancesTone);
        }
        else
        {

        }

        return null;
    }

    private void storeInDb(String uid, UtteranceAnalyses utterancesTone) {
        Map<String, String> timestamp = ServerValue.TIMESTAMP;

        DatabaseReference PostRef = db_ref.child(uid).child("TA_Data").push();
        PostRef.child("data").setValue(utterancesTone);
        PostRef.child("Timestamp").setValue(timestamp);

    }

    public class TA_Data {
        UtteranceAnalyses utterancesTone;
        Map<String, Object> timestamp;

        public TA_Data() {

        }

        public TA_Data(UtteranceAnalyses utterancesTone) {
            this.utterancesTone = utterancesTone;
            HashMap<String, Object> timestampNow = new HashMap<>();
            timestampNow.put("timestamp", ServerValue.TIMESTAMP);
            this.timestamp = timestampNow;
        }
    }
}

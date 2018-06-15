package edu.wit.mobileapp.socialite.IBM;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import edu.wit.mobileapp.socialite.Keyboard.R;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;

public class NLU extends AsyncTask<String, Void, Void>{
    private String text = "";

    NaturalLanguageUnderstanding service;

    public NLU (String text) {
        Log.v("NLU", "Inside NLU");
        this.text = text;
    }

    @Override
    protected Void doInBackground(String... strings) {
        service = new NaturalLanguageUnderstanding(
                "2018-03-16",
                "11f6f8e0-24bc-414c-88a4-108ec3124d34",
                "uQT4h6Oc8qtA"
        );
//        service.setUsernameAndPassword("11f6f8e0-24bc-414c-88a4-108ec3124d34", "uQT4h6Oc8qtA");
        Log.v("NLU", text);
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
                .build();
        AnalysisResults response = service
                .analyze(parameters)
                .execute();
        System.out.println(response);
        Log.v("RESPONSE", response.toString());
        return null;
    }
}

package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.GUI.Helpers.ExpandableListAdapter;
import edu.wit.mobileapp.socialite.IBM.NLU;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Test_NLU_Fragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_testing_nlu, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.testing_nlu_expandable_list_view);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listDataHeader.add("No Insights");
        List<String> EmotionArr = new ArrayList<String>();
        EmotionArr.add("No data has been entered yet. Click the \"Enter Data\" tab and enter text to begin!");
        listDataChild.put(listDataHeader.get(0), EmotionArr);
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void displayReceivedData(String message)
    {
        try {
            AnalysisResults NLU_results = new NLU(message).execute().get();
            Toast.makeText(getActivity(),
                        "NLU Response Received!",
                        Toast.LENGTH_SHORT).show();
            Log.v("NLU_Results", NLU_results.toString());
            listDataHeader.clear();
            listDataChild.clear();
            if (NLU_results.getKeywords().size() > 0 || NLU_results.getEntities().size() > 0) {
                for(int i = 0; i < NLU_results.getKeywords().size(); i++) {
                    listDataHeader.add("Keyword: " + NLU_results.getKeywords().get(i).getText());
                    List<String> KeywordArr = new ArrayList<String>();
                    KeywordArr.add("Anger: " +NLU_results.getKeywords().get(i).getEmotion().getAnger().toString());
                    KeywordArr.add("Disgust: " + NLU_results.getKeywords().get(i).getEmotion().getDisgust().toString());
                    KeywordArr.add("Fear: " + NLU_results.getKeywords().get(i).getEmotion().getFear().toString());
                    KeywordArr.add("Joy: " + NLU_results.getKeywords().get(i).getEmotion().getJoy().toString());
                    KeywordArr.add("Sadness: " + NLU_results.getKeywords().get(i).getEmotion().getSadness().toString());
                    KeywordArr.add("Relevance: " + NLU_results.getKeywords().get(i).getRelevance().toString());
                    KeywordArr.add("Sentiment: " + NLU_results.getKeywords().get(i).getSentiment().getScore().toString());
                    listDataChild.put(listDataHeader.get(listDataHeader.size()-1), KeywordArr);
                }
                for(int i = 0; i < NLU_results.getEntities().size(); i++) {
                    listDataHeader.add("Entity: " + NLU_results.getEntities().get(i).getText().trim());
                    List<String> EntityArr = new ArrayList<String>();
                    EntityArr.add("Type: " + NLU_results.getEntities().get(i).getType().trim());
                    EntityArr.add("Anger: " + NLU_results.getEntities().get(i).getEmotion().getAnger().toString());
                    EntityArr.add("Disgust: " + NLU_results.getEntities().get(i).getEmotion().getDisgust().toString());
                    EntityArr.add("Fear: " + NLU_results.getEntities().get(i).getEmotion().getFear().toString());
                    EntityArr.add("Joy: " + NLU_results.getEntities().get(i).getEmotion().getJoy().toString());
                    EntityArr.add("Sadness: " + NLU_results.getEntities().get(i).getEmotion().getSadness().toString());
                    EntityArr.add("Relevance: " + NLU_results.getEntities().get(i).getRelevance().toString());
                    EntityArr.add("Sentiment: " + NLU_results.getEntities().get(i).getSentiment().getScore().toString());
                    listDataChild.put(listDataHeader.get(listDataHeader.size()-1), EntityArr);
                }
            } else {
                listDataHeader.add("No Insights");
                List<String> InfoArr = new ArrayList<String>();
                InfoArr.add("No natural language emotion insights could be found on the the text \"" + message + "\". Try entering more data!");
                listDataChild.put(listDataHeader.get(0), InfoArr);
            }
            listAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(),
                    "NLU Response Failed!",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(getActivity(),
                    "NLU Response Failed!",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

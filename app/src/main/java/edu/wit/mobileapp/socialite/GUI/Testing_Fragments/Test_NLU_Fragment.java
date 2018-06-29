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

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

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
            Log.v("NLU_Results", NLU_results.toString());
            listDataHeader.clear();
            listDataChild.clear();
            if (NLU_results.getKeywords().size() > 0 || NLU_results.getEntities().size() > 0) {
                for(int i = 0; i < NLU_results.getKeywords().size(); i++) {
                    listDataHeader.add("Keyword: " + NLU_results.getKeywords().get(i).getText());
                    List<String> EmotionArr = new ArrayList<String>();
                    EmotionArr.add("Anger: " + NLU_results.getKeywords().get(i).getEmotion().getAnger().toString());
                    EmotionArr.add("Disgust: " + NLU_results.getKeywords().get(i).getEmotion().getDisgust().toString());
                    EmotionArr.add("Fear: " + NLU_results.getKeywords().get(i).getEmotion().getFear().toString());
                    EmotionArr.add("Joy: " + NLU_results.getKeywords().get(i).getEmotion().getJoy().toString());
                    EmotionArr.add("Sadness: " + NLU_results.getKeywords().get(i).getEmotion().getSadness().toString());
                    EmotionArr.add("Relevance: " + NLU_results.getKeywords().get(i).getRelevance().toString());
                    EmotionArr.add("Sentiment: " + NLU_results.getKeywords().get(i).getSentiment().getScore().toString());
                    listDataChild.put(listDataHeader.get(i), EmotionArr);
                }
                for(int i = 0; i < NLU_results.getEntities().size(); i++) {
                    listDataHeader.add("Entity: " + NLU_results.getEntities().get(i).getText().trim());
                    List<String> EmotionArr = new ArrayList<String>();
                    EmotionArr.add("Type: " + NLU_results.getEntities().get(i).getType().trim());
                    EmotionArr.add("Anger: " + NLU_results.getEntities().get(i).getEmotion().getAnger().toString());
                    EmotionArr.add("Disgust: " + NLU_results.getEntities().get(i).getEmotion().getDisgust().toString());
                    EmotionArr.add("Fear: " + NLU_results.getEntities().get(i).getEmotion().getFear().toString());
                    EmotionArr.add("Joy: " + NLU_results.getEntities().get(i).getEmotion().getJoy().toString());
                    EmotionArr.add("Sadness: " + NLU_results.getEntities().get(i).getEmotion().getSadness().toString());
//                    EmotionArr.add("Relevance: " + NLU_results.getEntities().get(i).getRelevance().toString());
//                    EmotionArr.add("Sentiment: " + NLU_results.getEntities().get(i).getSentiment().getScore().toString());
                    listDataChild.put(listDataHeader.get(i), EmotionArr);
                }
            } else {
                listDataHeader.add("No Insights");
                List<String> EmotionArr = new ArrayList<String>();
                EmotionArr.add("No natural language emotion insights could be found on the the text \"" + message + "\". Try entering more data!");
                listDataChild.put(listDataHeader.get(0), EmotionArr);
            }
            listAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

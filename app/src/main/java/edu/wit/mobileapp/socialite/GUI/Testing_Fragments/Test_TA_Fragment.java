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

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.GUI.Helpers.ExpandableListAdapter;
import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;


public class Test_TA_Fragment extends Fragment {

    ExpandableListAdapter listAdapter_doc;
    ExpandableListView expListView_doc;
    List<String> listDataHeader_doc;
    HashMap<String, List<String>> listDataChild_doc;

    ExpandableListAdapter listAdapter_sent;
    ExpandableListView expListView_sent;
    List<String> listDataHeader_sent;
    HashMap<String, List<String>> listDataChild_sent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_testing_ta, container, false);
        expListView_doc = (ExpandableListView) rootView.findViewById(R.id.testing_elv_ta_doc);
        listDataHeader_doc = new ArrayList<String>();
        listDataChild_doc = new HashMap<String, List<String>>();

        expListView_sent = (ExpandableListView) rootView.findViewById(R.id.testing_elv_ta_sent);
        listDataHeader_sent = new ArrayList<String>();
        listDataChild_sent = new HashMap<String, List<String>>();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listDataHeader_doc.add("No Insights");
        List<String> ToneArr_Doc = new ArrayList<String>();
        ToneArr_Doc.add("No data has been entered yet. Click the \"Enter Data\" tab and enter text to begin!");
        listDataChild_doc.put(listDataHeader_doc.get(0), ToneArr_Doc);
        listAdapter_doc = new ExpandableListAdapter(this.getActivity(), listDataHeader_doc, listDataChild_doc);
        expListView_doc.setAdapter(listAdapter_doc);

        listDataHeader_sent.add("No Insights");
        List<String> ToneArr_sent = new ArrayList<String>();
        ToneArr_sent.add("No data has been entered yet. Click the \"Enter Data\" tab and enter text to begin!");
        listDataChild_sent.put(listDataHeader_sent.get(0), ToneArr_sent);
        listAdapter_sent = new ExpandableListAdapter(this.getActivity(), listDataHeader_sent, listDataChild_sent);
        expListView_sent.setAdapter(listAdapter_sent);
    }

    public void displayReceivedData(String message)
    {
        Log.v("MESSAGE", message);
        ToneAnalysis TA_results = null;
        try {
            TA_results = new TA(message).execute().get();
            Log.v("Test", TA_results.toString());

            Toast.makeText(getActivity(),
                    "TA Response Received!",
                    Toast.LENGTH_SHORT).show();
            Log.v("TA_Results", TA_results.toString());
            listDataHeader_doc.clear();
            listDataChild_doc.clear();
            listDataHeader_sent.clear();
            listDataChild_sent.clear();
            if (TA_results.getDocumentTone().getTones().size() > 0) {
                for(int i = 0; i < TA_results.getDocumentTone().getTones().size(); i++) {
                    listDataHeader_doc.add("Tone: " + TA_results.getDocumentTone().getTones().get(i).getToneName());
                    List<String> KeywordArr = new ArrayList<String>();
                    KeywordArr.add("Score: " + TA_results.getDocumentTone().getTones().get(i).getScore().toString());
                    listDataChild_doc.put(listDataHeader_doc.get(listDataHeader_doc.size()-1), KeywordArr);
                }
            } else {
                listDataHeader_doc.add("No Insights");
                List<String> InfoArr = new ArrayList<String>();
                InfoArr.add("No tone analyzer insights could be found on the the text \"" + message + "\". Try entering more data!");
                listDataChild_doc.put(listDataHeader_doc.get(0), InfoArr);
            }
            listAdapter_doc.notifyDataSetChanged();
            try{
                if (TA_results.getSentencesTone().size() > 0) {
                    for(int i = 0; i < TA_results.getSentencesTone().size(); i++) {
                        listDataHeader_sent.add("Sentence: " + TA_results.getSentencesTone().get(i).getText());
                        List<String> KeywordArr = new ArrayList<String>();
                        for (int j = 0; j < TA_results.getSentencesTone().get(i).getTones().size(); j++) {
                            KeywordArr.add(TA_results.getSentencesTone().get(i).getTones().get(j).getToneName() + " : " + TA_results.getSentencesTone().get(i).getTones().get(j).getScore().toString());
                        }
                        listDataChild_sent.put(listDataHeader_sent.get(listDataHeader_sent.size()-1), KeywordArr);
                    }
                } else {
                    listDataHeader_sent.add("No Insights");
                    List<String> InfoArr = new ArrayList<String>();
                    InfoArr.add("No tone analyzer insights could be found on the the text \"" + message + "\". Try entering more data!");
                    listDataChild_sent.put(listDataHeader_sent.get(0), InfoArr);
                }
            } catch (Exception e) {
                listDataHeader_sent.add("No Insights");
                List<String> InfoArr = new ArrayList<String>();
                InfoArr.add("No tone analyzer sentence level insights could be found, as only one sentence was entered.");
                listDataChild_sent.put(listDataHeader_sent.get(0), InfoArr);
            }
            listAdapter_sent.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

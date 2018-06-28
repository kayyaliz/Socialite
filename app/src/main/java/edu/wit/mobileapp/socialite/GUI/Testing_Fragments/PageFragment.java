package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.IBM.NLU;
import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewDataEntry = inflater.inflate(R.layout.fragment_testing_data_entry, container, false);
        View view_nlu = inflater.inflate(R.layout.fragment_testing_nlu, container, false);
        View view_ta = inflater.inflate(R.layout.fragment_testing_ta, container, false);


        ExpandableListAdapter listAdapter;
        ExpandableListView NLU_exp_list_view;
        final List<String> NLU_listDataHeader = null;
        HashMap<String, List<String>> NLU_listDataChild;


        if (mPage == 1) {
            Button clickButton;
            final EditText textSubmission;

            textSubmission = (EditText) viewDataEntry.findViewById(R.id.SampleText);
            clickButton = (Button) viewDataEntry.findViewById(R.id.submitText_Button);

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
                            for(int i = 0; i < NLU_results.getEntities().size(); i++) {
                                NLU_listDataHeader.add("Insight " + i);
                                List<String> EmotionArr = new ArrayList<String>();
                                Double Anger = NLU_results.getEntities().get(i).getEmotion().getAnger();
                                Double Disgust = NLU_results.getEntities().get(i).getEmotion().getDisgust();
                                Double Fear = NLU_results.getEntities().get(i).getEmotion().getFear();
                                Double Joy = NLU_results.getEntities().get(i).getEmotion().getJoy();
                                Double Sadness = NLU_results.getEntities().get(i).getEmotion().getSadness();
                            }
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
            return viewDataEntry;
        } else if (mPage == 2) {
            NLU_exp_list_view = (ExpandableListView) view_nlu.findViewById(R.id.testing_nlu_expandable_list_view);

            return view_nlu;
        } else {
            return view_ta;
        }
    }
}

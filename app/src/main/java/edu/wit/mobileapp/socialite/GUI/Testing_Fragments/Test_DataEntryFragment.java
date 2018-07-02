package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.IBM.NLU;
import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Test_DataEntryFragment extends Fragment {
    SendMessage SM;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_testing_data_entry, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button clickButton;
        final EditText textSubmission;

        textSubmission = (EditText) view.findViewById(R.id.SampleText);
        clickButton = (Button) view.findViewById(R.id.submitText_Button);

        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String submissionText = textSubmission.getText().toString();
                if(!submissionText.isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    SM.sendData(submissionText);
                }
            }
        });

    }

    public interface SendMessage {
        void sendData(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}

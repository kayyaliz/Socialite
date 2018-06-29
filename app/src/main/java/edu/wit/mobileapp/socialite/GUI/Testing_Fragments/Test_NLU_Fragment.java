package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wit.mobileapp.socialite.IBM.NLU;
import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Test_NLU_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_testing_nlu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        NLU NLUhandler = new NLU(submissionText);
    }

    public void displayReceivedData(String message)
    {
        Log.v("NLU_FRAGMENT", message);
    }
}

package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wit.mobileapp.socialite.IBM.TA;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class Test_TA_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_testing_ta, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        TA TAhandler = new TA(submissionText);

    }

    public void displayReceivedData(String message)
    {
        Log.v("TA_FRAGMENT", message);
    }
}

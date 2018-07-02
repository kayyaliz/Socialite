package edu.wit.mobileapp.socialite.GUI.NLU_Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_DataEntryFragment;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class NLU_Graph_Fragment  extends Fragment {
    Spinner dateRangeSpinner;
    PieChart emotionChart;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_nlu_graph, container, false);
        //SPINNER FOR DATE FIELDS
        dateRangeSpinner = (Spinner) rootView.findViewById(R.id.NLU_Graph_DateRangeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.DateRangeArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateRangeSpinner.setAdapter(adapter);

        //RADAR CHART SETUP
        emotionChart = (PieChart) rootView.findViewById(R.id.NLU_Emotion_Chart);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Long unixTime = System.currentTimeMillis();
        final Long One_Day_Millis = (24 * 60 * 60 * 1000) * 1L;
        loadViews(emotionChart, (unixTime - (1 * One_Day_Millis)));

        dateRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String dateItemText = dateRangeSpinner.getSelectedItem().toString();
                Log.v("DateRange", dateItemText);
                Long startTime = null;
                switch (dateItemText) {
                    case "All Time":
                        startTime = 0L;
                        break;
                    case "Last 24 Hours":
                        startTime = unixTime - (1 * One_Day_Millis);
                        break;
                    case "Last 7 Days":
                        startTime = unixTime - (7 * One_Day_Millis);
                        break;
                    case "Last 30 Days":
                        startTime = unixTime - (30 * One_Day_Millis);
                        break;
                    case "Last Year":
                        startTime = unixTime - (365 * One_Day_Millis);
                        break;
                    default:
                        startTime = 0L;
                }
                loadViews(emotionChart, startTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void loadViews(final PieChart emotionChart, long startTime) {
        final List<PieEntry> entries = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("NLU_Data");

        // Attach a listener to read the data at our posts reference
        reference.orderByChild("Timestamp").startAt(startTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Emotions NLU Tracks
                float Anger = 0f;
                float Disgust = 0f;
                float Fear = 0f;
                float Joy = 0f;
                float Sadness = 0f;
                for(DataSnapshot instance: dataSnapshot.getChildren()) {
                    DataSnapshot keywords_snapshot = instance.child("data").child("keywords");
                    for (DataSnapshot keywords: keywords_snapshot.getChildren()) {
                        if (keywords.hasChild("emotion")) {
                            Anger += keywords.child("emotion").child("anger").getValue(Float.class);
                            Disgust += keywords.child("emotion").child("disgust").getValue(Float.class);
                            Fear += keywords.child("emotion").child("fear").getValue(Float.class);
                            Joy += keywords.child("emotion").child("joy").getValue(Float.class);
                            Sadness += keywords.child("emotion").child("sadness").getValue(Float.class);
                        }
                    }
                    DataSnapshot entities_snapshot = instance.child("data").child("entities");
                    for (DataSnapshot keywords: entities_snapshot.getChildren()) {
                        if (keywords.hasChild("emotion")) {
                            Anger += keywords.child("emotion").child("anger").getValue(Float.class);
                            Disgust += keywords.child("emotion").child("disgust").getValue(Float.class);
                            Fear += keywords.child("emotion").child("fear").getValue(Float.class);
                            Joy += keywords.child("emotion").child("joy").getValue(Float.class);
                            Sadness += keywords.child("emotion").child("sadness").getValue(Float.class);
                        }
                    }
                }
                float Total = Anger + Disgust + Fear + Joy + Sadness;
                Anger = (Anger / Total) * 100;
                Disgust = (Disgust / Total) * 100;
                Fear = (Fear / Total) * 100;
                Joy = (Joy / Total) * 100;
                Sadness = (Sadness / Total) * 100;

                entries.add(new PieEntry(Anger, "Anger"));
                entries.add(new PieEntry(Disgust, "Disgust"));
                entries.add(new PieEntry(Fear, "Fear"));
                entries.add(new PieEntry(Joy, "Joy"));
                entries.add(new PieEntry(Sadness, "Sadness"));
                final int[] MY_COLORS = {Color.parseColor("#BA1200"), Color.parseColor("#248232"), Color.parseColor("#9649CB"),
                        Color.parseColor("#F0C808"), Color.parseColor("#004E89")};
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for(int c: MY_COLORS) colors.add(c);

                PieDataSet set = new PieDataSet(entries, "Emotion Results");
                set.setValueTextSize(15f);
                set.setColors(colors);
                PieData data = new PieData(set);
                Legend legend = emotionChart.getLegend();
                emotionChart.getDescription().setText("");
                legend.setEnabled(false);
                emotionChart.setData(data);
                emotionChart.invalidate(); // refresh
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

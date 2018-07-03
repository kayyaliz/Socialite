package edu.wit.mobileapp.socialite.GUI.TA_Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.wit.mobileapp.socialite.Keyboard.R;

public class TA_Graph_Fragment extends android.support.v4.app.Fragment {

    BarChart bar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_ta_graph, container, false);

        //Setup Bar Chart
        bar = (BarChart) rootView.findViewById(R.id.TA_Language_Chart);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadView(bar);
    }


    private void loadView(final BarChart barChart) {
        final List<BarEntry> entries = new ArrayList<>();

        // Instantiate Discard Array
        final List<String> discard = new ArrayList<>();
        discard.add("anger");
        discard.add("disgust");
        discard.add("fear");
        discard.add("joy");
        discard.add("sadness");

        // Database stuff
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("TA_Data");

        // Attach a listener to read the data at our posts reference
        reference.orderByChild("Timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Lang Variables
                float analytical=0f;
                float confident=0f;
                float tentative=0f;
                float total=0f;
                //Get the data
                for(DataSnapshot instance: dataSnapshot.getChildren()) {
                    DataSnapshot count = instance.child("data").child("documentTone");

                    //Get total amount of documentTones
                    for (DataSnapshot c: count.getChildren()){
                        total+= (float)1.0;
                    }

                    DataSnapshot data = instance.child("data").child("documentTone").child("tones");
                    for (DataSnapshot tones: data.getChildren()) {

                        String t_id = tones.child("toneId").getValue(String.class);

                        if (!(discard.contains(t_id))) {
                            switch (t_id){
                                case "analytical":
                                    analytical += (float) 1.0;
                                    break;
                                case "confident":
                                    confident += (float) 1.0;
                                    break;
                                case "tentative":
                                    tentative += (float) 1.0;
                                    break;
                                default:
                            }
                        }
                    }
                }

                entries.add(new BarEntry(0f, analytical));
                entries.add(new BarEntry(1f, confident));
                entries.add(new BarEntry(2f, tentative));

                BarDataSet set = new BarDataSet(entries, "Socialite");

                BarData data = new BarData(set);
                data.setBarWidth(0.9f); // set custom bar width
                Legend legend = barChart.getLegend();
                barChart.getDescription().setText("");
                legend.setEnabled(false);
                barChart.setData(data);


                // Create the labels for the bars
                final ArrayList<String> xVals = new ArrayList<>();
                xVals.add("Analytical");
                xVals.add("Confident");
                xVals.add("Tentative");

                // Create colors for each bar
                final int[] MY_COLORS = {Color.parseColor("#ff6666"), Color.parseColor("#6699ff"), Color.parseColor("#ffff99")};
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for(int c: MY_COLORS) colors.add(c);

                set.setValueTextSize(15f);
                set.setColors(colors);

                // Display labels for bars
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));


                // Define xAxis Properties
                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setYOffset(1f);
                xAxis.setTextSize(15f);

                // Hide grid lines
                barChart.getAxisLeft().setEnabled(false);
                barChart.getAxisRight().setEnabled(false);
                // Hide graph description
                barChart.getDescription().setEnabled(false);
                // Hide graph legend
                barChart.getLegend().setEnabled(false);
                barChart.setFitBars(true); // make the x-axis fit exactly all bars
                barChart.invalidate(); // refresh
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

package edu.wit.mobileapp.socialite.GUI.TA_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wit.mobileapp.socialite.GUI.Helpers.ExpandableListAdapter;
import edu.wit.mobileapp.socialite.Keyboard.R;

public class TA_List_Fragment extends Fragment {

    Spinner dateRangeSpinner;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_ta_list, container, false);

        dateRangeSpinner = (Spinner) rootView.findViewById(R.id.TA_List_DateRangeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.DateRangeArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateRangeSpinner.setAdapter(adapter);

        expListView = (ExpandableListView) rootView.findViewById(R.id.ta_list_elv_doc);
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

//        final Long unixTime = System.currentTimeMillis();
//        final Long One_Day_Millis = (24 * 60 * 60 * 1000) * 1L;
//        loadList((unixTime - (1 * One_Day_Millis)));
//
//        dateRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String dateItemText = dateRangeSpinner.getSelectedItem().toString();
//                Log.v("DateRange", dateItemText);
//                Long startTime = null;
//                switch (dateItemText) {
//                    case "All Time":
//                        startTime = 0L;
//                        break;
//                    case "Last 24 Hours":
//                        startTime = unixTime - (1 * One_Day_Millis);
//                        break;
//                    case "Last 7 Days":
//                        startTime = unixTime - (7 * One_Day_Millis);
//                        break;
//                    case "Last 30 Days":
//                        startTime = unixTime - (30 * One_Day_Millis);
//                        break;
//                    case "Last Year":
//                        startTime = unixTime - (365 * One_Day_Millis);
//                        break;
//                    default:
//                        startTime = 0L;
//                }
//                loadList(startTime);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//        });
    }

    private void loadList(long startTime) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("NLU_Data");
        listDataHeader.clear();
        listDataChild.clear();
        // Attach a listener to read the data at our posts reference
        reference.orderByChild("Timestamp").startAt(startTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Emotions NLU Tracks
                for(DataSnapshot instance: dataSnapshot.getChildren()) {
                    DataSnapshot entities_snapshot = instance.child("data").child("entities");
                    for (DataSnapshot entities: entities_snapshot.getChildren()) {
                        if (entities.hasChild("emotion")) {
                            listDataHeader.add("Entity: " + entities.child("text").getValue().toString());
                            List<String> KeywordArr = new ArrayList<String>();
                            KeywordArr.add("Anger: " + entities.child("emotion").child("anger").getValue(Float.class).toString());
                            KeywordArr.add("Disgust: " + entities.child("emotion").child("disgust").getValue(Float.class).toString());
                            KeywordArr.add("Fear: " + entities.child("emotion").child("fear").getValue(Float.class).toString());
                            KeywordArr.add("Joy: " + entities.child("emotion").child("joy").getValue(Float.class).toString());
                            KeywordArr.add("Sadness: " + entities.child("emotion").child("sadness").getValue(Float.class).toString());
                            KeywordArr.add("Relevance: " + entities.child("relevance").getValue().toString());
                            KeywordArr.add("Sentiment: " + entities.child("sentiment").child("score").getValue(Float.class).toString());
                            listDataChild.put(listDataHeader.get(listDataHeader.size()-1), KeywordArr);
                        }
                    }
                    DataSnapshot keywords_snapshot = instance.child("data").child("keywords");
                    for (DataSnapshot keywords: keywords_snapshot.getChildren()) {
                        if (keywords.hasChild("emotion")) {
                            listDataHeader.add("Keyword: " + keywords.child("text").getValue().toString());
                            List<String> KeywordArr = new ArrayList<String>();
                            KeywordArr.add("Anger: " + keywords.child("emotion").child("anger").getValue(Float.class).toString());
                            KeywordArr.add("Disgust: " + keywords.child("emotion").child("disgust").getValue(Float.class).toString());
                            KeywordArr.add("Fear: " + keywords.child("emotion").child("fear").getValue(Float.class).toString());
                            KeywordArr.add("Joy: " + keywords.child("emotion").child("joy").getValue(Float.class).toString());
                            KeywordArr.add("Sadness: " + keywords.child("emotion").child("sadness").getValue(Float.class).toString());
                            KeywordArr.add("Relevance: " + keywords.child("relevance").getValue().toString());
                            KeywordArr.add("Sentiment: " + keywords.child("sentiment").child("score").getValue(Float.class).toString());
                            listDataChild.put(listDataHeader.get(listDataHeader.size()-1), KeywordArr);
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

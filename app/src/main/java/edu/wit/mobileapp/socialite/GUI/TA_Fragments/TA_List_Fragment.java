package edu.wit.mobileapp.socialite.GUI.TA_Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class TA_List_Fragment extends android.support.v4.app.Fragment {

    // Spinner
    Spinner dateRangeSpinner;

    //Document Tone
    ExpandableListAdapter listAdapter_doc;
    ExpandableListView expListView_doc;
    List<String> listDataHeader_doc;
    HashMap<String, List<String>> listDataChild_doc;

    //Sentence Tone
    ExpandableListAdapter listAdapter_sent;
    ExpandableListView expListView_sent;
    List<String> listDataHeader_sent;
    HashMap<String, List<String>> listDataChild_sent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_ta_list, container, false);

        dateRangeSpinner = (Spinner) rootView.findViewById(R.id.TA_List_DateRangeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.DateRangeArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateRangeSpinner.setAdapter(adapter);

        expListView_doc = (ExpandableListView) rootView.findViewById(R.id.ta_list_elv_doc);
        listDataHeader_doc = new ArrayList<String>();
        listDataChild_doc = new HashMap<String, List<String>>();

        expListView_sent = (ExpandableListView) rootView.findViewById(R.id.ta_list_elv_sent);
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

        final Long unixTime = System.currentTimeMillis();
        final Long One_Day_Millis = (24 * 60 * 60 * 1000) * 1L;
        loadList((unixTime - (1 * One_Day_Millis)));

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
                loadList(startTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void loadList(long startTime) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("TA_Data");
        listDataHeader_doc.clear();
        listDataChild_doc.clear();
        listDataHeader_sent.clear();
        listDataChild_sent.clear();

        final List<String> discard = new ArrayList<>();
        discard.add("anger");
        discard.add("disgust");
        discard.add("fear");
        discard.add("joy");
        discard.add("sadness");

        // Attach a listener to read the data at our posts reference
        reference.orderByChild("Timestamp").startAt(startTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float analytical=0f;
                float confident=0f;
                float tentative=0f;

                //Get Document Totals
                for(DataSnapshot instance: dataSnapshot.getChildren()) {
                    DataSnapshot documentTones = instance.child("data").child("documentTone").child("tones");
                    for (DataSnapshot d_tones: documentTones.getChildren()) {

                        //Set Tone Attributes
                        String t_id = d_tones.child("toneId").getValue(String.class);
                        float t_score = d_tones.child("score").getValue(Float.class);

                        if (!(discard.contains(t_id))) {
                            switch (t_id){
                                case "analytical":
                                    analytical += t_score;
                                    break;
                                case "confident":
                                    confident += t_score;
                                    break;
                                case "tentative":
                                    tentative += t_score;
                                    break;
                                default:
                            }
                        }
                    }
                }

                //Add Tone Headers to parent ExtendedList
                listDataHeader_doc.add("Analytical");
                listDataHeader_doc.add("Tentative");
                listDataHeader_doc.add("Confident");

                //Add tone score to String List
                List<String> tScoreArr = new ArrayList<String>();
                tScoreArr.add(Float.toString(analytical));
                tScoreArr.add(Float.toString(tentative));
                tScoreArr.add(Float.toString(confident));

                for (int i=0; i<=tScoreArr.size(); i++){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
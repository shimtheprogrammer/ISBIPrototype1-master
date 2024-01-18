package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.R;

import com.prototype.isbi.isbiprototype1.AssetOptionsActivity;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Assets;
import com.prototype.isbi.isbiprototype1.databaseHandlers.AssetsHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.EightELAdapter;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/9/2017.
 */

public class AssetsDisplayFragment extends Fragment {
    public static String TAG = "AssetsDisplayFragment";

    EightELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList = new HashMap<>();
    HashMap<String, String> nameList = new HashMap<>();
    HashMap<String, HashMap<String, String>> groupList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the listview
        final View view = inflater.inflate(R.layout.fragment_pre_purchase_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData(getActivity());

        listAdapter = new EightELAdapter(getActivity(), listDataHeader,
                listDataChild, "ASS", false);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                for(String Key : nameList.keySet()){
                    if(Key.matches("" + childPosition)) {
                        if(!(nameList.get(Key).matches("Licenses"))){
                            Log.d(TAG, "selected; " + Key + " is same " + childPosition + " has " + nameList.get(Key));

                            Intent intent = new Intent(getActivity().getApplicationContext(), AssetOptionsActivity.class);
                            intent.putExtra("ID", idList.get(Key));

                            if(nameList.get(Key).matches("Land")){
                                intent.putExtra("FROM", "LAN");
                            } else if(nameList.get(Key).matches("Building")){
                                intent.putExtra("FROM", "BUI");
                            } else if(nameList.get(Key).matches("Vehicle")){
                                intent.putExtra("FROM", "VEH");
                            } else if(nameList.get(Key).matches("Equipment")){
                                intent.putExtra("FROM", "EQU");
                            } else if(nameList.get(Key).matches("Other")){
                                intent.putExtra("FROM", "OTH");
                            }

                            startActivity(intent);
                        }
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);

        return view;
    }

    private void prepareListData(Activity activity) {
        AssetsHandler edb = new AssetsHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> assets = new ArrayList<>();

        List<Assets> allAssets = edb.getAllAssets();

        String title = "ID-Type-Info-Total-Method-Date-Useful Life-Scrap Value";
        //assets.add(title);
        int count = 0;
        for (Assets pd : allAssets) {
            String useful = "N/A", scrap = "N/A";

            String log;

            if(pd.getScrsp() > 0 && pd.getUsefull() > 0){
                log = pd.getID() + "-" + pd.getType() + "-" + pd.getInfo()
                        + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                        + "-" + pd.getDate() + "-" + numberFormat.format(pd.getUsefull())
                        + "-" + numberFormat.format(pd.getScrsp());

            } else if(pd.getScrsp() > 0){
                log = pd.getID() + "-" + pd.getType() + "-" + pd.getInfo()
                        + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                        + "-" + pd.getDate() + "-" + useful + "-" + numberFormat.format(pd.getScrsp());

            }else if(pd.getUsefull() > 0){
                log = pd.getID() + "-" + pd.getType() + "-" + pd.getInfo()
                        + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                        + "-" + pd.getDate() + "-" + numberFormat.format(pd.getUsefull()) + "-" + scrap;

            } else {
                log = pd.getID() + "-" + pd.getType() + "-" + pd.getInfo()
                        + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                        + "-" + pd.getDate() + "-" + useful + "-" + scrap;
            }

            Log.d(TAG,"Insert: " + log);
            assets.add(log);
            idList.put("" + count, "" + pd.getID());
            nameList.put("" + count, "" + pd.getType());
            count++;
        }

        listDataHeader.add("Assets: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), assets); // Header, Child data
    }
}

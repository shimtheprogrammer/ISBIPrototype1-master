package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.PaidPayablesHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/27/2017.
 */

public class PaidPayablesDisplayFragment extends Fragment {
    public static String TAG = "PaidPayblDisplyFragment";

    SixELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the listview
        final View view = inflater.inflate(R.layout.fragment_pre_purchase_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData(getActivity());

        listAdapter = new SixELAdapter(getActivity(), listDataHeader,
                listDataChild, "PPA", false);

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

                return false;
            }
        });

        expListView.expandGroup(0);

        return view;
    }

    private void prepareListData(Activity activity) {
        PaidPayablesHandler ppdb = new PaidPayablesHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> payables = new ArrayList<>();

        List<PaidPayable> allPayables = ppdb.getAllPaidPayable();

        String title = "ID-To-Phone No.-Total-Method-Date";
        //payables.add(title);
        int count = 0;
        for (PaidPayable pd : allPayables) {
            String name[] = pd.getTo().split("-");
            String names = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            String log = pd.getID() + "-" + names + "-" + logPhone
                    + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getPayment()
                    + "-" + pd.getDate();
            Log.d(TAG,"Insert: " + log);
            payables.add(log);
            count++;
        }

        listDataHeader.add("Paid Payables: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), payables); // Header, Child data
    }
}

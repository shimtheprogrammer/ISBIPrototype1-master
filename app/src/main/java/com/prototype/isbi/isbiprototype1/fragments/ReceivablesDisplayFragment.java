package com.prototype.isbi.isbiprototype1.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.prototype.isbi.isbiprototype1.PaymentChoiceActivity;
import com.prototype.isbi.isbiprototype1.R;
import com.prototype.isbi.isbiprototype1.databaseHandlers.Receivable;
import com.prototype.isbi.isbiprototype1.databaseHandlers.ReceivablesHandler;
import com.prototype.isbi.isbiprototype1.expandableListAdapter.SixELAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MRuto on 3/27/2017.
 */

public class ReceivablesDisplayFragment extends Fragment {
    public static String TAG = "ReceivablDisplyFragment";

    SixELAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, String> idList = new HashMap<>();

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the listview
        final View view = inflater.inflate(R.layout.fragment_pre_purchase_display, container, false);
        final ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData(getActivity());

        listAdapter = new SixELAdapter(getActivity(), listDataHeader,
                listDataChild, "REC", false);

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
                for (String Key : idList.keySet()) {
                    if (Key.matches("" + childPosition)) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), PaymentChoiceActivity.class);
                        intent.putExtra("text", "Would you like to Update this Receivable?");
                        intent.putExtra("info", listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition));
                        intent.putExtra("FROM", "REC");
                        intent.putExtra("ID", "" + idList.get(Key));

                        startActivity(intent);
                    }
                }

                return false;
            }
        });

        expListView.expandGroup(0);

        return view;
    }

    private void prepareListData(Activity activity) {
        ReceivablesHandler rdb = new ReceivablesHandler(activity.getApplicationContext());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> payables = new ArrayList<>();

        List<Receivable> allReceivables = rdb.getAllReceivable();

        String title = "ID-From-Phone No.-Total-Due Date-Date";
        //payables.add(title);
        int count = 0;
        for (Receivable pd : allReceivables) {
            String name[] = pd.getFrom().split("-");
            String names = name[0];
            String logPhone = "";
            if(name.length > 1){
                logPhone = name[name.length - 1];
            }
            String log = pd.getID() + "-" + names + "-" + logPhone
                    + "-" + numberFormat.format(pd.getTotal()) + "-" + pd.getDate()
                    + "-" + pd.getOriginalDate();
            Log.d(TAG,"Insert: " + log);
            payables.add(log);
            idList.put("" + count, "" + pd.getID());
            count++;
        }

        listDataHeader.add("Receivables: " + count + "-" + title);

        listDataChild.put(listDataHeader.get(0), payables); // Header, Child data
    }
}
